package com.example.a99794.framework.model.api;

import com.example.a99794.framework.model.bean.UserBean;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @作者 ClearLiang
 * @日期 2018/4/26
 * @描述 @desc
 **/
public interface ApiService {
    String API_SERVER_IP = "http://123.456.789.0:8080/";
    String API_SERVER_URL = API_SERVER_IP + "abc/";

    //登陆
    @POST("client/login")
    Observable<UserBean> login(
            @Query("username") String username,
            @Query("password") String password);

    @POST("client/register")
    Observable<ResponseBody> register(
            @Query("name") String name,
            @Query("pwd")  String pwd
    );

    /*获取到返回的Header和响应码的,用Observable<Result<T>> 代替 Observable<T>*/
    @GET("")
    Observable<Result<UserBean>> BlogService();
}
