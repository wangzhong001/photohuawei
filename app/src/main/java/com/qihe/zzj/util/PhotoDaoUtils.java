package com.qihe.zzj.util;

import android.content.Context;
import android.util.Log;

import com.qihe.zzj.bean.IdPhotoBean;
import com.qihe.zzj.gen.IdPhotoBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by lipei on 2020/6/30.
 */

public class PhotoDaoUtils {
    private static final String TAG = PhotoDaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public PhotoDaoUtils(Context context){
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成meizi记录的插入，如果表未创建，先创建Meizi表
     * @param meizi
     * @return
     */
    public boolean insertPic(IdPhotoBean meizi){
        boolean flag = false;
        flag = mManager.getDaoSession().getIdPhotoBeanDao().insert(meizi) == -1 ? false : true;
        Log.i(TAG, "insert Meizi :" + flag + "-->" + meizi.toString());
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     * @param meiziList
     * @return
     */
    public boolean insertMultPic(final List<IdPhotoBean> meiziList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (IdPhotoBean meizi : meiziList) {
                        mManager.getDaoSession().insertOrReplace(meizi);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     * @param meizi
     * @return
     */
    public boolean updateMeizi(IdPhotoBean meizi){
        boolean flag = false;
        try {
            mManager.getDaoSession().update(meizi);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     * @param meizi
     * @return
     */
    public boolean deleteMeizi(IdPhotoBean meizi){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(meizi);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     * @return
     */
    public boolean deleteAll(){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().deleteAll(IdPhotoBean.class);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<IdPhotoBean> queryAllMeizi(){
        return mManager.getDaoSession().loadAll(IdPhotoBean.class);
    }

    /**
     * 根据主键id查询记录
     * @param key
     * @return
     */
    public IdPhotoBean queryMeiziById(long key){
        return mManager.getDaoSession().load(IdPhotoBean.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<IdPhotoBean> queryMeiziByNativeSql(String sql, String[] conditions){
        return mManager.getDaoSession().queryRaw(IdPhotoBean.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     * @return
     */
    public List<IdPhotoBean> queryMeiziByQueryBuilder(long id){
        QueryBuilder<IdPhotoBean> queryBuilder = mManager.getDaoSession().queryBuilder(IdPhotoBean.class);
        return queryBuilder.where(IdPhotoBeanDao.Properties._id.eq(id)).list();
//        return queryBuilder.where(MeiziDao.Properties._id.ge(id)).list();
    }
}
