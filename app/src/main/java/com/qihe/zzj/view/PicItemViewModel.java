package com.qihe.zzj.view;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.qihe.zzj.bean.IdPhotoBean;
import com.xinqidian.adcommon.base.ItemViewModel;
import com.xinqidian.adcommon.binding.command.BindingAction;
import com.xinqidian.adcommon.binding.command.BindingCommand;

/**
 * Created by lipei on 2020/6/30.
 */

public class PicItemViewModel extends ItemViewModel<MakePhotoViewModel> {
    public IdPhotoBean idPhotoBean;
    public int postion;
    public ObservableBoolean isPay=new ObservableBoolean();
    public PicItemViewModel(@NonNull MakePhotoViewModel viewModel,IdPhotoBean idPhotoBean,int postion) {
        super(viewModel);
        this.idPhotoBean=idPhotoBean;
        this.postion=postion;
        isPay.set(idPhotoBean.getIsPay());
    }

    /**
     * 查看详情
     */
    public BindingCommand itemClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            viewModel.toDetai(PicItemViewModel.this);
        }
    });


    /**
     * 取消订单
     */
    public BindingCommand canelOrderClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            viewModel.deletePic(PicItemViewModel.this);
        }
    });

    /**
     * 去支付
     */
    public BindingCommand payClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            viewModel.payOrder(PicItemViewModel.this);
        }
    });
}
