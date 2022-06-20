package com.qihe.zzj.ui.activity;

import android.arch.lifecycle.Observer;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gyf.immersionbar.ImmersionBar;
import com.qihe.zzj.R;
import com.qihe.zzj.bean.FunctionRecycBean;
import com.qihe.zzj.databinding.ActivityTakePhotoBinding;
import com.qihe.zzj.util.CameraUtil;
import com.qihe.zzj.util.FileUtils;
import com.qihe.zzj.util.FrescoUtils;
import com.qihe.zzj.util.ImagePicker;
import com.qihe.zzj.util.SoundPlayer;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.view.LoadingDialog;
import com.zhihu.matisse.Matisse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.qihe.zzj.util.ImagePicker.REQUEST_CODE_CHOOSE;


public class TakePhotoActivity extends BaseActivity<ActivityTakePhotoBinding, BaseViewModel> implements SurfaceHolder.Callback {

    public static final int REQUEST_CODE_CAMERA = 999;
    public static final String TRANSMIT_IMAGE_URL = "imageUrl";
    private int delayedType;//0无延时，1延时3S, 2延时7S 3延时10S
    private int time;
    private long clicktime;
    private String imapath = "";
    private int mCameraId = 0;

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Handler handler;
    private SimpleDraweeView albumImage;
    private PopupWindow pop;
    private View popupView;
    private Timer timer = new Timer();

    //闪光灯模式 0:关闭 1: 开启 2: 自动
    private int light_num = 0;

//    private MagicEngine magicEngine;

    private FunctionRecycBean photo_data;


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_take_photo;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public boolean isShowBannerAd() {
        return false;
    }

    @Override
    public boolean isShowNativeAd() {
        return false;
    }

    @Override
    public boolean isShowVerticllAndStimulateAd() {
        return false;
    }

    @Override
    public void initParam() {
        super.initParam();
        SoundPlayer.init(this);
    }

