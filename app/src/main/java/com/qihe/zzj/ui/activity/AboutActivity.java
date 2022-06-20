package com.qihe.zzj.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.qihe.zzj.R;
import com.xinqidian.adcommon.ui.activity.WebViewActivity;
import com.xinqidian.adcommon.util.KeyInformationData;
import com.xinqidian.adcommon.util.ToastUtils;


/**
 * 关于我们
 */
public class AboutActivity extends AppCompatActivity{

    private ImageView back_iv;
    private RelativeLayout rl_user_agreement;
    private RelativeLayout rl_privacy_policy;
    private TextView tv_version;
    private TextView tv_update_version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        back_iv = findViewById(R.id.back_iv);
        rl_user_agreement = findViewById(R.id.rl_user_agreement);
        rl_privacy_policy = findViewById(R.id.rl_privacy_policy);
        tv_version = findViewById(R.id.tv_version);
        tv_update_version = findViewById(R.id.tv_update_version);

        tv_version.setText("当前版本V" + KeyInformationData.getInstance(AboutActivity.this).getAppVersionName());
        initListener();
    }

    private void initListener(){
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_user_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(AboutActivity.this, WebViewActivity.class);
                    intent.putExtra("url", "http://www.qihe.website/yonghu_xieyi_qihe.html");
                    intent.putExtra("title", "用户协议");
                    startActivity(intent);
                } catch (Exception ignored) {

                }
            }
        });
        rl_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(AboutActivity.this, WebViewActivity.class);
                    intent.putExtra("url", "http://www.shimukeji.cn/yinsimoban_zhengjianzhao_qihe.html");
                    startActivity(intent);
                } catch (Exception ignored) {

                }
            }
        });
        tv_update_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("当前已是最新版本");
//                ToastUtils.show("更新到最新版本");
            }
        });
    }
}
