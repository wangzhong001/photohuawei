package com.qihe.zzj.ui.activity;

import android.arch.lifecycle.Observer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;

import com.cfox.ivcliplib.CImageUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.qihe.zzj.BR;
import com.qihe.zzj.R;
import com.qihe.zzj.bean.FunctionRecycBean;
import com.qihe.zzj.databinding.ActivityMakeBinding;
import com.qihe.zzj.util.FileUtils;
import com.qihe.zzj.view.MakePhotoViewModel;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.login.IdPhotoModel;
import com.xinqidian.adcommon.util.IdPhotoUtil;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xinqidian.adcommon.view.LoadingDialog;
import com.xinqidian.adcommon.view.SureDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lipei on 2020/6/29.
 */

public class MakeActivity extends BaseActivity<ActivityMakeBinding, MakePhotoViewModel> {

    public static final String PIC = "pic";
    private IdPhotoModel.DataBean dataBean;
    private FunctionRecycBean photo_data;
    private int type = 0;

    private SureDialog sureDialog;

    private Bitmap bitmap;

    private int maxSize = 100;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_make;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
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
    public void onStimulateSuccessCall() {

    }

    @Override
    public void onStimulateSuccessDissmissCall() {

    }

    @Override
    public void initData() {
        super.initData();

        loadingDialog = new LoadingDialog(this);

        ImmersionBar.with(this)
                .reset()
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .init();

        sureDialog = new SureDialog(MakeActivity.this, "主人离生成证件照只有一步之遥离", "确定退出吗")
                .addItemListener(new SureDialog.ItemListener() {
                    @Override
                    public void onClickSure() {
                        finish();
                        ;
                    }

                    @Override
                    public void onClickCanel() {

                    }
                });
        if (getIntent() != null) {
            photo_data = (FunctionRecycBean) getIntent().getSerializableExtra("data");
            dataBean = (IdPhotoModel.DataBean) getIntent().getSerializableExtra(PIC);
            viewModel.withString.set(photo_data.getX_px() + "px");
            viewModel.heightString.set(photo_data.getY_px() + "px");
            maxSize = photo_data.getMaxSize();
            KLog.e("maxSize--->", maxSize);
            if (maxSize == 0) {
                maxSize = 100;
            }


        }


        bitmap = BitmapFactory.decodeFile(dataBean.getRedUrl());

        binding.picIv.setImageBitmap(bitmap);

        FileUtils.deleteFile(dataBean.getRedUrl());


        binding.picIv.setDrawingCacheEnabled(true);

        binding.picIv.setBackgroundColor(getResources().getColor(R.color.photo_red));
        binding.picIv.setDrawingCacheEnabled(false);

//        getFileBitMap();
    }


//    private void getFileBitMap() {
//
//        File newFile = CompressHelper.getDefault(this).compressToFile(new File(dataBean.getRedUrl()));
//
//        String path = newFile.getPath();
//        bitmap = BitmapFactory.decodeFile(path);
//        binding.picIv.setBitmap(bitmap);
//
//        binding.picIv.setDrawingCacheEnabled(true);
//
//        binding.picIv.setBackgroundColor(getResources().getColor(R.color.photo_red));
//        binding.picIv.setDrawingCacheEnabled(false);
//        FileUtils.deleteFile(dataBean.getRedUrl());
//
//
//    }


    public Bitmap zoomBitmap(Bitmap bitmap, float vw, float vh) {
        float width = bitmap.getWidth();//获得图片宽高
        float height = bitmap.getHeight();

        float scaleWidht, scaleHeight, x, y;//图片缩放倍数以及x，y轴平移位置
        Bitmap newbmp = null; //新的图片
        Matrix matrix = new Matrix();//变换矩阵
        if ((width / height) <= vw / vh) {//当宽高比大于所需要尺寸的宽高比时以宽的倍数为缩放倍数
            scaleWidht = vw / width;
            scaleHeight = scaleWidht;
            y = ((height * scaleHeight - vh) / 2) / scaleHeight;// 获取bitmap源文件中y做表需要偏移的像数大小
            x = 0;
        } else {
            scaleWidht = vh / height;
            scaleHeight = scaleWidht;
            x = ((width * scaleWidht - vw) / 2) / scaleWidht;// 获取bitmap源文件中x做表需要偏移的像数大小
            y = 0;
        }
        matrix.postScale(scaleWidht / 1f, scaleHeight / 1f);
        try {
            if (width - x > 0 && height - y > 0 && bitmap != null)//获得新的图片 （原图，x轴起始位置，y轴起始位置，x轴结束位置，Y轴结束位置，缩放矩阵，是否过滤原图）为防止报错取绝对值
                newbmp = Bitmap.createBitmap(bitmap, (int) Math.abs(x), (int) Math.abs(y), (int) Math.abs(vw - x),
                        (int) Math.abs(vh - y), matrix, false);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
        } catch (Exception e) {//如果报错则返回原图，不至于为空白
            e.printStackTrace();
            return bitmap;
        }
        return newbmp;
    }


    public Bitmap ratio(String imgPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩
//        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }


    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureDialog != null) {
                    sureDialog.show();
                }
            }
        });
        binding.redView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 0;
                binding.picIv.setDrawingCacheEnabled(true);
                binding.picIv.setBackgroundColor(getResources().getColor(R.color.photo_red));
                binding.picIv.setDrawingCacheEnabled(false);
//                viewModel.picUrl.set(dataBean.getRedUrl());
            }
        });

        binding.viewWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                binding.picIv.setDrawingCacheEnabled(true);
                binding.picIv.setBackgroundColor(getResources().getColor(R.color.white));
                binding.picIv.setDrawingCacheEnabled(false);
