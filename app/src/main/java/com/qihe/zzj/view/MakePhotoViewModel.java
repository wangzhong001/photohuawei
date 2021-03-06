package com.qihe.zzj.view;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.qihe.zzj.BR;
import com.qihe.zzj.BaseApplication;
import com.qihe.zzj.Contans;
import com.qihe.zzj.R;
import com.qihe.zzj.bean.IdPhotoBean;
import com.qihe.zzj.bean.KefuModel;
import com.qihe.zzj.util.FileUtils;
import com.qihe.zzj.util.PhotoDaoUtils;
import com.qihe.zzj.util.TimeUtil;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by lipei on 2020/6/29.
 */

public class MakePhotoViewModel extends BaseViewModel {

    private PhotoDaoUtils photoDaoUtils;



    public MakePhotoViewModel(@NonNull Application application) {
        super(application);
        if(photoDaoUtils==null){
            photoDaoUtils=new PhotoDaoUtils(this.getApplication());
        }
    }

    public ObservableField<String> picUrl=new ObservableField<>("");

    public ObservableField<String> withString=new ObservableField<>("");

    public ObservableField<String> heightString=new ObservableField<>("");

    public ObservableField<String> nameString=new ObservableField<>("");

    public ObservableField<String> pxString=new ObservableField<>("");

    public ObservableField<String> mmString=new ObservableField<>("");

    public ObservableField<String> timeString=new ObservableField<>("");



    public BindingRecyclerViewAdapter<PicItemViewModel> adapter=new BindingRecyclerViewAdapter<>();

    public ObservableArrayList<PicItemViewModel> list=new ObservableArrayList<>();

    public ItemBinding<PicItemViewModel> itemBinding = ItemBinding.of(BR.baseViewModel, R.layout.layout_item_my_recycler);




    private Disposable disposable;


    public void getPic(){

        final long nowTime=System.currentTimeMillis();


        disposable = Single
                .create(new SingleOnSubscribe<List<IdPhotoBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<IdPhotoBean>> e) {
                        e.onSuccess(photoDaoUtils.queryAllMeizi());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() {
//                        dialog.dismiss();
                    }
                })
                .subscribe(new Consumer<List<IdPhotoBean>>() {
                    @Override
                    public void accept(List<IdPhotoBean> musicInfos) {
                        if(musicInfos==null){
                            isEmpty.set(true);

                            return;
                        }

                        if(musicInfos.size()==0){
                            isEmpty.set(true);

                            return;
                        }

                        if(list!=null){
                            if(list.size()>0){
                                list.clear();
                            }
                        }
                        KLog.e("size--->",musicInfos.size()+"--???"+list.size());

                        for (int i = 0; i < musicInfos.size(); i++) {
                            KLog.e("time--->",musicInfos.get(i).getCreateTime());
                            IdPhotoBean idPhotoBean=musicInfos.get(i);
                            long effectiveTime=idPhotoBean.getEffectiveTime();
                            if(nowTime>effectiveTime){
                                if(photoDaoUtils!=null){
                                    photoDaoUtils.deleteMeizi(idPhotoBean);
                                }
                            }else {
                                list.add(new PicItemViewModel(MakePhotoViewModel.this,idPhotoBean,i));

                            }
                        }
                        if(list.size() > 0){
                            Collections.reverse(list);
                        }
                        if(list.size()==0){
                            isEmpty.set(true);
                        }else {
                            isEmpty.set(false);

                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
//                        dialog.dismiss();
//                        isEmpty.set(true);
//                        ToastUtils.show("??????????????????????????????");
                    }
                });


    }


    public void insertPic(final IdPhotoBean idPhotoBean){



        disposable = Single
                .create(new SingleOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(SingleEmitter<Boolean> e) {
                        e.onSuccess(photoDaoUtils.insertPic(idPhotoBean));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() {
//                        dialog.dismiss();
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean musicInfos) {
                        int number= (int) SharedPreferencesUtil.getParam(Contans.orderNumber,0);
                        number+=1;
                        SharedPreferencesUtil.setParam(Contans.orderNumber,number);
                        KLog.e("musicInfos-->",musicInfos);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        KLog.e("musicInfos-->",throwable.getMessage());

//                        dialog.dismiss();
//                        isEmpty.set(true);
//                        ToastUtils.show("??????????????????????????????");
                    }
                });


    }

    public ObservableField<String> picPath=new ObservableField<>("");

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        int length = baos.toByteArray().length / 1024;

