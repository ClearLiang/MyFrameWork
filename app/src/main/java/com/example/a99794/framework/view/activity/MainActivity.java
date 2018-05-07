package com.example.a99794.framework.view.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.a99794.framework.R;
import com.example.a99794.framework.event.Event;
import com.example.a99794.framework.presenter.MainActivityPresenter;
import com.example.a99794.framework.presenter.viewinterface.MainViewInterface;
import com.example.a99794.framework.service.MyIntentService;
import com.example.a99794.framework.utils.EventBusUtils;
import com.example.a99794.framework.utils.NotificationUtils;
import com.example.a99794.framework.utils.RSAUtils;
import com.example.a99794.framework.utils.ScanUtils;
import com.example.a99794.framework.view.base.BaseActivity;
import com.example.a99794.framework.view.widget.CirclePgBar;
import com.example.a99794.framework.wxapi.Config;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yzq.zxinglibrary.common.Constant;

import javax.crypto.Cipher;

import rx.functions.Action1;

public class MainActivity extends BaseActivity<MainViewInterface, MainActivityPresenter> implements MainViewInterface {
    private Button btnScan, btnCreate, btnLoginOther, btnFinger,
            btnEncryption, btnDecryption, btnProgressbar,
            btnNext, btnActivity2, btn1, btn2;
    private TextView tvScanResult;
    private ImageView ivMain;
    private Bitmap bitmap;
    private EditText etCreate;

    byte[] result, result1;
    String resultBefore, resultAfter;

    private CirclePgBar mCirclePgBar;

    /**
     * 微信登录相关
     */
    private IWXAPI api;


    @Override
    protected MainActivityPresenter createPresenter() {
        return new MainActivityPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initLoginOther();
    }

    private void initLoginOther() {
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, Config.APP_ID_WX,true);
        //将应用的appid注册到微信
        api.registerApp(Config.APP_ID_WX);
    }

    // 需要注册EventBus时，返回true
    /*@Override
    protected boolean isRegisterEventBus() {
        return true;
    }*/

    private void initView() {
        btnScan = findViewById(R.id.btn_Scan);
        btnCreate = findViewById(R.id.btn_Create);
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btnEncryption = findViewById(R.id.btn_encryption);
        btnDecryption = findViewById(R.id.btn_decryption);
        btnActivity2 = findViewById(R.id.btn_activity_2);
        btnNext = findViewById(R.id.btn_fragment);
        etCreate = findViewById(R.id.et_create);
        tvScanResult = findViewById(R.id.tv_scan_result);
        ivMain = findViewById(R.id.iv_main);
        btnLoginOther = findViewById(R.id.btn_login_other);
        btnFinger = findViewById(R.id.btn_finger);
        mCirclePgBar = findViewById(R.id.circle_pgbar);
        btnProgressbar = findViewById(R.id.btn_progressbar);
    }

    @Override
    protected void intData() {

    }

    @Override
    protected void initEvent() {
        //使用自定义键盘
        //showKeyBoard(MainActivity.this, etCreate);
        showSafeKeyBoard(MainActivity.this, etCreate);

        setClick(btnScan, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ScanUtils.getScanUtils().startScan(MainActivity.this, REQUEST_SCAN_CODE);
            }
        });

        setClick(btnCreate, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                bitmap = ScanUtils.getScanUtils().startCreate(MainActivity.this, etCreate.getText().toString());
                ivMain.setImageBitmap(bitmap);
            }
        });

        ivMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mPresenter.showInputDialog(MainActivity.this, bitmap);
                return false;
            }
        });

        setClick(btnEncryption, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                byte[] srcData = etCreate.getText().toString().getBytes();
                LogUtils.i("加密前的东西：" + etCreate.getText().toString());
                result = RSAUtils.getRSAUtils().processData(srcData, RSAUtils.getRSAUtils().getPublicKey(), Cipher.ENCRYPT_MODE);
                resultBefore = new String(result);
                LogUtils.i("加密后的东西：" + resultBefore);
            }
        });

        setClick(btnDecryption, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String rs = new String(result);
                LogUtils.i("解密前的东西：" + rs);
                result1 = RSAUtils.getRSAUtils().processData(result, RSAUtils.getRSAUtils().getPrivateKey(), Cipher.DECRYPT_MODE);
                resultAfter = new String(result1);
                LogUtils.i("解密后的东西：" + resultAfter);
            }
        });

        setClick(btnProgressbar, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mCirclePgBar.setTargetProgress(Integer.parseInt(etCreate.getText().toString()));
            }
        });

        setClick(btnNext, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                openActivity(FragmentActivity.class);
            }
        });

        setClick(btnActivity2, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                EventBusUtils.sendDelayedEvent(new Event(etCreate.getText().toString()));
                openActivity(SecondActivity.class);
            }
        });

        setClick(btn1, new Action1<Void>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void call(Void aVoid) {
                /*mBundle.putString("key", etCreate.getText().toString());
                openService(MyIntentService.class,mBundle);
                openActivityForResult(PayActivity.class,REQUEST_PAY_CODE,mBundle);*/

                //NotificationUtils.getNotificationUtils(MainActivity.this).showNotification(null,null);

                initRemoteViews();

            }
        });

        setClick(btn2, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //openActivity(FirstActivity.class);
                /*String a = RSAUtils.getRSAUtils().myEncrypt(etCreate.getText().toString());
                String b = RSAUtils.getRSAUtils().myDecryption(a);
                LogUtils.i(a);
                LogUtils.i(b);*/

                NotificationUtils.getNotificationUtils(MainActivity.this).closeNotification();

            }
        });

        setClick(btnLoginOther, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                // send oauth request
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_test";
                api.sendReq(req);
            }
        });

        setClick(btnFinger, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //openActivity(ThirdActivity.class);
                openActivityForResult(ThirdActivity.class,REQUEST_FINGER_CODE);
            }
        });

    }

    private void initRemoteViews(){
        //1.创建RemoteViews实例
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_remote);
        remoteViews.setTextViewText(R.id.tv_widget_remote_1, "123456qwerty" );
        remoteViews.setImageViewResource(R.id.imageView3, R.drawable.qmui_icon_checkbox_checked);

        //2.构建一个打开Activity的PendingIntent
        Intent intent=new Intent(MainActivity.this,MainActivity.class);
        PendingIntent mPendingIntent=PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //3.创建一个Notification
        NotificationUtils.getNotificationUtils(MainActivity.this).showNotification(null,mPendingIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_SCAN_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                tvScanResult.setText("扫描结果为：" + content);
            }
        }
        // 支付界面回传
        else if (requestCode == REQUEST_PAY_CODE && resultCode == RESULT_OK) {
            String content = data.getStringExtra(Constant.CODED_CONTENT);
            ToastUtils.showShort("MainActivity收到PayActivity结果：" + content);
        }
        // 指纹验证回传
        else if(requestCode == REQUEST_FINGER_CODE && resultCode == RESULT_OK){
            String content = data.getStringExtra(Constant.CODED_CONTENT);
            //ToastUtils.showShort("MainActivity收到ThirdActivity结果" + content);
            LogUtils.i("MainActivity收到ThirdActivity结果" + content);
        }


    }

}
