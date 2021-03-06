package com.qihe.zzj.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseReq;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseResult;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.entity.PurchaseIntentReq;
import com.huawei.hms.iap.entity.PurchaseIntentResult;
import com.huawei.hms.iap.entity.PurchaseIntentWithPriceReq;
import com.huawei.hms.iap.entity.PurchaseResultInfo;
import com.huawei.hms.support.api.client.Status;
import com.qihe.zzj.BR;
import com.qihe.zzj.BaseApplication;
import com.qihe.zzj.R;
import com.qihe.zzj.adapter.PicAdapter;
import com.qihe.zzj.bean.IsPay;
import com.qihe.zzj.bean.MyRecycBean;
import com.qihe.zzj.databinding.ActivityOrderDetailBinding;
import com.qihe.zzj.util.IAPSupport;
import com.qihe.zzj.util.PhotoDaoUtils;
import com.qihe.zzj.util.TimeUtil;
import com.qihe.zzj.view.MakePhotoViewModel;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xinqidian.adcommon.view.LoadingDialog;
import com.xinqidian.adcommon.view.SureDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import gdut.bsx.share2.FileUtil;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

/**
 * ????????????
 */
public class OrderDetailActivity extends BaseActivity<ActivityOrderDetailBinding, MakePhotoViewModel> {

    private MyRecycBean order_data;

    private PicAdapter picAdapter;
    private List<String> picList = new ArrayList<>();

    private SureDialog sureDialog;

    private SureDialog goodCommentDialog;

    private boolean isFree;

    private boolean isHasToast;

