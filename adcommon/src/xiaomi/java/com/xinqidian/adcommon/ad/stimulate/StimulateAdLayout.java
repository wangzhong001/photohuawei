package com.xinqidian.adcommon.ad.stimulate;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.ad.IRewardVideoAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.miui.zeus.mimo.sdk.listener.MimoRewardVideoListener;
import com.xiaomi.ad.common.pojo.AdType;
import com.xinqidian.adcommon.app.Contants;

/**
 * Created by lipei on 2020/5/13.
 */

public class StimulateAdLayout implements MimoRewardVideoListener{
    private static final String TAG = "StimulateAdLayout";
    private IRewardVideoAdWorker mWorker;
    private Context context;
    private boolean isSuccess=false;
    private StimulateAdInterface stimulateAdInterface;


    public StimulateAdLayout(Context context,StimulateAdInterface stimulateAdInterface){
        this.context=context.getApplicationContext();
        this.stimulateAdInterface=stimulateAdInterface;

    }


    public void loadAd() {
        try {
            mWorker = AdWorkerFactory.getRewardVideoAdWorker(context,Contants.XIAOMI_STIMULATE_ID, AdType.AD_REWARDED_VIDEO);
            mWorker.setListener(this);
            mWorker.recycle();
            if (!mWorker.isReady()) {
                mWorker.load();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showAd(){
        try {
            mWorker.show();
        } catch (Exception e) {
            Log.d(TAG,e.toString());
            e.printStackTrace();
        }

    }

    public void destoryAdView(){
        if(mWorker!=null && isSuccess){
            try {
                mWorker.recycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onVideoStart() {

    }

    @Override
    public void onVideoPause() {

    }

    @Override
    public void onVideoComplete() {
        Log.e(TAG,"-->onVideoComplete");
        isSuccess=true;
        stimulateAdInterface.onStimulateAdSuccess();

    }

    @Override
    public void onAdPresent() {
        Log.e(TAG,"-->onAdPresent");

        stimulateAdInterface.onStimulateAdPresent();

    }

    @Override
    public void onAdClick() {
        Log.e(TAG,"-->onAdClick");


        stimulateAdInterface.onStimulateAdClick();
    }

    @Override
    public void onAdDismissed() {
        Log.e(TAG,"-->onAdDismissed");

        if(isSuccess){
            stimulateAdInterface.onStimulateAdDismissed();

        }

    }

    @Override
    public void onAdFailed(String s) {
        stimulateAdInterface.onStimulateAdFailed(s);
        Log.e(TAG,s);

    }

    @Override
    public void onAdLoaded(int i) {
        stimulateAdInterface.onStimulateAdLoaded(i);

    }

    @Override
    public void onStimulateSuccess() {
        Log.e(TAG,"-->onStimulateSuccess");


    }


//    @Override
//    public void onAdPresent() {
//        stimulateAdInterface.onStimulateAdPresent();
//
//    }
//
//    @Override
//    public void onAdClick() {
//        stimulateAdInterface.onStimulateAdClick();
//
//    }
//
//    @Override
//    public void onAdDismissed() {
//        stimulateAdInterface.onStimulateAdDismissed();
//
//    }
//
//    @Override
//    public void onAdFailed(String s) {
//        Log.d(TAG,s);
//        stimulateAdInterface.onStimulateAdFailed(s);
//
//    }
//
//    @Override
//    public void onAdLoaded(int i) {
//        mAdSize=i;
//        stimulateAdInterface.onStimulateAdLoaded(i);
//
//    }
//
//    @Override
//    public void onStimulateSuccess() {
//        isSuccess=true;
//        stimulateAdInterface.onStimulateAdSuccess();
//
//    }
}
