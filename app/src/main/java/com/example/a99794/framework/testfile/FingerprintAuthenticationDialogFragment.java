/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.example.a99794.framework.testfile;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.a99794.framework.R;
import com.example.a99794.framework.utils.FingerprintUtil;
import com.example.a99794.framework.utils.UUIDUtil;
import com.example.a99794.framework.view.activity.ThirdActivity;
import com.jakewharton.rxbinding.view.RxView;
import com.yzq.zxinglibrary.common.Constant;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * A dialog which uses fingerprint APIs to authenticate the user, and falls back to password
 * authentication if fingerprint is not available.
 */
public class FingerprintAuthenticationDialogFragment extends DialogFragment {
    private FingerprintUtil fingerprintUtil;    //指纹工具类

    private Button cancelButton, secondDialogButton;
    private EditText mPassword;
    private RelativeLayout fingerprintContainer, backupContainer;
    private TextView fingerprintDescription, fingerprintStatus;
    private ImageView fingerprintIcon;
    private ThirdActivity mActivity;

    private Stage mStage = Stage.FINGERPRINT;
    private FingerprintManager.CryptoObject mCryptoObject;
    private int flag = 5;   //验证次数

    private static String SECRET_MESSAGE = "Very secret message";
    private static final String KEY_NAME_NOT_INVALIDATED = "key_not_invalidated";
    public static final String DEFAULT_KEY_NAME = "default_key";

    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    private SharedPreferences mSharedPreferences;
    private Cipher defaultCipher;

    // 向Activity传值的接口
    public interface FragmentInteraction {
        void process(String str);
    }

    private FragmentInteraction listterner;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    LogUtils.i("handleMessage---多次失败");
                    goToPassword();
                    break;
                case 1:
                    LogUtils.i("handleMessage---成功");
                    fingerprintIcon.setImageResource(R.drawable.ic_fingerprint_success);
                    fingerprintStatus.setText("成功");
                    fingerprintStatus.setTextColor(fingerprintStatus.getResources().getColor(R.color.success_color));
                    fingerprintStatus.postDelayed(mResetSucceeTextRunnable, 1600);
                    break;
                case 0:
                    flag--;
                    if (flag != 0) {
                        ToastUtils.showShort("指纹识别失败，还剩下" + flag + "次机会");
                        LogUtils.i("指纹识别失败，还剩下" + flag + "次机会");
                    }
                    fingerprintIcon.setImageResource(R.drawable.ic_fingerprint_error);
                    fingerprintStatus.setText("失败");
                    fingerprintStatus.setTextColor(fingerprintStatus.getResources().getColor(R.color.red));
                    fingerprintStatus.postDelayed(mResetErrorTextRunnable, 1600);
                    if (flag == 0) {
                        listterner.process(TimeUtils.getNowString());
                    }
                    break;

            }
        }
    };

    private Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            fingerprintStatus.setText("重新验证");
            fingerprintStatus.setTextColor(fingerprintStatus.getResources().getColor(R.color.grey));
            fingerprintIcon.setImageResource(R.mipmap.ic_fp_40px);
        }
    };

    // TODO: 2018/5/17 验证成功之后的操作
    private Runnable mResetSucceeTextRunnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (ThirdActivity) getActivity();
        listterner = (FragmentInteraction) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SECRET_MESSAGE = UUIDUtil.getMyUUID(mActivity);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        initKeyStore();
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void initKeyStore() {
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }

        try {
            defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);

        createKey(DEFAULT_KEY_NAME, true);
        createKey(KEY_NAME_NOT_INVALIDATED, false);

        if (initCipher(defaultCipher, DEFAULT_KEY_NAME)) {

            mCryptoObject = new FingerprintManager.CryptoObject(defaultCipher);

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean initCipher(Cipher cipher, String keyName) {

        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(keyName, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void createKey(String keyName, boolean invalidatedByBiometricEnrollment) {

        try {
            mKeyStore.load(null);
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
            }
            mKeyGenerator.init(builder.build());
            mKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("登陆验证");
        View v = inflater.inflate(R.layout.dialog_fingerprint, container, false);

        initView(v);
        initEvent();
        startFinger(v);

        return v;
    }

    //把传递进来的activity对象释放掉
    @Override
    public void onDetach() {
        super.onDetach();
        listterner = null;
    }

    private void initEvent() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStage == Stage.FINGERPRINT) {
                    fingerprintUtil.closeFinger();
                    dismiss();
                } else if (mStage == Stage.PASSWORD) {
                    mPassword.setText("");
                    dismiss();
                }
            }
        });

        secondDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStage == Stage.FINGERPRINT) {
                    goToPassword();
                } else if (mStage == Stage.PASSWORD) {
                    LogUtils.i("输入密码是：" + mPassword.getText().toString());
                    dismiss();
                }
            }
        });

    }

    private void goToPassword() {
        cancelButton.setText("Cancel");
        secondDialogButton.setText("OK");
        fingerprintContainer.setVisibility(View.GONE);
        backupContainer.setVisibility(View.VISIBLE);

        mStage = Stage.PASSWORD;
        mPassword.requestFocus();
        //mPassword.postDelayed(mShowKeyboardRunnable, 500);
        fingerprintUtil.closeFinger();

    }

    /**
     * @函数名 - startFinger
     * @功能 - 开始指纹验证
     * @参数 - view
     * @返回值 - 无
     **/
    @SuppressLint("NewApi")
    private void startFinger(final View view) {
        fingerprintUtil = new FingerprintUtil();
        if (fingerprintUtil.initFinger(view.getContext())) {
            fingerprintUtil.startListening(mCryptoObject, new FingerprintManager.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    //多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
                    Message message = new Message();
                    message.what = -1;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);

                    assert mCryptoObject != null;
                    tryEncrypt(mCryptoObject.getCipher());
                }

                @Override
                public void onAuthenticationFailed() {
                    Message message = new Message();
                    message.what = 0;
                    mHandler.sendMessage(message);
                }
            });
        }
    }

    /**
     * @函数名 - tryEncrypt
     * @功能 - 加密信息
     * @参数 - cipher
     * @返回值 - 无
     **/
    private void tryEncrypt(Cipher cipher) {
        try {
            byte[] encrypted = cipher.doFinal(SECRET_MESSAGE.getBytes());
            sendConfirmation(encrypted);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            LogUtils.e("Failed to encrypt the data with the generated key." + e.getMessage());
        }
    }

    /**
     * @函数名 - sendConfirmation
     * @功能 - 发送加密信息请求登陆
     * @参数 - encrypted
     * @返回值 - 无
     **/
    private void sendConfirmation(byte[] encrypted) {
        if (encrypted != null) {
            // TODO: 2018/5/18 发送加密后的识别码
            LogUtils.e(Base64.encodeToString(encrypted, 0 /* flags */));
        }
    }

    private void initView(View v) {
        fingerprintContainer = v.findViewById(R.id.fingerprint_container);
        backupContainer = v.findViewById(R.id.backup_container);

        fingerprintDescription = v.findViewById(R.id.fingerprint_description);
        fingerprintIcon = v.findViewById(R.id.fingerprint_icon);
        fingerprintStatus = v.findViewById(R.id.fingerprint_status);
        cancelButton = v.findViewById(R.id.cancel_button);
        secondDialogButton = v.findViewById(R.id.second_dialog_button);

        mPassword = v.findViewById(R.id.password);
    }

    public enum Stage {
        FINGERPRINT,
        NEW_FINGERPRINT_ENROLLED,
        PASSWORD
    }
}
