package com.smkj.zzj.gen;

import android.content.Context;

import com.qihe.zzj.bean.PayBean;

import java.util.List;


/**
 *
 */
public class PayBeanHelp {

    private PayBeanDao mPayBeanDao;
    private static PayBeanHelp sPayBeanHelp;

    public PayBeanHelp(Context context) {
        mPayBeanDao = MyDBManager.getInstace(context).getDevOpenHelper("number").getSession().getPayBeanDao();
    }

    public static PayBeanHelp getNewInstance(Context context) {
        if (sPayBeanHelp == null) {
            sPayBeanHelp = new PayBeanHelp(context);
        }
        return sPayBeanHelp;
    }

    //增加一条数据
    public void insertData(PayBean bean) {
        mPayBeanDao.insert(bean);
    }

    //增加多条数据
    public void insertAllData(List<PayBean> list){
        mPayBeanDao.insertInTx(list);
    }

    //删一条数据
    public void deleteData(PayBean bean) {
        if (bean != null) {
            mPayBeanDao.delete(bean);
        } else {
            mPayBeanDao.deleteAll();
        }
    }

    //删多条数据
    public void deleteAllData(List<PayBean> list){
        if(list != null && list.size() > 0){
            mPayBeanDao.deleteInTx(list);
        }else{
            mPayBeanDao.deleteAll();
        }
    }

    //改
    public void updateData(PayBean bean) {
        mPayBeanDao.update(bean);
    }

    //查
    public List<PayBean> queryAllData() {
        return mPayBeanDao.loadAll();
    }

    /**
     * 查询当前用户所有数据
     * @param orderId 用户名
     * @return
     */
    public PayBean queryDataByName(String orderId) {
        return mPayBeanDao.queryBuilder().where(PayBeanDao.Properties.OrderId.eq(orderId)).build().unique();
    }

}
