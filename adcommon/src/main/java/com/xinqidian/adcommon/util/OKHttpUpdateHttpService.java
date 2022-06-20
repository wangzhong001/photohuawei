/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xinqidian.adcommon.util;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.xinqidian.adcommon.app.AppConfig;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;



/**
 * 使用okhttp
 *
 * @author xuexiang
 * @since 2018/7/10 下午4:04
 */
public class OKHttpUpdateHttpService implements IUpdateHttpService {

    private boolean mIsPostJson;

    public OKHttpUpdateHttpService() {
        this(false);
    }

    public OKHttpUpdateHttpService(boolean isPostJson) {
        mIsPostJson = isPostJson;
    }


    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, Object> params, final @NonNull Callback callBack) {



        OkGo.<String>get(url).params(transform(params)).execute(new com.lzy.okgo.callback.StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                String data=response.body();

                UpdateBean result = new Gson().fromJson(data, UpdateBean.class);
                if (result != null) {
                    if(result.getUpdatePic()!=null){
                        SharedPreferencesUtil.setParam(AppConfig.updatePic,result.getUpdatePic());

                    }

                    if(result.getDownloadUrl()!=null){
                        SharedPreferencesUtil.setParam(AppConfig.downUrl,result.getDownloadUrl());

                    }

                    if(result.getPhoneNmber()!=null){
                        SharedPreferencesUtil.setParam(AppConfig.phoneNumber,result.getPhoneNmber());

                    }

                }
                KLog.e("data-->",data+"--->get");
                callBack.onSuccess(data);
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                super.onError(response);
                callBack.onError(response.getException());
            }
        });

    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, Object> params, final @NonNull Callback callBack) {
        OkGo.<String>post(url).params(transform(params)).execute(new com.lzy.okgo.callback.StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                String data=response.body();

                UpdateBean result = new Gson().fromJson(data, UpdateBean.class);
                if (result != null) {
                    if(result.getUpdatePic()!=null){
                        SharedPreferencesUtil.setParam(AppConfig.updatePic,result.getUpdatePic());

                    }

                    if(result.getDownloadUrl()!=null){
                        SharedPreferencesUtil.setParam(AppConfig.downUrl,result.getDownloadUrl());

                    }

                    if(result.getPhoneNmber()!=null){
                        SharedPreferencesUtil.setParam(AppConfig.phoneNumber,result.getPhoneNmber());

                    }

                }
                KLog.e("data-->",data+"--->get");
                callBack.onSuccess(data);
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                super.onError(response);
                callBack.onError(response.getException());
            }
        });
    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, final @NonNull DownloadCallback callback) {


        OkGo.<File>get(url).execute(new com.lzy.okgo.callback.FileCallback(path, fileName) {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onStart(com.lzy.okgo.request.base.Request<File, ? extends com.lzy.okgo.request.base.Request> request) {
                super.onStart(request);
                callback.onStart();
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<File> response) {
                super.onError(response);
                callback.onError(response.getException());
            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);

                callback.onProgress(progress.fraction, progress.totalSize);
            }
        });




    }

    @Override
    public void cancelDownload(@NonNull String url) {
//        OkHttpUtils.getInstance().cancelTag(url);
    }

    private Map<String, String> transform(Map<String, Object> params) {
        Map<String, String> map = new TreeMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            map.put(entry.getKey(), entry.getValue().toString());
        }
        return map;
    }


}