    private LoadingDialog loadingDialog;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_order_detail;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
    }

    public String saveBitmap(String path) {
//       Bitmap newBitmap= CImageUtils.instance(EditePhotoActivity.this).cropToBitmap(bitmap,Integer.parseInt(photo_data.getX_px()), Integer.parseInt(photo_data.getY_px()));
//        bitmap = scaleBitmap(bitmap, Integer.parseInt(photo_data.getX_px()), Integer.parseInt(photo_data.getY_px()));
//        Bitmap newBitmap=FaceCj.cutFace(bitmap,EditePhotoActivity.this);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            Log.e("aaa","????????????...28" + BaseApplication.mPath);
            Bitmap bitmap = BitmapFactory.decodeFile(BaseApplication.mPath);
            if (bitmap != null){
                //????????????1???
                bitmap = addBitmap(bitmap,bitmap);
                //????????????2???
                if (order_data.getName().contains("??????")){
                    bitmap = add2Bitmap(bitmap,bitmap);
                }else {
                    bitmap = add2Bitmap(bitmap,bitmap);
                    bitmap = add2Bitmap(bitmap,bitmap);
                }
            }
            String savePath;
            File filePic;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                savePath = path;
            } else {
                return path;
            }
            Log.e("aaa", "savePath1 : " + savePath);
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
        }else{
            Log.e("aaa","????????????...28????????????" + BaseApplication.mPath);
            Uri imageContentUri = getImageContentUri(OrderDetailActivity.this, BaseApplication.mPath);
            Bitmap bitmap = getBitmapFromUri(OrderDetailActivity.this, imageContentUri);
            if (bitmap != null){
                //????????????1???
                bitmap = addBitmap(bitmap,bitmap);
                //????????????2???
                if (order_data.getName().contains("??????")){
                    bitmap = add2Bitmap(bitmap,bitmap);
                }else {
                    bitmap = add2Bitmap(bitmap,bitmap);
                    bitmap = add2Bitmap(bitmap,bitmap);
                }
            }
            String savePath;
            File filePic;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                savePath = path;
            } else {
                return path;
            }
            Log.e("aaa", "savePath2 : " + savePath);
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
            /*ContentResolver contentResolver = OrderDetailActivity.this.getContentResolver();
//            ParcelFileDescriptor fd = null;
            try {
                ParcelFileDescriptor fd = contentResolver.openFileDescriptor(Uri.parse(BaseApplication.mPath), "r");
                if (fd != null) {
                    Bitmap bitmap2 = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor());
                    fd.close();

                    if (bitmap2 != null){
                        //????????????1???
                        bitmap2 = addBitmap(bitmap2,bitmap2);
                        //????????????2???
                        if (BaseApplication.mPath.contains("??????")){
                            bitmap2 = add2Bitmap(bitmap2,bitmap2);
                        }else {
                            bitmap2 = add2Bitmap(bitmap2,bitmap2);
                            bitmap2 = add2Bitmap(bitmap2,bitmap2);
                        }
                    }
                    String savePath;
                    File filePic;
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        savePath = path;
                    } else {
                        return path;
                    }
                    Log.e("aaa", "saveBitmap savePath : " + savePath);
                    try {
                        filePic = new File(savePath);
                        if (!filePic.exists()) {
                            filePic.getParentFile().mkdirs();
                            filePic.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(filePic);
                        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        Log.e("aaa", bitmap2.getHeight() + "");
                        fos.flush();
                    } catch (Exception e) {

                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }

        Log.e("aaa","..." + path);
        /*Log.e("aaa","????????????..." + BaseApplication.mPath);
        Bitmap bitmap = BitmapFactory.decodeFile(BaseApplication.mPath);
        if (bitmap != null){
            //????????????1???
            bitmap = addBitmap(bitmap,bitmap);
            //????????????2???
            if (BaseApplication.mPath.contains("??????")){
                bitmap = add2Bitmap(bitmap,bitmap);
            }else {
                bitmap = add2Bitmap(bitmap,bitmap);
                bitmap = add2Bitmap(bitmap,bitmap);
            }
        }

        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = path;
        } else {
            return path;
        }
        Log.e("aaa", "saveBitmap savePath : " + savePath);
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

        }*/
        return path;
    }

    public String saveBitmap11(String path) {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            Log.e("aaa","????????????...28" + BaseApplication.mNewPath);
            Bitmap bitmap = BitmapFactory.decodeFile(BaseApplication.mNewPath);
            if (bitmap != null){
                //????????????1???
                bitmap = addBitmap(bitmap,bitmap);
                //????????????2???
                if (order_data.getName().contains("??????")){
                    bitmap = add2Bitmap(bitmap,bitmap);
                }else {
                    bitmap = add2Bitmap(bitmap,bitmap);
                    bitmap = add2Bitmap(bitmap,bitmap);
                }
            }
            String savePath;
            File filePic;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                savePath = path;
            } else {
                return path;
            }
            Log.e("aaa", "savePath1 : " + savePath);
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
        }else{
            Log.e("aaa","????????????...28????????????" + BaseApplication.mNewPath);
            Uri imageContentUri = getImageContentUri(OrderDetailActivity.this, BaseApplication.mNewPath);
            Bitmap bitmap = getBitmapFromUri(OrderDetailActivity.this, imageContentUri);
            if (bitmap != null){
                //????????????1???
                bitmap = addBitmap(bitmap,bitmap);
                //????????????2???
                if (order_data.getName().contains("??????")){
                    bitmap = add2Bitmap(bitmap,bitmap);
                }else {
                    bitmap = add2Bitmap(bitmap,bitmap);
                    bitmap = add2Bitmap(bitmap,bitmap);
                }
            }
            String savePath;
            File filePic;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                savePath = path;
            } else {
                return path;
            }
            Log.e("aaa", "savePath2 : " + savePath);
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
        }
        Log.e("aaa","..." + path);
        return path;
    }

    public String saveBitmap22(String path) {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            Log.e("aaa","????????????...28" + BaseApplication.mNewPath);
            Bitmap bitmap = BitmapFactory.decodeFile(BaseApplication.mNewPath);
            String savePath;
            File filePic;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                savePath = path;
            } else {
                return path;
            }
            Log.e("aaa", "savePath1 : " + savePath);
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
        }else{
            Log.e("aaa","????????????...28????????????" + BaseApplication.mNewPath);
            Uri imageContentUri = getImageContentUri(OrderDetailActivity.this, BaseApplication.mNewPath);
            Bitmap bitmap = getBitmapFromUri(OrderDetailActivity.this, imageContentUri);
            String savePath;
            File filePic;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                savePath = path;
            } else {
                return path;
            }
            Log.e("aaa", "savePath2 : " + savePath);
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
        }
        Log.e("aaa","..." + path);
        return path;
    }

    public Uri getImageContentUri(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { path }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            // ????????????????????????????????????????????????????????????????????????
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    // ??????uri????????????
    public Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap shotRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                        holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            Drawable lBackground = view.getBackground();
            if (lBackground instanceof ColorDrawable) {
                ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                int lColor = lColorDrawable.getColor();
                bigCanvas.drawColor(lColor);
            }

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
        }
        return bigBitmap;
    }

    //????????????
    private Bitmap add2Bitmap(Bitmap first, Bitmap second) {
        int width = first.getWidth() + second.getWidth() + 10;
        int height = Math.max(first.getHeight(), second.getHeight());
        Log.e("aaa", "2??????" + width + "?????????" + height);
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(OrderDetailActivity.this.getResources().getColor(R.color.tab_bg_color));
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, first.getWidth() + 10, 0, null);
        return result;
    }

    //????????????
    private Bitmap addBitmap(Bitmap first, Bitmap second) {
        int width = Math.max(first.getWidth(), second.getWidth());
        int height = first.getHeight() + second.getHeight() + 10;
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, first.getHeight() + 10, null);
        return result;
    }

    private PhotoDaoUtils photoDaoUtils;

    @Override
    public void initData() {
        super.initData();
        photoDaoUtils = new PhotoDaoUtils(this);
        loadingDialog = new LoadingDialog(this, true);
        if (getIntent() != null) {
            order_data = (MyRecycBean) getIntent().getSerializableExtra("data");
            viewModel.picUrl.set(order_data.getUrl());
            viewModel.timeString.set(TimeUtil.timeStamp2Date(order_data.getTime(), TimeUtil.TIME));
            viewModel.isPay.set(order_data.isPay());
        }
        ImmersionBar.with(this)
                .reset()
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .fitsSystemWindows(false)
                .init();
        order_data.setOrder_num("20061545465456456465a");

        binding.ivPayStatus.setImageDrawable(order_data.isPay() ? getResources().getDrawable(R.drawable.yifukuan_icon)
                : getResources().getDrawable(R.drawable.daifukuan_icon));

        binding.tvPayStatus.setText(order_data.isPay() ? getResources().getString(R.string.have_pay)
                : getResources().getString(R.string.dai_pay));

//        binding.llBottom.setVisibility(order_data.isPay() ? View.GONE : View.VISIBLE);

        binding.tvName.setText(order_data.getName());
        binding.tvXPx.setText(order_data.getX_px());
        binding.tvMm.setText(order_data.getMm());
//        binding.tvDpi.setText(order_data.getDpi());
        binding.tvOrderNum.setText(order_data.getOrder_num());
        binding.tvMoney.setText("???" + order_data.getMoney());

        if (order_data.isPay()) {
            Log.e("aaa", "BaseApplication.mPath...1");
            viewModel.saveic(OrderDetailActivity.this, order_data.getUrl(), order_data.getName(), true);
        }

        binding.canelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????
                if (sureDialog == null) {
                    sureDialog = new SureDialog(OrderDetailActivity.this, "", "?????????????????????")
                            .addItemListener(new SureDialog.ItemListener() {
                                @Override
                                public void onClickSure() {
                                    LiveDataBus.get().with(LiveBusConfig.canelOrder, String.class).postValue(LiveBusConfig.canelOrder);
                                    finish();
                                }

                                @Override
                                public void onClickCanel() {

                                }
                            });
                }
                sureDialog.show();
            }
        });
        binding.tvDownOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aaa","tvDownOne...1");
                List<String> systemPhotoList = getSystemPhotoList(OrderDetailActivity.this);
                for (int i = 0; i < systemPhotoList.size(); i++){
                    String s = systemPhotoList.get(i);
                    if (s.contains(BaseApplication.fileNewName)){
                        BaseApplication.mNewPath = s;
                        Log.e("aaa", "path1..." + BaseApplication.mNewPath);
                        saveBitmap22(Environment.getExternalStorageDirectory().getPath() + "/Pictures/"+ order_data.getName() + ".jpg");
//                        saveBitmap22(Environment.getExternalStorageDirectory().getPath() + "/DCIM/"+ order_data.getName() + ".jpg");
                    }
                }
                viewModel.saveic(OrderDetailActivity.this, order_data.getUrl(), order_data.getName(), true);
                ToastUtils.show("??????????????????");
            }
        });
        binding.tvDownMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownDialog();
            }
        });
        binding.shareIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.picPath.get() != null) {
                    if (viewModel.picPath.get().equals("")) {
                        ToastUtils.show("???????????????????????????????????????");
                    } else {
                        new Share2.Builder(OrderDetailActivity.this)
                                .setContentType(ShareContentType.FILE)
                                .setShareFileUri(FileUtil.getFileUri(OrderDetailActivity.this, ShareContentType.FILE, new File(viewModel.picPath.get())))
                                .build()
                                .shareBySystem();
                    }
//                    AndroidShareUtils.goToMarket();
                } else {
                    ToastUtils.show("???????????????????????????????????????");
                }
            }
        });
        binding.payTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharedPreferencesUtil.isLogin()) {
                    startActivity(LoginActivity.class);
                    return;
                }
                if (SharedPreferencesUtil.isVip()) {
                    //vip
                    viewModel.saveic(OrderDetailActivity.this, order_data.getUrl(), order_data.getName(), true);
                    LiveDataBus.get().with(LiveBusConfig.alipaySuccess, Boolean.class).postValue(false);
                    finish();
                } else {
                    //??????vip,??????????????????????????????
                    UserUtil.getUserFreeNumber(loadingDialog, new UserUtil.CallBack() {
                        @Override
                        public void onSuccess() {
                            //???????????????,????????????????????????
                            UserUtil.updateFreeNumber(0, new UserUtil.CallBack() {
                                @Override
                                public void onSuccess() {
                                    viewModel.saveic(OrderDetailActivity.this, order_data.getUrl(), order_data.getName(), true);
                                    LiveDataBus.get().with(LiveBusConfig.alipaySuccess, Boolean.class).postValue(false);
                                    finish();
                                }

                                @Override
                                public void onFail() {
                                    //??????????????????????????????????????????
                                    getBuyIntentWithPrice(OrderDetailActivity.this);
                                }

                                @Override
                                public void loginFial() {
                                    startActivity(LoginActivity.class);
                                }
                            });
                        }

                        @Override
                        public void onFail() {
                            //?????????????????????????????????????????????
                            getBuyIntentWithPrice(OrderDetailActivity.this);
                        }

                        @Override
                        public void loginFial() {
                            startActivity(LoginActivity.class);
                        }
                    });
                }
            }
        });

        binding.tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????
                //???????????????????????????
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // ?????????????????????ClipData
                ClipData mClipData = ClipData.newPlainText(null, binding.tvOrderNum.getText());
                // ???ClipData?????????????????????????????????
                cm.setPrimaryClip(mClipData);
                ToastUtils.show("????????????");
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String name = order_data.getName();
        Log.e("aaa", "???????????????2???" + name);
        if (name.contains("??????")) {
            addData2();
            binding.picRel.setLayoutManager(new GridLayoutManager(OrderDetailActivity.this, 2));
            picAdapter = new PicAdapter(R.layout.pic_item2, picList);
        } else {
            addData();
            binding.picRel.setLayoutManager(new GridLayoutManager(OrderDetailActivity.this, 4));
            picAdapter = new PicAdapter(R.layout.pic_item, picList);
        }
        binding.picRel.setAdapter(picAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String imagePath = Environment.getExternalStorageDirectory() + "/Pictures/";
        List<File> fileList = com.blankj.utilcode.util.FileUtils.listFilesInDir(imagePath);
        Log.e("aaa","onBackPressed..." + fileList.size());
        if (fileList != null) {
            if (fileList.size() > 0) {
                for (File file1 : fileList) {
                    String fileNname = file1.getName();
                    if (fileNname.contains(BaseApplication.fileNewName)){
                        Log.e("aaa","onBackPressed..." + fileNname);
                        file1.delete();
                    }
                }
            }
        }
        finish();
    }

    public static List<String> getSystemPhotoList(Context context) {
        List<String> result = new ArrayList<String>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() <= 0) return null; // ????????????
        while (cursor.moveToNext()) {
            int index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index); // ????????????
            File file = new File(path);
            if (file.exists()) {
                result.add(path);
                Log.e("aaa", path);
            }
        }
        return result;
    }

    public void addData() {
        for (int i = 0; i < 8; i++) {
            picList.add(order_data.getUrl());
        }
    }

    public void addData2() {
        for (int i = 0; i < 4; i++) {
            picList.add(order_data.getUrl());
        }
    }

    private void showDownDialog() {
        final Dialog dialog = new Dialog(OrderDetailActivity.this);
        View view = View.inflate(OrderDetailActivity.this, R.layout.dialog_down, null);
        TextView tv_down = view.findViewById(R.id.tv_down);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> systemPhotoList = getSystemPhotoList(OrderDetailActivity.this);
                for (int i = 0; i < systemPhotoList.size(); i++){
                    String s = systemPhotoList.get(i);
                    if (s.contains(BaseApplication.fileNewName)){
                        BaseApplication.mNewPath = s;
                        Log.e("aaa", "path2..." + BaseApplication.mNewPath);
                        saveBitmap11(Environment.getExternalStorageDirectory().getPath() + "/Pictures/"+ order_data.getName() + "??????.jpg");
//                        saveBitmap(Environment.getExternalStorageDirectory().getPath() + "/DCIM/"+ order_data.getName() + "??????.jpg");
                    }
                }
                saveBitmap(Environment.getExternalStorageDirectory().getPath() + "/?????????/"+ BaseApplication.fileName2 + "??????.jpg");
                Toast.makeText(OrderDetailActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_background));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(true);// true??????????????????
        dialog.show();
    }

    private static final int PAY_PROTOCOL_INTENT = 3001;
    private static final int PAY_INTENT = 3002;

    public void getBuyIntentWithPrice(final Activity activity) {
        ToastUtils.show("?????????????????????");
        double newMoney = Double.parseDouble(Contants.photoPrice);
        int newMoney2 = (int) (newMoney * 100);
        Log.e("hw_login???", "money..." + newMoney2);
        BaseApplication.mercdWorth = newMoney;
//        BaseApplication.mercdWorth = 0.01;
        BaseApplication.orderNumber = 0;
        IapClient mClient = Iap.getIapClient(activity);
//        Task<PurchaseIntentResult> task = mClient.createPurchaseIntentWithPrice(IAPSupport.createGetBuyIntentWithPriceReq("1","????????????"));
//        Task<PurchaseIntentResult> task = mClient.createPurchaseIntentWithPrice(IAPSupport.createGetBuyIntentWithPriceReq(
//                BaseApplication.mercdWorth, BaseApplication.orderNumber,newMoney2+"","????????????"));

        // ????????????PurchaseIntentReq??????
        PurchaseIntentReq req = new PurchaseIntentReq();
        // ??????createPurchaseIntent????????????????????????????????????AppGallery Connect????????????????????????
        req.setProductId("zzj1");
        // priceType: 0??????????????????; 1?????????????????????; 2??????????????????
        req.setPriceType(0);
        req.setDeveloperPayload("test");
        PurchaseIntentWithPriceReq getBuyIntentWithPriceReq = IAPSupport.createGetBuyIntentWithPriceReq(
                BaseApplication.mercdWorth, BaseApplication.orderNumber, newMoney2 + "", "????????????");
        Task<PurchaseIntentResult> task = mClient.createPurchaseIntent(req);

        task.addOnSuccessListener(new OnSuccessListener<PurchaseIntentResult>() {
            @Override
            public void onSuccess(PurchaseIntentResult result) {
//                dealSuccess(result, activity);
//                Log.e("hw_login???", "getBuyIntentWithPrice success");

                // ???????????????????????????
                Status status = result.getStatus();
                if (status.hasResolution()) {
                    try {
                        // 6666????????????????????????
                        // ??????IAP????????????????????????
                        status.startResolutionForResult(activity, PAY_INTENT);
                    } catch (IntentSender.SendIntentException exp) {
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    int statusCode = ((ApiException) e).getStatusCode();
                    dealIAPFailed(statusCode, ((IapApiException) e).getStatus());
                }
            }
        });
    }

    public void dealIAPFailed(int statusCode, Status status) {
        if (statusCode == OrderStatusCode.ORDER_NOT_ACCEPT_AGREEMENT) {
            startActivityForResult(OrderDetailActivity.this, status, PAY_PROTOCOL_INTENT);
        } else {
            ToastUtils.show("getBuyIntentWithPrice failed");
            Log.e("hw_login???", "getBuyIntentWithPrice failed");
        }
    }

    public void dealSuccess(PurchaseIntentResult result, Activity activity) {
        if (result == null) {
            ToastUtils.show("dealSuccess, result is null");
            return;
        }
        Status status = result.getStatus();
        if (status.getResolution() == null) {
            ToastUtils.show("intent is null");
            return;
        }
        Log.e("hw_login???", "paymentData???" + result.getPaymentData());
        Log.e("hw_login???", "paymentSignature???" + result.getPaymentSignature());
        if (result.getPaymentSignature() != null && result.getPaymentData() != null) {
            // check sign
            boolean success = IAPSupport.doCheck(result.getPaymentData(), result.getPaymentSignature());
            if (success) {
                startActivityForResult(activity, status, PAY_INTENT);
            } else {
                ToastUtils.show("check sign failed");
            }
        }
    }

    private void startActivityForResult(Activity activity, Status status, int reqCode) {
        if (status.hasResolution()) {
            try {
                //???????????????????????????
                status.startResolutionForResult(activity, reqCode);
            } catch (IntentSender.SendIntentException exp) {
                ToastUtils.show(exp.getMessage());
            }
        } else {
            ToastUtils.show("intent is null");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PAY_PROTOCOL_INTENT == requestCode) {
            getBuyIntentWithPrice(OrderDetailActivity.this);
        } else if (PAY_INTENT == requestCode) {
            handlePayResult(data);
        } else {
            ToastUtils.show("unknown requestCode in onActivityResult");
//            showLog("unknown requestCode in onActivityResult");
        }
    }

    public void handlePayResult(Intent data) {
        // ??????parsePurchaseResultInfoFromIntent??????????????????????????????
        PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(OrderDetailActivity.this).parsePurchaseResultInfoFromIntent(data);
        if (purchaseResultInfo != null) {
            int iapRtnCode = purchaseResultInfo.getReturnCode();
            switch (iapRtnCode) {
                case OrderStatusCode.ORDER_STATE_CANCEL:
                    // ????????????
                    ToastUtils.show("????????????");
                    break;
                case OrderStatusCode.ORDER_STATE_FAILED:
                case OrderStatusCode.ORDER_PRODUCT_OWNED:
                    // ?????????????????????????????????
                    break;
                case OrderStatusCode.ORDER_STATE_SUCCESS:
                    // ????????????
                    order_data.setPay(true);
                    binding.ivPayStatus.setImageDrawable(order_data.isPay() ? getResources().getDrawable(R.drawable.yifukuan_icon)
                            : getResources().getDrawable(R.drawable.daifukuan_icon));

                    binding.tvPayStatus.setText(order_data.isPay() ? getResources().getString(R.string.have_pay)
                            : getResources().getString(R.string.dai_pay));
                    binding.llDown.setVisibility(View.VISIBLE);
                    BaseApplication.sIdPhotoBean.setIsPay(true);

                    photoDaoUtils.updateMeizi(BaseApplication.sIdPhotoBean);
                    IsPay isPay = new IsPay();
                    isPay.setIs_pay(true);
                    EventBus.getDefault().post(isPay);

                    ToastUtils.show("????????????");
                    String inAppPurchaseData = purchaseResultInfo.getInAppPurchaseData();
                    String inAppPurchaseDataSignature = purchaseResultInfo.getInAppDataSignature();
                    // ??????????????????IAP??????????????????
                    // ?????????????????????????????????
                    // ???????????????????????????????????????????????????????????????????????????consumeOwnedPurchase??????????????????
                    surePay(inAppPurchaseData);
                    viewModel.saveic(OrderDetailActivity.this, order_data.getUrl(), order_data.getName(), true);
                    Log.e("aaa", "BaseApplication.mPath...4");
                    break;
                default:
                    break;
            }

            Log.e("hw_login???", "pay...photo...4");
            Log.e("hw_login???", "iapRtnCode???" + iapRtnCode);
        } else {
            Log.e("hw_login???", "pay...photo...5");
            Log.e("hw_login???", "iap failed");
        }
    }

    private void surePay(String inAppPurchaseData) {
        // ??????ConsumeOwnedPurchaseReq??????
        ConsumeOwnedPurchaseReq req = new ConsumeOwnedPurchaseReq();
        String purchaseToken = "";
        try {
            // purchaseToken??????????????????InAppPurchaseData?????????
            InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseData);
            purchaseToken = inAppPurchaseDataBean.getPurchaseToken();
        } catch (JSONException e) {
        }
        req.setPurchaseToken(purchaseToken);
        // ?????????????????????Activity??????
        // ??????????????????????????????????????????consumeOwnedPurchase??????????????????
        Task<ConsumeOwnedPurchaseResult> task = Iap.getIapClient(OrderDetailActivity.this).consumeOwnedPurchase(req);
        task.addOnSuccessListener(new OnSuccessListener<ConsumeOwnedPurchaseResult>() {
            @Override
            public void onSuccess(ConsumeOwnedPurchaseResult result) {
                // ????????????????????????
                Log.e("hw_login???", "pay...photo...6");
                notifyService();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    Status status = apiException.getStatus();
                    int returnCode = apiException.getStatusCode();
                } else {
                    // ??????????????????
                    Log.e("hw_login???", "pay...photo...7");
                }
            }
        });
    }

    private void notifyService() {
        UserUtil.huaweiPay(BaseApplication.mercdName, BaseApplication.mercdWorth, BaseApplication.openId, BaseApplication.orderId,
                BaseApplication.orderNumber, BaseApplication.payTime, new UserUtil.CallBack() {
                    @Override
                    public void onSuccess() {
                        finish();
                        Log.e("hw_login???", "notifyService...1");
                    }

                    @Override
                    public void onFail() {
                        finish();
                        Log.e("hw_login???", "notifyService...2");
                    }

                    @Override
                    public void loginFial() {
                        Log.e("hw_login???", "notifyService...3");
                    }
                }, null);
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
    public void initViewObservable() {
        super.initViewObservable();
        LiveDataBus.get().with(LiveBusConfig.alipaySuccess, Boolean.class).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        KLog.e("isFree--->", "--->onRestart" + isFree);
        if (isFree) {
            if (!isHasToast) {
                ToastUtils.show("??????????????????????????????");
                isHasToast = true;
            }
        }
    }
}