        if (length>5000){
            //??????baos?????????baos
            baos.reset();
            //???????????????????????????100????????????????????????????????????????????????baos???
            image.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        }else if (length>4000){
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        }else if (length>3000){
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }else if (length>2000){
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        }
        //?????????????????????????????????????????????1M,??????????????????
        while (baos.toByteArray().length / 1024>100) {
            KLog.e("length--->",baos.toByteArray().length);

            //??????baos?????????baos
            baos.reset();
            //????????????options%?????????????????????????????????baos???
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //???????????????10
            options -= 10;
        }
        //?????????????????????baos?????????ByteArrayInputStream???
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //???ByteArrayInputStream??????????????????
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    public void saveic(final Context context, final String url, final String name, final boolean isShow){
        disposable = Single
                .create(new SingleOnSubscribe<File>() {
                    @Override
                    public void subscribe(SingleEmitter<File> e) {
                        try {
                            e.onSuccess(Glide.with(context)
                                    .load(url)
                                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get());
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        } catch (ExecutionException e1) {
                            e1.printStackTrace();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() {
//                        dialog.dismiss();
                    }
                })
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                        Log.e("aaa", "File...1..." + file);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P ) {
                            boolean b = SavePictureFile(BaseApplication.getContext(), file);
                            Log.e("aaa", "File...2..." + b);
                        }

                        File pictureFolder = new File(FileUtils.UPDATE_PHOTO_FILE_PATH);
                        //????????????????????????????????????????????????
                        File appDir;
                        if (isShow){
                            appDir = new File(FileUtils.DOWN_PHOTO_FILE_PATH);
                        }else {
                            appDir = new File(FileUtils.UPDATE_PHOTO_FILE_PATH);
                        }
//                        File appDir = new File(FileUtils.DOWN_PHOTO_FILE_PATH);
                        if (!appDir.exists()) {
                            appDir.mkdirs();
                        }
                        String fileName1 = name + TimeUtil.getTimeByDate(Calendar.getInstance().getTime());
                        String fileName = fileName1 + ".jpg";
                        BaseApplication.fileName2 = fileName1;

                        File destFile = new File(appDir, fileName);
                        //???gilde???????????????????????????????????????????????????
                        copy(file, destFile);
                        picPath.set(destFile.getPath());
                        // ????????????????????????
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(destFile.getPath()))));
                        BaseApplication.mPath = destFile.getPath();
                        Log.e("aaa", "?????????" + BaseApplication.mPath);
                        Log.e("aaa", "?????????" + BaseApplication.fileName2);
