package com.xinqidian.adcommon.binding.viewadapter.seekbar;

import android.databinding.BindingAdapter;
import android.widget.SeekBar;

import com.xinqidian.adcommon.binding.command.BindingCommand;
import com.xinqidian.adcommon.util.KLog;


/**
 * Created by lipei on 2019/1/14.
 */

public class ViewAdapter {
    @BindingAdapter(value = {"OnSeekBarChangeCommand"}, requireAll = false)
    public static void onSeekBarChangeCommand(final SeekBar seekBar, final BindingCommand<String> bindingCommand) {

        String lowValue = "-70";
        String highValue = "70";
        final String tempValue = String.valueOf(0 - Integer.parseInt(lowValue));
        seekBar.setMax(Integer.parseInt(highValue) - Integer.parseInt(lowValue));

       seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               float reust=convertProgress(progress - Integer.parseInt(tempValue));
               bindingCommand.execute(String.valueOf(reust));

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }
       });
    }


    @BindingAdapter(value = {"onWalletSeekBarChangeCommand","maxValue","minValue","currentValue"}, requireAll = false)
    public static void onWalletSeekBarChangeCommand(final SeekBar seekBar, final BindingCommand<String> bindingCommand,String lowValue,String highValue,String currentValue) {
        final String tempValue = String.valueOf(0 - Integer.parseInt(lowValue));
        seekBar.setMax(Integer.parseInt(highValue) - Integer.parseInt(lowValue));

//        seekBar.setProgress((int) (Float.parseFloat(currentValue) + Integer.parseInt(tempValue)));


        KLog.e("value--->",highValue+"--->"+lowValue+"--->"+currentValue);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float reust=convertProgress(progress - Integer.parseInt(tempValue));
                bindingCommand.execute(String.valueOf(reust));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private static float convertProgress(int intVal) {
        float result;
        //这里/10 是因为前面每一个数都扩大10倍，因此这里需要/10还原
        result = intVal/100f;
        return result;
    }

}
