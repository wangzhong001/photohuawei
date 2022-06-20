package com.qihe.zzj.ui.activity;

import android.arch.lifecycle.Observer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLException;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.common.MLPosition;
import com.huawei.hms.mlsdk.face.MLFace;
import com.huawei.hms.mlsdk.face.MLFaceAnalyzer;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentation;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationAnalyzer;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationSetting;
import com.nanchen.compresshelper.CompressHelper;
import com.qihe.zzj.R;
import com.qihe.zzj.bean.FunctionRecycBean;
import com.qihe.zzj.databinding.ActivityEditePhotoBinding;
import com.qihe.zzj.util.AnimationUtil;
import com.qihe.zzj.util.FileUtils;
import com.qihe.zzj.util.MakeupBeautyUtils;
import com.qihe.zzj.view.SmallFaceView;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.login.IdPhotoModel;
import com.xinqidian.adcommon.util.IdPhotoUtil;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xinqidian.adcommon.view.ErrorDialog;
import com.xinqidian.adcommon.view.LoadingDialog;
import com.xinqidian.adcommon.view.SureDialog;


import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EditePhotoActivity extends BaseActivity<ActivityEditePhotoBinding, BaseViewModel> {
    public static final String PHOTO_MODEL = "PHOTO_MODEL";
    private String imageUrl;


    private FunctionRecycBean photo_data;


    private LoadingDialog loadingDialog;

    private LoadingDialog makeLoading;

    private ErrorDialog errorDialog;

    private int number;

    private SureDialog backSureDialog;

    private MakeupBeautyUtils makeupBeautyUtils;
    private Bitmap bm;
    private float meiYanPro;
    private int moPiPro;

    private String postion = "postion";

    private String changePostion = "changePostion";

    private SureDialog sureDialog;
    private HashMap<String, Bitmap> bitmapHashMap = new HashMap<>();

    private HashMap<String, Bitmap> changeHashMap = new HashMap<>();

    private String filePath;


    private Disposable setDisposable;
    private boolean isMeibai = true;

    private Handler handlerMopi = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj instanceof Bitmap) {
//                if(msg.what == 0) {
//                    drawMakeup((Bitmap) msg.obj);
//                    magnifyEye((Bitmap) msg.obj);
//                }else if(msg.what == 1){
//                postion += 1;
                binding.smallView.setBitmap((Bitmap) msg.obj);
                makeupBeautyUtils.progressSkinWhiteness(changeHashMap.get(changePostion), handler, meiYanPro);
                if(loadingDialog!=null&&loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }

//                bm=(Bitmap) msg.obj;
//                }
            }
            return false;
        }
    });


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj instanceof Bitmap) {

                bitmapHashMap.put(postion, (Bitmap) msg.obj);

            }
            return false;
        }
    });


    private Handler handlerMeiBai = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj instanceof Bitmap) {

//                postion += 1;
                binding.smallView.setBitmap((Bitmap) msg.obj);
                makeupBeautyUtils.progress(changeHashMap.get(changePostion), handler, moPiPro);
                if(makeLoading!=null&&makeLoading.isShowing()){
                    makeLoading.dismiss();
                }

            }
            return false;
        }
    });


    private boolean isShouLian;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_edite_photo;
    }

    @Override
    public void initParam() {
        super.initParam();
    }

    private boolean isHasChange;

    private boolean isAllChange;

    private void setStringToBitmap() {
        if(makeLoading==null){
            makeLoading=new LoadingDialog(this,true);
        }
        makeLoading.show();
        setDisposable = Single
                .create(new SingleOnSubscribe<Bitmap>() {
                    @Override
                    public void subscribe(SingleEmitter<Bitmap> e) {
                        e.onSuccess(getFileBitMap());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() {
                    }
                })
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap musicInfos) {
                        bitmapHashMap.put(postion, musicInfos);
                        changeHashMap.put(changePostion, musicInfos);
//                        binding.smallView.setBitmap(musicInfos);
                        binding.smallView.setNoChnageBitMap(musicInfos);
//                        loadingDialog.dismiss();
                        meiYanPro=2;
                        binding.meibaiSeekbar.setProgress(24);
////
//
////                        moPiPro=320;
////                        binding.mopiSeekbar.setProgress(32);
//
//
                        makeupBeautyUtils.progressSkinWhiteness(bitmapHashMap.get(postion), handlerMeiBai, meiYanPro);

//                        makeupBeautyUtils.progress(bitmapHashMap.get(postion), handlerMopi, moPiPro);


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                });
    }

    @Override
    public void initData() {
        super.initData();
        makeLoading=new LoadingDialog(this,true);
        loadingDialog = new LoadingDialog(this);
        errorDialog = new ErrorDialog(this);
        binding.smallView.setChangeInterface(new SmallFaceView.ChangeInterface() {
            @Override
            public void change() {
                isAllChange = true;

                isHasChange = true;
            }
        });


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Gson gson=new Gson();
//               IdPhotoModel.DataBean dataBean= gson.fromJson("{\"blueUrl\":\"https://oapi.aisegment.com/static/photo/p1/20200629/apiresult/6c0/6c05dee7aba9484ca6f8a1e2cc8de1ca.jpg\",\"height\":413,\"redUrl\":\"https://oapi.aisegment.com/static/photo/p1/20200629/result/857/857546679bd3463d9552269d743c38f9.jpg\",\"status\":\"0\",\"whiteUrl\":\"https://oapi.aisegment.com/static/photo/p1/20200629/result/797/797656e9568c436bb38a8a5d0f8f4176.jpg\",\"width\":295}",IdPhotoModel.DataBean.class);
//
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("data",photo_data);
//                bundle.putSerializable(MakeActivity.PIC,dataBean);
//                startActivity(MakeActivity.class,bundle);

//                if(!isAllChange){
//                    doNextAction(imageUrl);
//
//                    return;
//                }
                if (isShouLian && isHasChange) {
                    ToastUtils.show("您的瘦脸操作未完成");
                    return;
                }
//                Bitmap newBitmap=FaceCj.cutFace(binding.smallView.getBitmap(),EditePhotoActivity.this);
//                binding.smallView.setBitmap(newBitmap);

//                number = (int) SharedPreferencesUtil.getParam(Contans.orderNumber, 0);
//
//                if (number >= 2) {
//                    if (sureDialog == null) {
//                        sureDialog = new SureDialog(EditePhotoActivity.this, "您未支付的订单较多!请先去支付再制作新的证件照吧", "支付订单", "我在想想", "去支付")
//                                .addItemListener(new SureDialog.ItemListener() {
//                                    @Override
//                                    public void onClickSure() {
////                                ToastUtils.show("去支付");
//                                        startActivity(OrderActivity.class);
//
//                                    }
//
//                                    @Override
//                                    public void onClickCanel() {
//
//                                    }
//                                });
//                    }
//                    sureDialog.show();
//                    return;
//                }
//


                getFace();
//                savaPic(FileUtils.UPDATE_PHOTO_FILE_PATH+ System.currentTimeMillis() + ".jpg");

            }
        });
        makeupBeautyUtils = new MakeupBeautyUtils();

        if (getIntent() != null) {
            photo_data = (FunctionRecycBean) getIntent().getSerializableExtra("data");
            imageUrl = getIntent().getStringExtra(PHOTO_MODEL);

        }
        ImmersionBar.with(this)
                .reset()
                .statusBarColor(R.color.color_1B1B1B)
                .fitsSystemWindows(true)
                .init();

        setStringToBitmap();
