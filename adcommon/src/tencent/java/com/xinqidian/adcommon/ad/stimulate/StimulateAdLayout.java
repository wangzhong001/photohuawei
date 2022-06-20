package com.xinqidian.adcommon.ad.stimulate;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;
import com.xinqidian.adcommon.app.Contants;

/**
 * Created by lipei on 2020/5/13.
 */

public class StimulateAdLayout implements RewardVideoADListener {
    private static final String TAG = "StimulateAdLayout";
    private Context context;
    private StimulateAdInterface stimulateAdInterface;

    private RewardVideoAD rewardVideoAD;
    private boolean adLoaded;//广告加载成功标志
    private boolean videoCached;//视频素材文件下载完成标志


    public StimulateAdLayout(Context context, StimulateAdInterface stimulateAdInterface) {
        this.context = context.getApplicationContext();
        this.stimulateAdInterface = stimulateAdInterface;

    }


    public void loadAd() {
        rewardVideoAD = new RewardVideoAD(context, Contants.TENCENT_APPID, Contants.TENCENT_STIMULATE_ID, this, true);
        adLoaded = false;
        videoCached = false;
        // 2. 加载激励视频广告
        rewardVideoAD.loadAD();
    }


    public void showAd() {
        if (adLoaded && rewardVideoAD != null) {//广告展示检查1：广告成功加载，此处也可以使用videoCached来实现视频预加载完成后再展示激励视频广告的逻辑
            if (!rewardVideoAD.hasShown()) {//广告展示检查2：当前广告数据还没有展示过
                long delta = 1000;//建议给广告过期时间加个buffer，单位ms，这里demo采用1000ms的buffer
                //广告展示检查3：展示广告前判断广告数据未过期
                if (SystemClock.elapsedRealtime() < (rewardVideoAD.getExpireTimestamp() - delta)) {
                    rewardVideoAD.showAD();
                } else {
                    Log.d(TAG,"激励视频广告已过期，请再次请求广告后进行广告展示！");
                    if(stimulateAdInterface!=null){
                        stimulateAdInterface.onFail();
                    }
                }
            } else {
                Log.d(TAG,"此条广告已经展示过，请再次请求广告后进行广告展示！");
                if(stimulateAdInterface!=null){
                    stimulateAdInterface.onFail();
                }

            }
        } else {
            if(stimulateAdInterface!=null){
                stimulateAdInterface.onFail();
            }
            Log.d(TAG,"成功加载广告后再进行广告展示！");

        }

    }

    public void destoryAdView() {

    }

    /**
     * 广告加载成功，可在此回调后进行广告展示
     **/
    @Override
    public void onADLoad() {
        adLoaded = true;
//        String msg = "load ad success ! expireTime = " + new Date(System.currentTimeMillis() +
//                rewardVideoAD.getExpireTimestamp() - SystemClock.elapsedRealtime());
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
//        Log.d(TAG, "eCPM = " + rewardVideoAD.getECPM() + " , eCPMLevel = " + rewardVideoAD.getECPMLevel());
    }

    /**
     * 视频素材缓存成功，可在此回调后进行广告展示
     */
    @Override
    public void onVideoCached() {
        videoCached = true;
        Log.i(TAG, "onVideoCached");
    }

    /**
     * 激励视频广告页面展示
     */
    @Override
    public void onADShow() {
        Log.i(TAG, "onADShow");
    }

    /**
     * 激励视频广告曝光
     */
    @Override
    public void onADExpose() {
        Log.i(TAG, "onADExpose");
    }

    /**
     * 激励视频触发激励（观看视频大于一定时长或者视频播放完毕）
     */
    @Override
    public void onReward() {
        Log.i(TAG, "onReward");
    }

    /**
     * 激励视频广告被点击
     */
    @Override
    public void onADClick() {
//        Map<String, String> map = rewardVideoAD.getExts();
//        String clickUrl = map.get("clickUrl");
//        Log.i(TAG, "onADClick clickUrl: " + clickUrl);
    }

    /**
     * 激励视频播放完毕
     */
    @Override
    public void onVideoComplete() {
        Log.i(TAG, "onVideoComplete");
        stimulateAdInterface.onStimulateAdSuccess();
    }

    /**
     * 激励视频广告被关闭
     */
    @Override
    public void onADClose() {
        Log.i(TAG, "onADClose");
            stimulateAdInterface.onStimulateAdDismissed();


    }

    /**
     * 广告流程出错
     */
    @Override
    public void onError(AdError adError) {
        Log.e(TAG,adError.getErrorMsg());
//        String msg = String.format(Locale.getDefault(), "onError, error code: %d, error msg: %s",
//                adError.getErrorCode(), adError.getErrorMsg());
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


}
