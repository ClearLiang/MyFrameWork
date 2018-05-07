package com.example.a99794.framework.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.example.a99794.framework.model.entity.WXAccessTokenEntity;
import com.example.a99794.framework.model.entity.WXBaseRespEntity;
import com.example.a99794.framework.model.entity.WXCheckTokenEntity;
import com.example.a99794.framework.model.entity.WXUserInfo;
import com.example.a99794.framework.utils.SPUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * @作者 ClearLiang
 * @日期 2018/5/4
 * @描述 微信授权界面
 **/
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    /**
     * 微信登录相关
     */
    private IWXAPI api;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, Config.APP_ID_WX,true);
        //将应用的appid注册到微信
        api.registerApp(Config.APP_ID_WX);
        LogUtils.d("------------------------------------");
        //注意：
        // 第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，
        // 则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean result =  api.handleIntent(getIntent(), this);
            if(!result){
                LogUtils.d("参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data,this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogUtils.d("baseReq:"+ JSON.toJSONString(baseReq));
    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtils.d("baseResp:--A:"+JSON.toJSONString(baseResp));
        LogUtils.d("baseResp:--B:"+baseResp.errStr+","+baseResp.openId+","+baseResp.transaction+","+baseResp.errCode);
        WXBaseRespEntity entity = JSON.parseObject(JSON.toJSONString(baseResp),WXBaseRespEntity.class);
        String result = "";
        switch(baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result ="发送成功";
                getAccessToken(entity);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                LogUtils.d("发送取消");
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                LogUtils.d("发送被拒绝");
                finish();
                break;
            case BaseResp.ErrCode.ERR_BAN:
                result = "签名错误";
                LogUtils.d("签名错误");
                break;
            default:
                result = "发送返回";
                finish();
                break;
        }
        Toast.makeText(WXEntryActivity.this,result, Toast.LENGTH_LONG).show();

    }

    /**
     * 通过code获取access_token
     *
     * @param entity*/
    private void getAccessToken(WXBaseRespEntity entity){
        OkHttpUtils.get().url("https://api.weixin.qq.com/sns/oauth2/access_token")
                .addParams("appid",Config.APP_ID_WX)
                .addParams("secret",Config.APP_SECRET_WX)
                .addParams("code",entity.getCode())
                .addParams("grant_type","authorization_code")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        LogUtils.d("请求错误..");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("response:"+response);
                        WXAccessTokenEntity accessTokenEntity = JSON.parseObject(response,WXAccessTokenEntity.class);
                        if(accessTokenEntity!=null){
                            getUserInfo(accessTokenEntity);
                        }else {
                            LogUtils.d("获取失败");
                        }
                    }
                });
    }

    /**
     * 获取个人信息
     * @param accessTokenEntity
     */
    private void getUserInfo(WXAccessTokenEntity accessTokenEntity) {
        //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
        OkHttpUtils.get()
                .url("https://api.weixin.qq.com/sns/userinfo")
                .addParams("access_token",accessTokenEntity.getAccess_token())
                .addParams("openid",accessTokenEntity.getOpenid())//openid:授权用户唯一标识
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        LogUtils.d("获取错误..");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("userInfo:"+response);
                        WXUserInfo wxResponse = JSON.parseObject(response,WXUserInfo.class);
                        LogUtils.d("微信登录资料已获取，后续未完成");
                        String headUrl = wxResponse.getHeadimgurl();
                        LogUtils.d("头像Url:"+headUrl);
                        SPUtils.getInstance().put("headUrl",headUrl);
                        //App.getShared().putString("headUrl",headUrl);
                        Intent intent = getIntent();
                        intent.putExtra("headUrl",headUrl);
                        WXEntryActivity.this.setResult(0,intent);
                        finish();
                    }
                });
    }

    /**
     * 刷新或续期access_token使用
     * */
    private void updateToken(String refreshToken){

        OkHttpUtils.get().url("https://api.weixin.qq.com/sns/oauth2/refresh_token")
                .addParams("appid",Config.APP_ID_WX)
                .addParams("grant_type","authorization_code")
                .addParams("refresh_token",refreshToken)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        LogUtils.d("请求错误..");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("response:"+response);
                        WXAccessTokenEntity accessTokenEntity = JSON.parseObject(response,WXAccessTokenEntity.class);
                        if(accessTokenEntity!=null){
                            getUserInfo(accessTokenEntity);
                        }else {
                            LogUtils.d("获取失败");
                        }
                    }
                });
    }

    /**
     * 检验授权凭证（access_token）是否有效
     * */
    private void checkToken(final WXAccessTokenEntity accessTokenEntity){
        OkHttpUtils.get().url("https://api.weixin.qq.com/sns/auth")
                .addParams("access_token",accessTokenEntity.getAccess_token())
                .addParams("openid",accessTokenEntity.getOpenid())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        LogUtils.d("请求错误..");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("response:"+response);
                        WXCheckTokenEntity checkTokenEntity = JSON.parseObject(response,WXCheckTokenEntity.class);
                        if(checkTokenEntity!=null){
                            if(checkTokenEntity.getErrcode().equals("0")){
                                LogUtils.i("授权凭证依旧有效");
                            }else {
                                updateToken(accessTokenEntity.getRefresh_token());
                            }
                        }else {
                            LogUtils.d("获取失败");
                        }
                    }
                });
    }



}
