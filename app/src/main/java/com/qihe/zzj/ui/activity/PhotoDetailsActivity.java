package com.qihe.zzj.ui.activity;

import android.annotation.TargetApi;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.gyf.immersionbar.ImmersionBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qihe.zzj.R;
import com.qihe.zzj.databinding.ActivityPhotoDetailsBinding;
import com.qihe.zzj.bean.FunctionRecycBean;
import com.qihe.zzj.util.ImagePicker;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.util.ToastUtils;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zhihu.matisse.Matisse;

import java.util.ArrayList;
import java.util.List;

import static com.qihe.zzj.util.ImagePicker.REQUEST_CODE_CHOOSE;

public class PhotoDetailsActivity extends BaseActivity<ActivityPhotoDetailsBinding, BaseViewModel> {

    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_PHOTO2 = 3;
    public static final int CROP_PHOTO = 2;

    private FunctionRecycBean photo_data;
//    private Uri imageUri;

    private List<Drawable> images;


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_photo_details;
    }

    @Override
    public void initParam() {
        super.initParam();
        images = new ArrayList<>();
        images.add(getResources().getDrawable(R.drawable.banner_yi));
        images.add(getResources().getDrawable(R.drawable.banner_er));
        images.add(getResources().getDrawable(R.drawable.banner_san));
        images.add(getResources().getDrawable(R.drawable.banner_si));
        if (getIntent() != null) {
            photo_data = (FunctionRecycBean) getIntent().getSerializableExtra("data");
        }
    }

    @Override
    public void initData() {
        super.initData();

        ImmersionBar.with(this)
                .reset()
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .fitsSystemWindows(false)
                .init();

        binding.tvName.setText(photo_data.getName());
//        binding.tvMm.setText(photo_data.getMm());
//        binding.tvPx.setText(data.getPx());
        binding.tvXPx.setText(photo_data.getX_px());
        binding.tvYPx.setText(photo_data.getY_px());


        binding.banner.setImageLoader(new GlideImageLoader()).setImages(images).setBannerStyle(BannerConfig.CIRCLE_INDICATOR).start();
        initClick();
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", photo_data);
            startActivity(TakePhotoActivity.class, bundle);
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            ToastUtils.show("没有权限无法进行此操作");
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {"android.permission.CAMERA"};
            if (
                    checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 1024);
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", photo_data);
                startActivity(TakePhotoActivity.class, bundle);
            }
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", photo_data);
            startActivity(TakePhotoActivity.class, bundle);
        }
    }

    private void initClick() {
//        PathConfig.setTempCache("/sdcard/DCIM");
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tvTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    checkAndRequestPermission();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", photo_data);
                    startActivity(TakePhotoActivity.class, bundle);
                }


//                MagicShowManager.getInstance().openCameraAndEdit(PhotoDetailsActivity.this, new ImageEditCallBack() {
//                    @Override
//                    public void onCompentFinished(MagicShowResultEntity magicShowResultEntity) {
//                        Log.e("HongLi","获取图片地址:" + magicShowResultEntity.getAngle() + ";" + magicShowResultEntity.getFilePath());
//                        //magicShowResultEntity.getFilePath() 图片路径
//                        BaseUtil.showToast(PhotoDetailsActivity.this,magicShowResultEntity.getFilePath());
//                        IdPhotoUtil.makeIdPhoto(1,new File(magicShowResultEntity.getFilePath()),PhotoDetailsActivity.this,loadingDialog);
//
//
//                    }
//                });
//                startActivity(new Intent(PhotoDetailsActivity.this,TakePhotoActivity.class));
            }
        });

        binding.tvPhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.getImagesPath(PhotoDetailsActivity.this, 1, false);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            //返回相册选中图片路径
            List<String> paths = Matisse.obtainPathResult(data);
            if (paths != null && paths.size() > 0 && !TextUtils.isEmpty(paths.get(0))) {
//                Intent intent = new Intent(this, AlbumActivity.class);
//                intent.putExtra(Constants.TRANSMIT_IMAGE_URL,paths.get(0));
//                startActivity(intent);

                Bundle bundle = new Bundle();
                bundle.putSerializable("data", photo_data);
                bundle.putString(EditePhotoActivity.PHOTO_MODEL, paths.get(0));
                startActivity(EditePhotoActivity.class, bundle);


//                MagicShowManager.getInstance().openEdit(PhotoDetailsActivity.this,paths.get(0), new ImageEditCallBack() {
//                    @Override
//                    public void onCompentFinished(MagicShowResultEntity magicShowResultEntity) {
//                        //magicShowResultEntity.getFilePath() 图片路径
//                        BaseUtil.showToast(PhotoDetailsActivity.this,magicShowResultEntity.getFilePath());
//                    }
//                });
//                Intent intent = new Intent(this, PictrueConfirmActivity.class);
//                intent.putExtra(SelectSizeActivity.TYPE, 1);
//                intent.putExtra(SelectSizeActivity.IMAGEPATH, paths.get(0));
//                startActivity(intent);
            }
        }
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
    public void onStimulateSuccessCall() {
    }

    @Override
    public void onStimulateSuccessDissmissCall() {
    }

    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageDrawable((Drawable) path);
        }

        @Override
        public ImageView createImageView(Context context) {
            RoundedImageView imageView = new RoundedImageView(context);
            imageView.setCornerRadius(16);
            return imageView;
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
//        LiveDataBus.get().with(LiveBusConfig.idPhotoModel, IdPhotoModel.DataBean.class).observe(this, new Observer<IdPhotoModel.DataBean>() {
//            @Override
//            public void onChanged(@Nullable IdPhotoModel.DataBean dataBean) {
//                KLog.e("path--->", new Gson().toJson(dataBean));
//            }
//        });


        LiveDataBus.get().with(LiveBusConfig.backHome, String.class).observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                finish();
            }
        });
    }


}
