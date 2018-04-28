package com.example.a99794.framework.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.a99794.framework.event.Event;
import com.example.a99794.framework.presenter.MainPresenter;
import com.example.a99794.framework.presenter.viewinterface.MainViewInterface;
import com.example.a99794.framework.utils.EventBusUtils;
import com.example.a99794.framework.utils.RSAUtils;
import com.example.a99794.framework.utils.ScanUtils;
import com.example.a99794.framework.view.base.BaseActivity;
import com.example.a99794.mytest.R;
import com.yzq.zxinglibrary.common.Constant;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.crypto.Cipher;

import rx.functions.Action1;

public class MainActivity extends BaseActivity<MainViewInterface, MainPresenter> implements MainViewInterface {
    private Button btnScan,btnCreate,btnShowkey,btnNext,btnActivity2;
    private TextView tvScanResult;
    private ImageView image;
    private Bitmap bitmap;
    private EditText etCreate;

    byte[] result,result1;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    // 需要注册EventBus时，返回true
    /*@Override
    protected boolean isRegisterEventBus() {
        return true;
    }*/

    private void initView() {
        btnScan = findViewById(R.id.btn_Scan);
        btnCreate = findViewById(R.id.btn_create);
        tvScanResult = findViewById(R.id.tv_scan_result);
        image = findViewById(R.id.image);
        etCreate = findViewById(R.id.et_create);
        btnShowkey = findViewById(R.id.btn_showkey);
        btnNext = findViewById(R.id.btn_next);
        btnActivity2 = findViewById(R.id.btn_activity_2);
    }

    @Override
    protected void intData() {

    }

    @Override
    protected void initEvent() {
        //使用自定义键盘
        //showKeyBoard(MainActivity.this, etCreate);
        showSafeKeyBoard(MainActivity.this,etCreate);

        setClick(btnScan, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ScanUtils.getScanUtils().startScan(MainActivity.this, REQUEST_SCAN_CODE);
            }
        });
        setClick(btnCreate, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                /*bitmap = ScanUtils.getScanUtils().startCreate(MainActivity.this, etCreate.getText().toString());
                image.setImageBitmap(bitmap);*/

                byte[] srcData = {12};
                LogUtils.i("加密前的东西："+srcData[0]);
                result = RSAUtils.getRSAUtils().processData(srcData,RSAUtils.getRSAUtils().getPublicKey(), Cipher.ENCRYPT_MODE);
                LogUtils.i("加密后的东西："+result);

            }
        });
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mPresenter.showInputDialog(MainActivity.this, bitmap);
                return false;
            }
        });
        setClick(btnShowkey, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //openActivity(FirstActivity.class);
                LogUtils.i("解密前的东西："+result);
                result1 = RSAUtils.getRSAUtils().processData(result,RSAUtils.getRSAUtils().getPrivateKey(),Cipher.DECRYPT_MODE);
                LogUtils.i("解密后的东西："+result1[0]);
            }
        });
        setClick(btnNext, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                /*mBundle.putString("key",etCreate.getText().toString());
                openActivity(PayActivity.class,mBundle);*/
                openActivity(FragmentActivity.class);
            }
        });

        setClick(btnActivity2, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                EventBusUtils.sendDelayedEvent(new Event("asdfghjkl"));
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });

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
        } else if (requestCode == REQUEST_PAY_CODE) {

        }
    }

}
