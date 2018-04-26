package com.example.a99794.framework.utils;


import com.example.a99794.framework.event.Event;

import org.greenrobot.eventbus.EventBus;

/**
 *@作者 ClearLiang
 *@日期 2018/4/13
 *@描述 EventBus 工具类
 **/

public class EventBusUtils {

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }


    public class EventCode {
        public static final int MAIN_FRAGMENT = 0x000001;
        public static final int HOME_FRAGMENT = 0x000002;
        public static final int ME_FRAGMENT   = 0x000003;

        public static final int MAIN_USER_NAME = 0x000000;

    }
}
