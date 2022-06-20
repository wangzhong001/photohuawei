package com.xinqidian.adcommon.base;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.view.View;

import com.xinqidian.adcommon.binding.command.BindingCommand;


/**
 * Created by lipei on 2019/1/10.
 */

public class TitleViewModel extends BaseViewModel {
    public TitleViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<String> titleName=new ObservableField<>();

    public ObservableInt tabar=new ObservableInt(View.GONE);

    public ObservableBoolean isWhite=new ObservableBoolean(true);

    public BindingCommand backClick;


    @Override
    public void onDestory() {
        super.onDestory();
        if(backClick!=null){
            backClick=null;
        }
    }
}
