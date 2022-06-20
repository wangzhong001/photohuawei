package com.qihe.zzj.view;

import android.support.annotation.NonNull;

import com.qihe.zzj.bean.KefuModel;
import com.xinqidian.adcommon.base.ItemViewModel;
import com.xinqidian.adcommon.binding.command.BindingAction;
import com.xinqidian.adcommon.binding.command.BindingCommand;

/**
 * Created by lipei on 2020/6/30.
 */

public class KufuItemViewModel extends ItemViewModel<MakePhotoViewModel> {
    public KefuModel kefuModel;
    public KufuItemViewModel(@NonNull MakePhotoViewModel viewModel, KefuModel kefuModel) {
        super(viewModel);
        this.kefuModel=kefuModel;
    }

    public BindingCommand itemClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            viewModel.toKefuDetail(kefuModel);
        }
    });

}