    @Override
    public void initData() {
        super.initData();
        loadingDialog = new LoadingDialog(this);
        if (getIntent() != null) {
            photo_data = (FunctionRecycBean) getIntent().getSerializableExtra("data");
        }
//        mCamera=binding.surfaceView;
//        magicEngine = new MagicEngine.Builder().build(mCamera);

        initPopupWindow();
        ImmersionBar.with(this)
                .reset()
                .statusBarColor(R.color.color_1B1B1B)
                .fitsSystemWindows(true)
                .init();

        albumImage = binding.cameraAlbumImage;
        handler = new MyHandler(this);


        mHolder = binding.surfaceView.getHolder();
        final IOrientationEventListener orientationEventListener = new IOrientationEventListener(this);

        mHolder.addCallback(this);

        mHolder.setKeepScreenOn(true);//屏幕常亮
        mHolder.addCallback(new SurfaceHolder.Callback() {
            //当SurfaceHolder被创建的时候回调
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                orientationEventListener.enable();
            }

            //当SurfaceHolder的尺寸发生变化的时候被回调
            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            //当SurfaceHolder被销毁的时候回调
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                orientationEventListener.disable();
            }
        });

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.surfaceView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = params.width / 9 * 16;
        binding.surfaceView.setLayoutParams(params);


        initClick();
        getImages();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
        }
        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    public class IOrientationEventListener extends OrientationEventListener {

        public IOrientationEventListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (ORIENTATION_UNKNOWN == orientation) {
                return;
            }
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraId, info);
            orientation = (orientation + 45) / 90 * 90;
            int rotation = 0;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                rotation = (info.orientation - orientation + 360) % 360;
            } else {
                rotation = (info.orientation + orientation) % 360;
            }
            if (null != mCamera) {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setRotation(rotation);
                mCamera.setParameters(parameters);
            }
        }
    }


    private void initPopupWindow() {
        popupView = LayoutInflater.from(this).inflate(R.layout.popupwindow_delayed, null);
        TextView text1 = popupView.findViewById(R.id.popupwindow_text1);
        TextView text2 = popupView.findViewById(R.id.popupwindow_text2);
        TextView text3 = popupView.findViewById(R.id.popupwindow_text3);
        TextView text4 = popupView.findViewById(R.id.popupwindow_text4);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                }
                delayedType = 1;
                binding.ivDelay.setImageDrawable(getResources().getDrawable(R.drawable.san_icon));
                binding.tvDelay.setTextColor(getResources().getColor(R.color.color_FDCA41));
            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                }
                delayedType = 2;
                binding.ivDelay.setImageDrawable(getResources().getDrawable(R.drawable.qi_icon));
                binding.tvDelay.setTextColor(getResources().getColor(R.color.color_FDCA41));
            }
        });
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                }
                delayedType = 3;
                binding.ivDelay.setImageDrawable(getResources().getDrawable(R.drawable.sih_icon));
                binding.tvDelay.setTextColor(getResources().getColor(R.color.color_FDCA41));
            }
        });

        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                }
                delayedType = 0;
                binding.ivDelay.setImageDrawable(getResources().getDrawable(R.drawable.yanchi_icon));
                binding.tvDelay.setTextColor(getResources().getColor(R.color.color_FFFFFF));
            }
        });
        pop = new PopupWindow(popupView, getResources().getDisplayMetrics().widthPixels, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void showPopupWindow() {
        if (pop.isShowing()) {
            pop.dismiss();
        } else {
            int[] location = new int[2];
            binding.cameraTopLayout.getLocationOnScreen(location);
            pop.showAtLocation(binding.cameraTopLayout, Gravity.NO_GRAVITY, location[0], location[1] + binding.cameraTopLayout.getHeight());
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }
        MineThread mThread = new MineThread();
        mThread.start();
    }

    private void initClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tvGonglue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TakePhotoActivity.this, ShootTipsActivity.class));
            }
        });

        binding.ivXuanzhuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCamera();
            }
        });

        binding.ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQuickClick()) {
                    return;
                }
                takepictrueDelayed();
            }
        });

        binding.cameraAlbumImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.getImagesPath(TakePhotoActivity.this, 1, false);
            }
        });

        binding.llDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

        LiveDataBus.get().with(LiveBusConfig.backHome, String.class).observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                finish();
            }
        });
    }

    /**
     * 延迟拍照
     */
    private void takepictrueDelayed() {
        switch (delayedType) {
            case 0:
                captrue();
                return;
            case 1:
                time = 3;
                break;
            case 2:
                time = 7;
                break;
            case 3:
                time = 10;
                break;
        }
        binding.cameraDelayedNum.setVisibility(View.VISIBLE);
        timer.schedule(new MyTask(), 0, 1000);
        binding.cameraDelayedNum.setText(time + "");
    }

    /**
     * 拍照
     */
    private void captrue() {
        SoundPlayer.playSound(1);
//        magicEngine.savePicture(com.zero.magicshow.common.utils.BaseUtil.getRandomTempImageFile(), new SavePictureTask.OnPictureSaveListener() {
//            @Override
//            public void onSaved(MagicShowResultEntity resultEntity) {
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("data",photo_data);
//                bundle.putString(EditePhotoActivity.PHOTO_MODEL,resultEntity.getFilePath());
//                startActivity(EditePhotoActivity.class,bundle);
//                finish();
////                Log.e("HongLi","保存成功:" + (System.nanoTime() / 1000000 - startTime));
////                RxBus.getInstance().post(resultEntity, Constants.RX_JAVA_TYPE_CAMERA_SHOOT);
////                doFinishAction();
////                tv_delayed_num.setVisibility(View.GONE);
//            }
//        });
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                setTakePhoto(data);
//                ToastUtils.showShort("拍照成功！");
//                BufferedOutputStream bos = null;
//                FileOutputStream fos = null;
//                File file = null;
//                try {
//                    file = getOutputMediaFile();
//                    fos = new FileOutputStream(file);
//                    bos = new BufferedOutputStream(fos);
//                    bos.write(data);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (bos != null) {
//                        try {
//                            bos.close();
//                            getToConfirmPictrue(file.getPath());
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                    if (fos != null) {
//                        try {
//                            fos.close();
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                }
//                mCamera.stopPreview();
            }
        });
    }

    private String setTakePhotoPic(byte[] data) {


        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = getOutputMediaFile();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                    return file.getPath();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return "";
    }

    private Disposable disposable;
    private LoadingDialog loadingDialog;

    private void setTakePhoto(final byte[] data) {

        loadingDialog.show();
        disposable = Single
                .create(new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(SingleEmitter<String> e) {
                        e.onSuccess(setTakePhotoPic(data));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() {
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String musicInfos) {
                        loadingDialog.dismiss();

                        getToConfirmPictrue(musicInfos);


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        com.xinqidian.adcommon.util.ToastUtils.show("拍照失败");
                        loadingDialog.dismiss();
                    }
                });


    }


    public void getToConfirmPictrue(String path) {
//        Intent intent = new Intent(this, PictrueConfirmActivity.class);
//        intent.putExtra(IMAGEPATH, path);
//        intent.putExtra(SelectSizeActivity.SPECID, specId + "");
//        startActivity(intent);
//        Intent intent = new Intent(this, AlbumActivity.class);
//        intent.putExtra(Constants.TRANSMIT_IMAGE_URL,path);
//        startActivity(intent);

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", photo_data);
        bundle.putString(EditePhotoActivity.PHOTO_MODEL, path);
        startActivity(EditePhotoActivity.class, bundle);
        finish();
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);   //, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        Uri uri = Uri.fromFile(new File(path));
        intent.setData(uri);
        sendBroadcast(intent);
