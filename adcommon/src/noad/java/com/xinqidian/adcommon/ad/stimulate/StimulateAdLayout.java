package com.xinqidian.adcommon.ad.stimulate;

import android.content.Context;


/**
 * Created by lipei on 2020/5/13.
 */

public class StimulateAdLayout {
    private static final String TAG = "StimulateAdLayout";
    private Context context;
    private StimulateAdInterface stimulateAdInterface;


    public StimulateAdLayout(Context context,StimulateAdInterface stimulateAdInterface){
        this.context=context.getApplicationContext();
        this.stimulateAdInterface=stimulateAdInterface;

    }


    public void loadAd() {

    }


    public void showAd(){


    }

    public void destoryAdView(){

    }


}