//                        if(isShow){
//                            ToastUtils.show("?????????????????????????????????");
//                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                });
    }

    public static boolean SavePictureFile(Context context, File file) {
        if (file == null) {
            return false;
        }
        Uri uri = insertFileIntoMediaStore(context, file, true);
        return SaveFile(context, file, uri);
    }

    public static boolean SaveVideoFile(Context context, File file) {
        if (file == null) {
            return false;
        }
        Uri uri = insertFileIntoMediaStore(context, file, false);
        return SaveFile(context, file, uri);
    }

    private static boolean SaveFile(Context context, File file, Uri uri) {
        if (uri == null) {
            Log.e("aaa", "url is null");
            return false;
        }
        Log.e("aaa", "file.getName: " + file.getName());
        BaseApplication.fileNewName = file.getName();
        Log.e("aaa", "file.getPath: " + file.getPath());
        ContentResolver contentResolver = context.getContentResolver();
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "w");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (parcelFileDescriptor == null) {
            Log.e("aaa", "parcelFileDescriptor is null");
            return false;
        }
        FileOutputStream outputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.e("aaa", e.toString());
            try {
                outputStream.close();
            } catch (IOException ex) {
                Log.e("aaa", ex.toString());
            }
            return false;
        }
        try {
            copy(inputStream, outputStream);
        } catch (IOException e) {
            Log.e("aaa", e.toString());
            return false;
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                Log.e("aaa", e.toString());
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e("aaa", e.toString());
            }
        }
        return true;
    }

    //??????????????????????????????????????????????????????
    private static void copy(InputStream ist, OutputStream ost) throws IOException {
        byte[] buffer = new byte[4096];
        int byteCount;
        while ((byteCount = ist.read(buffer)) != -1) {
            ost.write(buffer, 0, byteCount);
        }
        ost.flush();
    }

    //????????????????????????URI
    private static Uri insertFileIntoMediaStore(Context context, File file, boolean isPicture) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, file.getName());
        contentValues.put(MediaStore.Video.Media.MIME_TYPE, isPicture ? "image/jpeg" : "video/mp4");
        if (Build.VERSION.SDK_INT > 28) {
            contentValues.put(MediaStore.Video.Media.DATE_TAKEN, file.lastModified());
        }
        Uri uri = null;
        try {
            uri = context.getContentResolver().insert(
                    (isPicture ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                    , contentValues
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     * @param srcPath   ?????????????????????
     * @param savePath  ?????????????????????
     * @param aimSize   ?????????????????????????????????
     */
    public void compressImageByPath(String srcPath, String savePath, int aimSize) {
        // ??????savePath??????????????????????????????
        Bitmap imgBitmap = BitmapFactory.decodeFile(srcPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int percent = 100;// ???????????????????????????????????????
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, percent, baos);
        int currentSize = baos.toByteArray().length / 1024;
        while (currentSize > aimSize) {// ??????????????????????????????????????????????????????????????????????????????
            baos.reset();// ??????baos????????????baos
            //???????????????????????????????????????????????????JPEG?????????PNG??????????????????
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, percent, baos);
            currentSize = baos.toByteArray().length / 1024;
            percent -= 5;
            if (percent <= 0) {
                break;
            }
        }
        try {//????????????????????????
            FileOutputStream fos = new FileOutputStream(savePath);
            baos.writeTo(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {//????????????????????????
                baos.flush();
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!imgBitmap.isRecycled()) {
            imgBitmap.recycle();//???????????????????????????
            System.gc();//????????????????????????
        }
    }


    /**
     * ????????????
     *
     * @param source ????????????
     * @param target ????????????
     */
    public void copy(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onDestory() {
        super.onDestory();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    /**
     * ????????????
     * @param idPhotoBean
     */
    public MutableLiveData<IdPhotoBean> toDetailLiveData=new MutableLiveData<>();
    public void toDetai(PicItemViewModel picItemViewModel){
        picItemViewModelObservableField.set(picItemViewModel);
        toDetailLiveData.postValue(picItemViewModel.idPhotoBean);
    }


    public MutableLiveData<PicItemViewModel> deleteLiveData=new MutableLiveData<>();
    public void deletePic(PicItemViewModel picItemViewModel){
        deleteLiveData.postValue(picItemViewModel);
    }

    /**
     * ????????????
     * @param picItemViewModel
     */
    public void canelOrder(PicItemViewModel picItemViewModel,boolean isNeedSet){
        if(list.size()>0){
            if(picItemViewModel==null){
                return;
            }
//            if(!picItemViewModel.isPay.get()){
//                int number= (int) SharedPreferencesUtil.getParam(Contans.orderNumber,0);
//                if(number>0){
//                    number-=1;
//                    SharedPreferencesUtil.setParam(Contans.orderNumber,number);
//                }
//            }

            if(isNeedSet){
                LiveDataBus.get().with(LiveBusConfig.deleteOrder,Integer.class).postValue(picItemViewModel.postion);

            }
            list.remove(picItemViewModel);
            if(photoDaoUtils!=null){
                photoDaoUtils.deleteMeizi(picItemViewModel.idPhotoBean);

            }
            if(list.size()==0){
                isEmpty.set(true);
            }
        }
    }


    /**
     * ?????????
     * @param picItemViewModel
     */
    public ObservableField<PicItemViewModel> picItemViewModelObservableField=new ObservableField<>();
    public MutableLiveData<PicItemViewModel> payLiveData=new MutableLiveData<>();

    public void payOrder(PicItemViewModel picItemViewModel){
        picItemViewModelObservableField.set(picItemViewModel);
        payLiveData.postValue(picItemViewModel);

    }

    public MutableLiveData<MakePhotoViewModel> paySuccessLiveData=new MutableLiveData<>();

    public void setPaySuccess(){
        if(picItemViewModelObservableField!=null){
            PicItemViewModel picItemViewModel=picItemViewModelObservableField.get();
            if(picItemViewModel!=null){
                IdPhotoBean idPhotoBean=picItemViewModel.idPhotoBean;
                idPhotoBean.setIsPay(true);
                picItemViewModel.isPay.set(true);
                if(photoDaoUtils!=null){
                    photoDaoUtils.updateMeizi(idPhotoBean);

                }
                paySuccessLiveData.postValue(MakePhotoViewModel.this);
            }

        }

    }


    public ObservableBoolean isPay=new ObservableBoolean();

    public ObservableBoolean isEmpty=new ObservableBoolean(true);




    public BindingRecyclerViewAdapter<KufuItemViewModel> kefuAdapter=new BindingRecyclerViewAdapter<>();

    public ObservableArrayList<KufuItemViewModel> kefuList=new ObservableArrayList<>();

    public ItemBinding<KufuItemViewModel> kefuItemBinding = ItemBinding.of(BR.baseViewModel, R.layout.kefu_item);

    public void getKefuData(){
        String titles[]=getApplication().getResources().getStringArray(R.array.kefu_title);
        String contents[]=getApplication().getResources().getStringArray(R.array.kefu_content);

        for (int i=0;i<titles.length;i++){
            KefuModel kefuModel=new KefuModel();
            kefuModel.setTitle(titles[i]);
            kefuModel.setContent(contents[i]);
            kefuList.add(new KufuItemViewModel(MakePhotoViewModel.this,kefuModel));
        }
    }


    public MutableLiveData<KefuModel> kefuLiveData=new MutableLiveData<>();
    public void toKefuDetail(KefuModel kefuModel){
        kefuLiveData.postValue(kefuModel);
    }

}