//
//        ImagePicker.getImagesPath(TakePhotoActivity.this, 1, false);
//
//
//        MagicShowManager.getInstance().openCameraAndEdit(TakePhotoActivity.this, new ImageEditCallBack() {
//            @Override
//            public void onCompentFinished(MagicShowResultEntity magicShowResultEntity) {
//
//
//
//
//                Log.e("HongLi","获取图片地址:" + magicShowResultEntity.getAngle() + ";" + magicShowResultEntity.getFilePath());
//                BaseUtil.showToast(TakePhotoActivity.this,magicShowResultEntity.getFilePath());
//            }
//        });
    }

    private File getOutputMediaFile() {
        //图片文件

        String timeStamp = new SimpleDateFormat("yyyy-MMdd-HHmmss").format(new Date());
        File tempFile = new File(FileUtils.TAKE_PHOTO_FILE_PATH, "verify_photo_" + timeStamp + ".png");

        return tempFile;
    }

    private boolean isQuickClick() {
        if (System.currentTimeMillis() - clicktime < 1000) {
            return true;
        }
        clicktime = System.currentTimeMillis();
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
            if (mHolder != null) {
                startPreview(mCamera, mHolder);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();

    }


    /**
     * 切换摄像头
     */
    public void switchCamera() {
//        magicEngine.switchCamera();

        releaseCamera();
        mCameraId = (mCameraId + 1) % mCamera.getNumberOfCameras();
        mCamera = getCamera(mCameraId);
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }

    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {

        }
        setFlash(camera);
        return camera;
    }


    private void setFlash(Camera camera) {
        if (mCameraId == 1) {
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        if (light_num == 0) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
        } else {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);//开启
            camera.setParameters(parameters);
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        if (camera == null) return;
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //亲测的一个方法 基本覆盖所有手机 将预览矫正
            CameraUtil.getInstance().setCameraDisplayOrientation(this, mCameraId, camera);
//            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置
     */
    private void setupCamera(Camera camera) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();//获取所有支持的camera尺寸
            Camera.Size optionSize = getOptimalPreviewSize(sizeList, binding.surfaceView.getHeight(), binding.surfaceView.getWidth());//获取一个最为适配的camera.size
            parameters.setPreviewSize(optionSize.width, optionSize.height);//把camera.size赋值到parameters
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            parameters.setPictureSize(optionSize.width, optionSize.height);
            camera.setParameters(parameters);
        } catch (Exception e) {
            KLog.e("CameraActivity", "set parameters fail");
        }
    }


    /**
     * 解决预览变形问题
     *
     * @param sizes
     * @param w
     * @param h
     * @return
     */
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int h, int w) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            //相册选中图片路径
            List<String> paths = Matisse.obtainPathResult(data);
            if (paths != null && paths.size() > 0 && !TextUtils.isEmpty(paths.get(0))) {
                getToConfirmPictrue(paths.get(0));
            }
        }
    }

    @Override
    public void onStimulateSuccessCall() {

    }

    @Override
    public void onStimulateSuccessDissmissCall() {

    }


    class MineThread extends Thread {

        @Override
        public void run() {
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = TakePhotoActivity.this
                    .getContentResolver();

            // 只查询jpeg和png的图片
            Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png", "image/bmp"},
                    MediaStore.Images.Media.DATE_ADDED + " DESC");

            while (mCursor.moveToNext()) {
                // 获取图片的路径
                String path = mCursor.getString(mCursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));

                //预先验证图片的有效性
                final File file = new File(path);
                if (!file.exists()) {
//                    throw new IllegalArgumentException("Uri 文件不存在");
                    //如果此处抛出异常 说明预见检查到无效的图片 此时 开发者把 下面 continue 放开  再把 抛出异常的代码注释即可
                    continue;
                }
                // 拿到第一张图片的路径
                if (TextUtils.isEmpty(imapath)) {
                    imapath = path;
                    if (binding.cameraAlbumImage != null) {
                        Message message = new Message();
                        message.what = 101;
                        message.obj = imapath;
                        handler.sendMessage(message);
                    }
                    break;
                }
            }
        }
    }

    class MyTask extends TimerTask {
        @Override
        public void run() {
            if (!isFinishing()) {
                if (this == null) {
                    return;
                }
                TakePhotoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (time == 0) {
                            cancel();
                            captrue();
                            binding.cameraDelayedNum.setVisibility(View.GONE);
                        } else {
                            binding.cameraDelayedNum.setText(time + "");
                            time--;
                        }
                    }
                });
            } else {
                if (this == null) {
                    return;
                }
                TakePhotoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (this == null) {
                            cancel();
                            return;
                        }
                        if (time <= 0) {
                            cancel();
                            captrue();
                            binding.cameraDelayedNum.setVisibility(View.GONE);
                        } else {
                            TakePhotoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.cameraDelayedNum.setText(time + "");
                                }
                            });
                        }
                    }
                });

            }
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<TakePhotoActivity> mWeakRefrence;

        public MyHandler(TakePhotoActivity activity) {
            mWeakRefrence = new WeakReference<TakePhotoActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final TakePhotoActivity activity = mWeakRefrence.get();
            if (activity != null) {
                switch (msg.what) {
                    case 101:
                        if (activity.albumImage != null) {
                            String loacalpath = (String) msg.obj;
                            FrescoUtils.getInstance().showImageFile(activity.albumImage, loacalpath);
                        }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
