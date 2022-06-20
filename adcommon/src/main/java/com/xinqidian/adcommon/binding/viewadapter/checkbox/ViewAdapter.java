package com.xinqidian.adcommon.binding.viewadapter.checkbox;

import android.databinding.BindingAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.xinqidian.adcommon.binding.command.BindingCommand;


/**
 * Created by goldze on 2017/6/16.
 */

public class ViewAdapter {
    /**
     * @param bindingCommand //绑定监听
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void setCheckedChanged(final CheckBox checkBox, final BindingCommand<Boolean> bindingCommand) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!compoundButton.isPressed()) {
                    return;
                }
                bindingCommand.execute(b);
            }
        });
    }



    @BindingAdapter(value = {"onSwitchCheckedChangedCommand"}, requireAll = false)
    public static void setSwitchCheckedChanged(final Switch checkBox, final BindingCommand<Boolean> bindingCommand) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                bindingCommand.execute(isChecked);
            }
        });


    }
}
