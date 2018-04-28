package com.example.a99794.framework.utils;


import android.os.Handler;

import com.example.a99794.framework.event.Event;

import org.greenrobot.eventbus.EventBus;

/**
 *@作者 ClearLiang
 *@日期 2018/4/13
 *@描述 EventBus 工具类
 **/

public class EventBusUtils {
    static Handler handler = new Handler();

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**  常规sendEvent*/
    public static<T> void sendEvent(T event) {
        EventBus.getDefault().post(event);
    }
    /**
     *@函数名 - sendDelayedEvent
     *@功能   - 通过延迟0.1秒，实现直接传递数据
     *          利用sendEvent方法，sendEvent会在注册之前post信息，因此启动的界面是收不到信息
     *@参数   - event  事件
     *@返回值 - 无
     **/
    public static<T> void sendDelayedEvent(final T event) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(event);
            }
        }, 100);//0.1秒后执行Runnable中的run方法

    }

    public static<T> void sendStickyEvent(T event) {
        EventBus.getDefault().postSticky(event);
    }

    public static boolean isRegistered(Object subscriber){
        return EventBus.getDefault().isRegistered(subscriber);
    }


    public class EventCode {
        public static final int MAIN_FRAGMENT = 0x000001;
        public static final int HOME_FRAGMENT = 0x000002;
        public static final int ME_FRAGMENT   = 0x000003;

        public static final int MAIN_USER_NAME = 0x000000;

    }
}
