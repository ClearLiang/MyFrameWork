package com.example.a99794.framework.testfile;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.example.a99794.framework.R;
import com.example.a99794.framework.utils.FingerPrintUtils;


@SuppressLint("ValidFragment")
@TargetApi(Build.VERSION_CODES.M)
public class FingerprintAuthenticationDialogFragmentCopy extends DialogFragment {
    private TextView fingerprintStatus; //验证状态提示
    private TextView tvPasswordLogin;   //密码登录
    private ImageView fingerprintIcon;  //图标

    // 向外界传值的接口
    public interface FragmentListener {
        void process(boolean str);
    }

    public FingerprintAuthenticationDialogFragmentCopy.FragmentListener listterner;

    public FingerprintAuthenticationDialogFragmentCopy(FingerprintAuthenticationDialogFragmentCopy.FragmentListener listterner) {
        this.listterner = listterner;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    fingerprintStatus.setText("尝试次数过多，请稍后重试");
                    fingerprintStatus.setTextColor(fingerprintStatus.getResources().getColor(R.color.red));
                    fingerprintStatus.postDelayed(mResetFailTextRunnable, 1000);
                    SPUtils.getInstance("Login_Finger").put("isFinger",false);
                    listterner.process(false);
                    break;
                case 1:
                    LogUtils.i("handleMessage---验证成功");
                    fingerprintStatus.setText("登录成功");
                    fingerprintStatus.postDelayed(mResetSucceeTextRunnable, 1000);
                    SPUtils.getInstance("Login_Finger").put("isFinger",true);
                    listterner.process(true);
                    break;
                case 0:
                    fingerprintStatus.setText("登录失败\n请重试");
                    fingerprintStatus.setTextColor(fingerprintStatus.getResources().getColor(R.color.red));
                    fingerprintStatus.postDelayed(mResetErrorTextRunnable, 1000);
                    if(msg.arg1 == 5){
                        SPUtils.getInstance("Login_Finger").put("isFinger",false);
                        listterner.process(false);
                    }
                    break;

            }
        }
    };
    private Runnable mResetFailTextRunnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    private Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            fingerprintStatus.setText("指纹验证失败\n"+"请重新验证");
            fingerprintStatus.setTextColor(fingerprintStatus.getResources().getColor(R.color.green));
        }
    };


    private Runnable mResetSucceeTextRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO: 2018/5/17 验证成功之后的操作
            dismiss();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("指纹验证");
        View v = inflater.inflate(R.layout.dialog_fingerprint_1, container, false);

        initView(v);
        initEvent();
        startFingerPrint();
        return v;
    }

    private void initEvent() {
        tvPasswordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerPrintUtils.cancelCallback();
                dismiss();
            }
        });

    }

    /**
     * @函数名 - startFingerPrint
     * @功能 - 开始指纹验证
     * @参数 - 无
     * @返回值 - 无
     **/
    private void startFingerPrint() {
        FingerPrintUtils.init(getContext(), new FingerPrintUtils.FingerPrintResult() {
            @Override
            public void success() {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
                FingerPrintUtils.cancelCallback();
            }

            @Override
            public void error(int code, CharSequence info) {
                Message message = new Message();
                message.what = -1;
                message.arg1 = code;
                message.obj = info;
                mHandler.sendMessage(message);
                LogUtils.i("登录失败，关闭指纹识别功能\n" + "code=" + code + ",error info=" + info);
                FingerPrintUtils.cancelCallback();
            }

            @Override
            public void retry(int code, CharSequence info) {
                Message message = new Message();
                message.what = 0;
                message.arg1 = code;
                message.obj = info;
                mHandler.sendMessage(message);
                LogUtils.i("登录失败,请重试\n" + "code=" + code + ",retry info=" + info);
            }
        });
    }

    private void initView(View v) {
        fingerprintIcon = (ImageView) v.findViewById(R.id.fingerprint_icon);
        fingerprintStatus = (TextView) v.findViewById(R.id.fingerprint_status);
        tvPasswordLogin = (TextView) v.findViewById(R.id.tv_password_login);

    }

}