////        FileInputStream fis = null;
//        try {
////            fis = new FileInputStream(imageUrl);
//
////            Bitmap b = BitmapFactory.decodeFile(imageUrl);
//////            bitmap = Bitmap.createScaledBitmap(b,b.getWidth()/3,b.getHeight()/3,false);
////            bm = Bitmap.createBitmap(b.getWidth(),b.getHeight(), Bitmap.Config.ARGB_8888);
//
//            File newFile = CompressHelper.getDefault(this).compressToFile(new File(imageUrl));
//            String path = newFile.getPath();
//
//            bm = BitmapFactory.decodeFile(path);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }




    }







    private Bitmap getFileBitMap() {
//
//        File newFile = CompressHelper.getDefault(this).compressToFile(new File(imageUrl));




        File newFile =   new CompressHelper.Builder(this)
                .setMaxWidth(720)  // 默认最大宽度为720
                .setMaxHeight(960) // 默认最大高度为960
                .setQuality(80)    // 默认压缩质量为80
                .setFileName(System.currentTimeMillis()+photo_data.getName()) // 设置你需要修改的文件名
                .setCompressFormat(Bitmap.CompressFormat.PNG) // 设置默认压缩为jpg格式
                .setDestinationDirectoryPath(FileUtils.UPDATE_PHOTO_FILE_PATH)
                .build()
                .compressToFile(new File(imageUrl));

        String path = newFile.getPath();
        bm = BitmapFactory.decodeFile(path);
//                bm = comp(BitmapFactory.decodeFile(imageUrl));

        FileUtils.deleteFile(newFile);

//        bm = BitmapFactory.decodeFile(imageUrl);

        return bm;

    }

    private Disposable disposable;


    private void savaPic(final String path) {




        disposable = Single
                .create(new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(SingleEmitter<String> e) {
                        e.onSuccess(saveBitmap(foreground, path));
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
                        filePath = musicInfos;

                        IdPhotoModel.DataBean dataBean = new IdPhotoModel.DataBean();
                        dataBean.setBlueUrl(musicInfos);
                        dataBean.setRedUrl(musicInfos);
                        dataBean.setWhiteUrl(musicInfos);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", photo_data);
                        bundle.putSerializable(MakeActivity.PIC, dataBean);
                        startActivity(MakeActivity.class, bundle);

                        finish();
//                        doNextAction(musicInfos);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        loadingDialog.dismiss();
                        ToastUtils.show("制作失败,请联系客服");
                    }
                });
    }








    public String saveBitmap(Bitmap bitmap, String path) {
//       Bitmap newBitmap= CImageUtils.instance(EditePhotoActivity.this).cropToBitmap(bitmap,Integer.parseInt(photo_data.getX_px()), Integer.parseInt(photo_data.getY_px()));

//        bitmap = scaleBitmap(bitmap, Integer.parseInt(photo_data.getX_px()), Integer.parseInt(photo_data.getY_px()));

//        Bitmap newBitmap=FaceCj.cutFace(bitmap,EditePhotoActivity.this);


        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = path;
        } else {
            return path;
        }
