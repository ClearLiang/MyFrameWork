package com.example.a99794.framework.view.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.example.a99794.framework.model.dao.DaoMaster;
import com.example.a99794.framework.model.dao.DaoSession;


/**
 * @作者 ClearLiang
 * @日期 2018/4/13
 * @描述 APP全局
 * 这里可以加上对于Android 设备特有硬件的操作，如扫码、拍照、录像、数据库创建
 **/

public class BaseApplication extends Application {
    public static DaoSession daoSession;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//突破Android64K方法数
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);//初始化BlankJ的工具类
        LogUtils.getConfig()
                .setLogSwitch(true)//log开关
                .setGlobalTag(getClass().getSimpleName());//全局tag

        //配置数据库
        setupDatabase();
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库 MyDataBase.db
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "MyDataBase.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }
}
