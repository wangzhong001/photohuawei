package com.qihe.zzj.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;


/**
 * Facebook's Fresco 图片加载工具类
 * <p>
 * Note:虽然SimpleDraweeView继承于ImageView,但是最好不要使用ImageView的任何属性.
 * <p>
 * 附常用XML属性配置：
 * fresco:actualImageScaleType="" //图片缩放类型
 * fresco:placeholderImage=""     //默认占位图
 * fresco:placeholderImageScaleType="" //默认占位图缩放类型
 * fresco:roundAsCircle="true"    //显示圆形
 * fresco:roundedCornerRadius=""  //显示圆角
 * <p>
 * 更多属性请详见网址：http://fresco-cn.org/docs/using-drawees-xml.html#_
 * <p>
 * Created by Mr.Wang on 2018/3/29
 */

public class FrescoUtils {

    private static final FrescoUtils FRESCO_INSTANCE = new FrescoUtils();
    private ResizeOptions resizeOptions;

    public static FrescoUtils getInstance() {
        return FRESCO_INSTANCE;
    }


    /**
     * 显示本地文件 （默认不对图片裁剪，适用于显示单张或少量图片）。
     *
     * @param draweeView
     * @param localPath
     */
    public void showImageFile(SimpleDraweeView draweeView, String localPath) {
        configImageFile(draweeView, localPath, false);
    }

    private void configImageFile(SimpleDraweeView draweeView, String localPath, boolean isClip) {
        if (!localPath.startsWith("file://")) {
            localPath = "file://" + localPath;
        }

        if (isClip) {
            if (resizeOptions == null) {
                resizeOptions = new ResizeOptions(200, 200);
            }
            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(localPath))
                    .setResizeOptions(resizeOptions)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(draweeView.getController())
                    .build();
            draweeView.setController(controller);
        } else {
            try {
                draweeView.setImageURI(Uri.parse(localPath));
            } catch (java.lang.IllegalStateException e) {
//                showImageResource(draweeView, R.mipmap.album_err);
            }
        }
    }

    /**
     * 初始化设置Fresco,设置渐变式加载以及缓存配置
     */
    public ImagePipelineConfig getFrescoConfig(Context context) {

        ProgressiveJpegConfig pjpegConfig = new ProgressiveJpegConfig() {
            @Override
            public int getNextScanNumberToDecode(int scanNumber) {
                return scanNumber + 2;
            }

            public QualityInfo getQualityInfo(int scanNumber) {
                boolean isGoodEnough = (scanNumber >= 5);
                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
            }
        };

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setMaxCacheSize(50 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(20 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(8 * ByteConstants.MB)
                .build();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setProgressiveJpegConfig(pjpegConfig)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setDownsampleEnabled(true)
                .build();
        return config;
    }
}