//        Log.d(TAG, "saveBitmap savePath : " + savePath);
        try {
            filePic = new File(savePath);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Log.e("errorMessage--->", bitmap.getHeight() + "");


            fos.flush();

        } catch (Exception e) {

        }

        return path;
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


    private void doNextAction(String path) {


        IdPhotoUtil.makeIdPhoto(photo_data.getSpecId(), new File(path), EditePhotoActivity.this, loadingDialog);


    }


    /**
     * 提供精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示需要精确到小数点以后几位
     * @return 两个参数的商
     */
    public  float div(int v1, int v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("保留的小数位数必须大于零");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        BigDecimal divide = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);

        // BigDecimal divide = b1.divide(b2);
        return Float.parseFloat(divide.toString());
    }




    @Override
    public void initViewObservable() {
        super.initViewObservable();
        LiveDataBus.get().with(LiveBusConfig.idPhotoModel, IdPhotoModel.DataBean.class).observe(this, new Observer<IdPhotoModel.DataBean>() {
            @Override
            public void onChanged(@Nullable IdPhotoModel.DataBean dataBean) {

                FileUtils.deleteFile(filePath);

                Bundle bundle = new Bundle();
                bundle.putSerializable("data", photo_data);
                bundle.putSerializable(MakeActivity.PIC, dataBean);
                startActivity(MakeActivity.class, bundle);

                finish();
                KLog.e("id--->", new Gson().toJson(dataBean));
            }
        });


        LiveDataBus.get().with(LiveBusConfig.backHome, String.class).observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                finish();
            }
        });

        LiveDataBus.get().with(LiveBusConfig.makeFail, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                errorDialog.show();
            }
        });


        binding.meibaiSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(div(seekBar.getProgress(),10,1)!=meiYanPro){
                    meiYanPro = div(seekBar.getProgress(),10,1) ;
                    isAllChange = true;

                    makeupBeautyUtils.progressSkinWhiteness(bitmapHashMap.get(postion), handlerMeiBai, meiYanPro);

                }
                KLog.e("meiYanPro--->",seekBar.getProgress()+"--->"+meiYanPro);
