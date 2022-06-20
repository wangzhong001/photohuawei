package com.xinqidian.adcommon.ui.activity;

import android.app.Application;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.binding.command.BindingAction;
import com.xinqidian.adcommon.binding.command.BindingCommand;
import com.xinqidian.adcommon.util.JumpUtils;
import com.xinqidian.adcommon.util.KeyInformationData;
import com.xinqidian.adcommon.util.ToastUtils;

/**
 * Created by lipei on 2020/6/7.
 */

public class FeedBackViewModel extends BaseViewModel {
    public FeedBackViewModel(@NonNull Application application) {
        super(application);
        keyInformationData=KeyInformationData.getInstance(this.getApplication());

    }

    private AVObject feedBackObject ;



    private JumpUtils jumpUtils;

    public BindingCommand backClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });


    /**
     * 输入建议
     */
    public ObservableField<String> inputContent=new ObservableField<>("");


    /**
     * 手机号或者邮箱
     */
    public ObservableField<String> phoneEmailContent=new ObservableField<>("");


    /**
     * 提交
     */
    public BindingCommand sumbitClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(inputContent.get().isEmpty()){
                ToastUtils.show("输入的内容不能为空哦");
                return;
            }

            setData();
            feedBackObject.put(Contants.INPUT_CONTENT,inputContent.get());
            feedBackObject.put(Contants.INPUT_PHONE_EMAIL,phoneEmailContent.get());

            feedBackObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e==null){
                        ToastUtils.show("提交成功");

                    }else {
                        ToastUtils.show("提交失败");

                    }
                }
            });



        }
    });

    /**
     * qq
     */
    public BindingCommand qqClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Context context=getApplication();
            if(jumpUtils==null){
                jumpUtils=new JumpUtils(context);
            }
            jumpUtils.jumpQQ();

        }
    });


    /**
     * 邮箱
     */
    public BindingCommand emailClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Context context=getApplication();
            if(jumpUtils==null){
                jumpUtils=new JumpUtils(context);
            }
            jumpUtils.jumpEmail();

        }
    });


    private KeyInformationData keyInformationData;


    private void setData(){

        if(feedBackObject==null){
            feedBackObject=new AVObject();
        }

        /**
         * 设置app的信息
         */
        feedBackObject.put(Contants.APP_NAME,keyInformationData.getAppName());
        feedBackObject.put(Contants.VERSION_CODE,keyInformationData.getAppVersionCode());
        feedBackObject.put(Contants.VERSION_NAME,keyInformationData.getAppVersionName());
        feedBackObject.put(Contants.PACKAGE_NAME,keyInformationData.getAppPackgeName());

        /**
         * 设置手机的信息
         */

        feedBackObject.put(Contants.BRAND,keyInformationData.getPhoneBrand());
        feedBackObject.put(Contants.MODEL,keyInformationData.getPhoneModel());
        feedBackObject.put(Contants.MANUFACTURER,keyInformationData.getPhoneManufacturer());
        feedBackObject.put(Contants.BUILD_LEVEL,keyInformationData.getBuildLevel());
        feedBackObject.put(Contants.BUILD_VERSION,keyInformationData.getBuildVersion());
    }

}