//                viewModel.picUrl.set(dataBean.getWhiteUrl());
            }
        });

        binding.blueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                binding.picIv.setDrawingCacheEnabled(true);
                binding.picIv.setBackgroundColor(getResources().getColor(R.color.photo_blue));
                binding.picIv.setDrawingCacheEnabled(false);
//                viewModel.picUrl.set(dataBean.getBlueUrl());
            }
        });
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savaPic(FileUtils.UPDATE_PHOTO_FILE_PATH + System.currentTimeMillis() + ".jpg");
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("data", photo_data);
//                bundle.putInt("type", type);
//                bundle.putString(SavePhotoActivity.PIC, viewModel.picUrl.get());
//                startActivity(SavePhotoActivity.class, bundle);
            }
        });
        LiveDataBus.get().with(LiveBusConfig.backHome, String.class).observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                finish();
            }
        });
        LiveDataBus.get().with(LiveBusConfig.idPhotoModel, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                FileUtils.deleteFile(fileFinalyPath);

                Bundle bundle = new Bundle();
                bundle.putSerializable("data", photo_data);
                bundle.putInt("type", type);
                bundle.putString(SavePhotoActivity.PIC, s);
                startActivity(SavePhotoActivity.class, bundle);
//                finish();
            }
        });
        LiveDataBus.get().with(LiveBusConfig.makeFail, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                ToastUtils.show("制作失败");
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", photo_data);
                bundle.putInt("type", type);
                bundle.putBoolean("fail", true);
                bundle.putString(SavePhotoActivity.PIC, fileFinalyPath);
                startActivity(SavePhotoActivity.class, bundle);
//                finish();
            }
        });
    }

    @Override //再按一次退出程序
    public void onBackPressed() {
        if (sureDialog != null) {
            sureDialog.show();
        }
    }

    private LoadingDialog loadingDialog;

    public Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        // load the origial Bitmap
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculate the scale
        float localScale = ((float) width) / height;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        KLog.e("scale--->", localScale + "--->" + scaleWidth + "--->" + scaleHeight);

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);

        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return resizedBitmap;
    }

    public String saveBitmap(Bitmap bitmap, String path) {
//        Log.d(TAG, "saveBitmap savePath : " + savePath);
        try {
            Bitmap newBitmaps = CImageUtils.instance(MakeActivity.this).cropToBitmap(bitmap, Integer.parseInt(photo_data.getX_px()), Integer.parseInt(photo_data.getY_px()));
            Bitmap newBitmap = resizeImage(newBitmaps, Integer.parseInt(photo_data.getX_px()), Integer.parseInt(photo_data.getY_px()));
//        Bitmap newBitmap = CImageUtils.instance(MakeActivity.this).cropToBitmap(bitmap, Integer.parseInt(photo_data.getX_px()), Integer.parseInt(photo_data.getY_px()));

            String savePath;
            File filePic;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                savePath = path;
            } else {
                return path;
            }
            filePic = new File(savePath);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            ToastUtils.show("制作失败,请联系客服");
        }
        return compressImageByPath(path, maxSize);
    }

    public String compressImageByPath(String srcPath, int aimSize) {
        String outPath = FileUtils.UPDATE_PHOTO_FILE_PATH + System.currentTimeMillis() + ".jpg";
        // 注意savePath的文件夹和文件的判断
        Bitmap imgBitmap = BitmapFactory.decodeFile(srcPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int percent = 100;// 定义压缩比例，初始为不压缩
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, percent, baos);
        int currentSize = baos.toByteArray().length / 1024;
        while (currentSize > aimSize) {// 循环判断压缩后图片是否大于目的大小，若大于则继续压缩
            baos.reset();// 重置baos，即清空baos
            //注意：此处该方法的第一个参数必须为JPEG，若为PNG则无法压缩。
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, percent, baos);
            currentSize = baos.toByteArray().length / 1024;
            percent -= 5;
            if (percent <= 0) {
                break;
            }
        }
        try {//将数据写入输出流
            FileOutputStream fos = new FileOutputStream(outPath);
            baos.writeTo(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {//清空缓存，关闭流
                baos.flush();
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!imgBitmap.isRecycled()) {
            imgBitmap.recycle();//回收图片所占的内存
            System.gc();//提醒系统及时回收
        }
        FileUtils.deleteFile(srcPath);
        return outPath;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        if (processedImage != null && !processedImage.isRecycled()) {
            processedImage.recycle();
            processedImage = null;
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    private Disposable disposable;
    private Bitmap processedImage;
    private String fileFinalyPath;

    private void savaPic(final String path) {
        loadingDialog.show();
        disposable = Single
                .create(new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(SingleEmitter<String> e) {
                        binding.picIv.setDrawingCacheEnabled(true);
                        Bitmap processedImage = Bitmap.createBitmap(binding.picIv.getDrawingCache());
                        e.onSuccess(saveBitmap(processedImage, path));
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
//                        binding.picIv.setDrawingCacheEnabled(true);
                        binding.picIv.setDrawingCacheEnabled(false);
                        fileFinalyPath = musicInfos;
                        IdPhotoUtil.savaPhoto(new File(musicInfos), loadingDialog, MakeActivity.this);

//                        loadingDialog.dismiss();
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("data", photo_data);
//                        bundle.putInt("type", type);
//                        bundle.putString(SavePhotoActivity.PIC, musicInfos);
//                        startActivity(SavePhotoActivity.class, bundle);
//                        finish();
//                        doNextAction(musicInfos);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        loadingDialog.dismiss();
                        binding.picIv.setDrawingCacheEnabled(false);
//                        binding.picIv.setDrawingCacheEnabled(false);
                        ToastUtils.show("制作失败,请联系客服");
                    }
                });
    }

}