//                if(bitmapResult==null){
//                    makeupBeautyUtils.progressSkinWhiteness(bm,handlerMeiBai,meiYanPro);
//
//                }else {


//                }
            }
        });


        binding.mopiSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress() * 10!=moPiPro){
                    moPiPro = seekBar.getProgress() * 10;
                    isAllChange = true;

                    makeupBeautyUtils.progress(bitmapHashMap.get(postion), handlerMopi, moPiPro);

                }
                KLog.e("moPiPro--->",seekBar.getProgress()+"--->"+moPiPro);




            }
        });

        binding.smallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.featureRel.getVisibility() == View.VISIBLE) {
                    setAnim(false);
                }
            }
        });

        binding.smallView.setEnableOperate(false);
        binding.imageEditBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShouLian) {
                    if (!isHasChange) {
                        setAnim(false);

                        return;

                    }
                    if (backSureDialog == null) {
                        backSureDialog = new SureDialog(EditePhotoActivity.this, "", "是否应用修改?")
                                .addItemListener(new SureDialog.ItemListener() {
                                    @Override
                                    public void onClickSure() {
                                        updateSmallFace();

                                    }

                                    @Override
                                    public void onClickCanel() {
                                        binding.smallView.resetView();
                                        isHasChange = false;

                                        setAnim(false);

                                    }
                                });
                    }
                    backSureDialog.show();
                } else {
                    setAnim(false);

                }
            }
        });

        binding.imageEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShouLian) {
                    if (!isHasChange) {
                        setAnim(false);
                        return;

                    }
                    updateSmallFace();

                } else {
                    setAnim(false);

                }

            }
        });

        binding.resetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.smallView.resetView();
            }
        });


        binding.typeRb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                setAnim(true);

                switch (checkedId) {

                    case R.id.meibai_rb:
                        isShouLian = false;
                        setCheckType();
                        isMeibai = true;
                        binding.smallView.setEnableOperate(false);

                        binding.meibaiLl.setVisibility(View.VISIBLE);
                        binding.mopiLl.setVisibility(View.GONE);
                        binding.shoulianLl.setVisibility(View.GONE);
                        break;
                    case R.id.mopi_rb:
                        isMeibai = false;
                        isShouLian = false;


                        binding.smallView.setEnableOperate(false);

                        binding.meibaiLl.setVisibility(View.GONE);
                        binding.mopiLl.setVisibility(View.VISIBLE);
                        binding.shoulianLl.setVisibility(View.GONE);
                        break;
                    case R.id.shoulian_rb:
                        isShouLian = true;

                        binding.smallView.setEnableOperate(true);
                        setCheckType();
                        binding.meibaiLl.setVisibility(View.GONE);
                        binding.mopiLl.setVisibility(View.GONE);
                        binding.shoulianLl.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void setCheckType() {
        if (isMeibai) {

//            postion += 1;

            makeupBeautyUtils.progressSkinWhiteness(changeHashMap.get(changePostion), handler, meiYanPro);


        } else {

            makeupBeautyUtils.progress(changeHashMap.get(changePostion), handler, moPiPro);


        }
        isMeibai = !isMeibai;
    }

    @Override
    public void setLoginState(boolean loginState) {
        super.setLoginState(loginState);
        if (!loginState) {
            startActivity(LoginActivity.class);
        }
    }

    private void setAnim(boolean isShow) {
        if (isShow) {
            binding.featureRel.setVisibility(View.VISIBLE);
            binding.featureRel.setAnimation(AnimationUtil.moveToViewLocation());
        } else {
            binding.typeRb.clearCheck();
            binding.smallView.setEnableOperate(false);
            binding.featureRel.setVisibility(View.GONE);
            binding.featureRel.setAnimation(AnimationUtil.moveToViewBottom());
        }


    }

    private Disposable smallDisposable;

    private void updateSmallFace() {

        loadingDialog.show();
        smallDisposable = Single
                .create(new SingleOnSubscribe<Bitmap>() {
                    @Override
                    public void subscribe(SingleEmitter<Bitmap> e) {
                        e.onSuccess(binding.smallView.getNoChnageBitMap());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() {
                    }
                })
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap musicInfos) {
                        loadingDialog.dismiss();
                        setAnim(false);

//                        changePostion += 1;
//                        postion += 1;
                        isHasChange = false;
                        changeHashMap.put(changePostion, musicInfos);
                        bitmapHashMap.put(postion, musicInfos);
                        binding.smallView.setNoChnageBitMap(musicInfos);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        loadingDialog.dismiss();
                        ToastUtils.show("应用失败");
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        if (smallDisposable != null && !smallDisposable.isDisposed()) {
            smallDisposable.dispose();
        }


        if (makeupBeautyUtils != null) {
            makeupBeautyUtils.destroy();

        }
        if (bitmapHashMap != null) {
            if (bitmapHashMap.get(postion) != null && !bitmapHashMap.get(postion).isRecycled()) {
                bitmapHashMap.get(postion).recycle();
            }
        }

        if (changeHashMap != null) {
            if (changeHashMap.get(changePostion) != null && !changeHashMap.get(changePostion).isRecycled()) {
                changeHashMap.get(changePostion).recycle();
            }
        }


        if (bm != null && bm.isRecycled()) {
            bm.recycle();
            bm = null;
        }

    }


    private Bitmap foreground;


    private MLImageSegmentationAnalyzer analyzer;


    private void createImageTransactor() {



        MLImageSegmentationSetting setting = new MLImageSegmentationSetting.Factory()
                .setAnalyzerType(MLImageSegmentationSetting.BODY_SEG)
//                .setScene(MLImageSegmentationScene.FOREGROUND_ONLY)

                .setExact(true)
                .create();
        analyzer = MLAnalyzerFactory.getInstance().getImageSegmentationAnalyzer(setting);

//        if (isChosen(originBitmap)) {
//        MLFrame mlFrame = new MLFrame.Creator().setBitmap(binding.smallView.getBitmap()).create();

        MLFrame mlFrame = new MLFrame.Creator().setBitmap(setCrop(binding.smallView.getBitmap(), by, hederHeight)).create();
        Task<MLImageSegmentation> task = analyzer.asyncAnalyseFrame(mlFrame);
        task.addOnSuccessListener(new OnSuccessListener<MLImageSegmentation>() {
            @Override
            public void onSuccess(MLImageSegmentation mlImageSegmentationResults) {
                // 转换成功
                if (mlImageSegmentationResults != null) {
                    foreground = mlImageSegmentationResults.getForeground();

                    savaPic(FileUtils.UPDATE_PHOTO_FILE_PATH + System.currentTimeMillis() + ".jpg");
//                    imageView.setImageBitmap(foreground);
////                        processedImage = ((BitmapDrawable) ((ImageView)  preview).getDrawable()).getBitmap();
//                    changeBackground();
                } else {
//                        displayFailure();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e("error-->", e.getMessage());
                // 转换失败
//                    displayFailure();
                return;
            }
        });
//        } else {
//            Toast.makeText(getApplicationContext(), "请先选择图片", Toast.LENGTH_SHORT).show();
//            return;
//        }
    }




    private Disposable disposableFace;

    private void getFace() {
        loadingDialog.show();
        disposableFace = Single
                .create(new SingleOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(SingleEmitter<Boolean> e) {
                        e.onSuccess(getHasFace());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() {
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean musicInfos) {
//                        loadingDialog.dismiss();

                        if (musicInfos) {
                            createImageTransactor();
                        } else {
                            errorDialog.show();
                            loadingDialog.dismiss();
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        loadingDialog.dismiss();
                    }
                });


    }

    private int picHeight;
    private int picWith;

    private float by;
    private float hederHeight;


    private boolean getHasFace() {
        picHeight = binding.smallView.getBitmap().getHeight();
        picWith = binding.smallView.getBitmap().getWidth();
        KLog.e("errorMessage--->", picHeight + "--->" + picWith);

        MLFrame frame = MLFrame.fromBitmap(binding.smallView.getBitmap());
//        MLFaceAnalyzerSetting setting = new MLFaceAnalyzerSetting.Factory()
//                // 设置是否检测人脸关键点。
//                .setKeyPointType(MLFaceAnalyzerSetting.TYPE_KEYPOINTS)
//                // 设置是否检测人脸特征点。
//                .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURES)
//                // 设置是否检测人脸轮廓点。
//                .setShapeType(MLFaceAnalyzerSetting.TYPE_SHAPES)
//                // 设置是否开启人脸追踪。
//                .setTracingAllowed(true)
//                // 设置检测器速度/精度模式。
//                .setPerformanceType(MLFaceAnalyzerSetting.TYPE_SPEED)
//                .create();
//        MLFaceAnalyzer analyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer(setting);

        final MLFaceAnalyzer analyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer();

        Task<List<MLFace>> task = analyzer.asyncAnalyseFrame(frame);

        task.addOnSuccessListener(new OnSuccessListener<List<MLFace>>() {
            @Override
            public void onSuccess(List<MLFace> faces) {


//x                Log.e("errorMessage--->", "onSuccess");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 检测失败。
                // Recognition failure.
                Log.e("errorMessage--->", e.getMessage() + "-->");

                try {
                    MLException mlException = (MLException) e;
                    // 获取错误码，开发者可以对错误码进行处理，根据错误码进行差异化的页面提示。错误码信息可参见：机器学习服务错误码。
                    int errorCode = mlException.getErrCode();
                    // 获取报错信息，开发者可以结合错误码，快速定位问题。
                    String errorMessage = mlException.getMessage();
                    Log.e("errorMessage--->", errorMessage);
                } catch (Exception error) {
                    // 转换错误处理。
                    Log.e("errorMessage--->", error.getMessage());

                }
            }
        });


        SparseArray<MLFace> faces = analyzer.analyseFrame(frame);

        if (faces.size() > 0) {
//            loadingDialog.dismiss();
            MLFace mlFace = faces.get(0);

            mlFace.getAllPoints().get(0).getX();
            List<MLPosition> po = mlFace.getAllPoints();
            KLog.e("errorMessage--->", po.size());

            by = po.get(0).getY();
            hederHeight = mlFace.getHeight();
            KLog.e("errorMessage--->", po.get(0).getX() + "--->" + po.get(0).getY() + "--->" + mlFace.getHeight());

            return true;
        }


        return false;
    }


    private Bitmap setCrop(Bitmap bitmap, float by, float heder) {

        float ty = by - heder;


        /**
         * 身体高度
         */
        int bodyHeight = (int) (picHeight - by);


        /**
         * 顶部高度
         */
        int topHeigh = (int) ty;


        /**
         * 头的高度
         */
        float headerHeight = heder;


        if (ty < 150) {
            topHeigh = 0;
            KLog.e("errorMessage--->", "true");

        } else {
            topHeigh -= 150;
        }


        picHeight -= topHeigh;

        int cut = 0;



        if (bodyHeight > 220) {
            cut = bodyHeight - 220;
        }
        picHeight -= cut;

        /**
         * 人物高度
         */
        int peopleHeght = (int) (headerHeight + bodyHeight);
        Bitmap bitmap1 = null;
        KLog.e("errorMessage--->", "--->" + peopleHeght + "--->" + headerHeight + "--->" + ty + "--->ca");
        try {
            bitmap1 = Bitmap.createBitmap(bitmap, 0, topHeigh, picWith, picHeight, null, false);

        } catch (Exception e) {

        }

        if(bitmap1==null){
            return bitmap;
        }


        return bitmap1;


    }
}
