package com.example.a99794.framework.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.security.PublicKey;

import javax.crypto.Cipher;


public class KeyGenUtil {

    /**
     * 密码生成，递归实现
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    static Cipher createCipher() throws Exception {
        RSAUtil.getRSAUtil().generateRSAKeyPair();
        PublicKey key = RSAUtil.getRSAUtil().getPublicKey();
        Cipher cipher = Cipher.getInstance("RSA");

        cipher.init(Cipher.ENCRYPT_MODE | Cipher.DECRYPT_MODE, key);
        return cipher;
    }
}
