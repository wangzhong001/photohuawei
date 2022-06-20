package com.xinqidian.adcommon.base;

/**
 * Created by lipei on 2018/11/27.
 * Activity初始化操作
 */

public interface IBaseActivity {


    /**
     * 初始化界面传递参数
     */
    void initParam();


    /**
     * 初始化数据
     */
    void initData();


    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();




    /**
     * 刷新的监听
     */
    void refeshData();




}
