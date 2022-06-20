package com.xinqidian.adcommon.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.view.PrivacyDialog;
import com.xinqidian.adcommon.view.SecretInterface;

/**
 * Created by lipei on 2020/5/31.
 */

public class SecretDialogUtil {

    private SecretInterface secretInterface;

    public SecretDialogUtil(SecretInterface secretInterface){
        this.secretInterface=secretInterface;
    }


    /**
     * 显示用户协议和隐私政策
     */
    public  void showPrivacy(Context context,String appname) {

        final PrivacyDialog dialog = new PrivacyDialog(context);
        TextView tv_privacy_tips = dialog.findViewById(R.id.tv_privacy_tips);
        TextView btn_exit = dialog.findViewById(R.id.btn_exit);
        TextView btn_enter = dialog.findViewById(R.id.btn_enter);
        tv_privacy_tips.setText("欢迎使用"+appname+"！"+appname+"非常重视您的隐私和个人信息保护。在您使用"+appname+"前请认真阅读《用户协议》及《隐私政策》,您同意并接受全部条款后方可开始使用"+appname);
        dialog.show();

        String string = tv_privacy_tips.getText().toString();
        String key1 = context.getResources().getString(R.string.privacy_tips_key1);
        String key2 = context.getResources().getString(R.string.privacy_tips_key2);
        int index1 = string.indexOf(key1);
        int index2 = string.indexOf(key2);

        //需要显示的字串
        SpannableString spannedString = new SpannableString(string);
        //设置点击字体颜色
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(context.getResources().getColor(R.color.colorBlue));
        spannedString.setSpan(colorSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(context.getResources().getColor(R.color.colorBlue));
        spannedString.setSpan(colorSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置点击字体大小
        AbsoluteSizeSpan sizeSpan1 = new AbsoluteSizeSpan(16, true);
        spannedString.setSpan(sizeSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        AbsoluteSizeSpan sizeSpan2 = new AbsoluteSizeSpan(16, true);
        spannedString.setSpan(sizeSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置点击事件
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if(secretInterface!=null){
                    secretInterface.xieyiClick();
                }//                Intent intent = new Intent(MainActivity.this, TermsActivity.class);
//                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //点击事件去掉下划线
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if(secretInterface!=null){
                    secretInterface.yinsiClick();
                }
//                Intent intent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
//                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //点击事件去掉下划线
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置点击后的颜色为透明，否则会一直出现高亮
        tv_privacy_tips.setHighlightColor(Color.TRANSPARENT);
        //开始响应点击事件
        tv_privacy_tips.setMovementMethod(LinkMovementMethod.getInstance());

        tv_privacy_tips.setText(spannedString);

        //设置弹框宽度占屏幕的80%
        WindowManager m = ((Activity)context).getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (defaultDisplay.getWidth() * 0.80);
        dialog.getWindow().setAttributes(params);

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(secretInterface!=null){
                    secretInterface.canelClick();
                }

            }
        });

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(secretInterface!=null){
                    secretInterface.sureClick();
                }
//                SPUtil.put(MainActivity.this, SP_VERSION_CODE, currentVersionCode);
//                SPUtil.put(MainActivity.this, SP_PRIVACY, true);
//
//                Toast.makeText(MainActivity.this, getString(R.string.confirmed), Toast.LENGTH_SHORT).show();
            }
        });

    }






}
