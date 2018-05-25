package com.example.a99794.framework.testfile;

import com.baidu.location.BDLocation;

/**
 * Created by 99794 on 2018/5/15.
 */

public interface LocationInterface {
    void getLocationMessage(BDLocation location);  // 获取定位信息
    void setLocationMap();      // 设置定位信息（手动修改定位）
    void getLocationMap();      // 获取定位的地图
}
