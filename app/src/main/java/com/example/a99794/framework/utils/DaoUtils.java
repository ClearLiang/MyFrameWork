package com.example.a99794.framework.utils;

import com.blankj.utilcode.util.LogUtils;
import com.example.a99794.framework.model.dao.Shop;
import com.example.a99794.framework.view.base.BaseApplication;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 *@作者 ClearLiang
 *@日期 2018/4/13
 *@描述 数据库操作
 **/
public class DaoUtils {
    private static DaoUtils sDaoUtils;

    private DaoUtils() {
    }

    public static synchronized DaoUtils getDaoUtils() {
        if(sDaoUtils == null){
            sDaoUtils = new DaoUtils();
        }
        return sDaoUtils;
    }

    /**
     * 检查表是否为空
     * */
    public boolean checkIsBlank(){
        List<Shop> shops = new ArrayList<>();
        shops = queryAll();
        if(shops.size()>0){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param shop
     */
    public void insertData(Shop shop) {
        LogUtils.i("添加数据");
        BaseApplication.getDaoInstant().getShopDao().insertOrReplace(shop);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public void deleteData(long id) {
        LogUtils.i("删除id="+id+"的数据");
        BaseApplication.getDaoInstant().getShopDao().deleteByKey(id);
    }
    /**
     * 删除数据
     */
    public void deleteDataAll() {
        LogUtils.i("删除所有数据");
        BaseApplication.getDaoInstant().getShopDao().deleteAll();
    }

    /**
     * 更新数据
     *
     * @param shop
     */
    public void updateData(Shop shop) {
        LogUtils.i("修改"+shop.getId()+"的数据");
        BaseApplication.getDaoInstant().getShopDao().update(shop);
    }

    /**
     * 查询条件为Type=TYPE_LOVE的数据
     *
     * @return
     */
    public List<Shop> queryData(WhereCondition cond) {
        /*return BaseApplication.getDaoInstant().getShopDao().queryBuilder()
                .where(ShopDao.Properties.Type.eq(Shop.TYPE_LOVE)).list();*/
        return BaseApplication.getDaoInstant().getShopDao().queryBuilder().where(cond).list();
    }

    public List<Shop> queryData(WhereCondition cond, WhereCondition... condMore) {
        return BaseApplication.getDaoInstant().getShopDao().queryBuilder()
                .where(cond,condMore).list();
    }

    /**
     * 查询全部数据
     */
    public List<Shop> queryAll() {
        return BaseApplication.getDaoInstant().getShopDao().loadAll();
    }

}
