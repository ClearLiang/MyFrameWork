package com.example.a99794.framework.utils;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @作者 ClearLiang
 * @日期 2018/5/4
 * @描述 RSA加解密工具
 **/

public class RSAUtil {
    private static RSAUtil sRSAUtil;

    private RSAUtil() {
    }

    public static synchronized RSAUtil getRSAUtil() {
        if(sRSAUtil == null){
            sRSAUtil = new RSAUtil();
        }
        return sRSAUtil;
    }

    private static String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"; // 加密算法
    private static int KEYLENGTH = 2048;// 密钥位数

    private KeyPair keyPair = null;

    /**
     * 产生密钥对
     * 密钥长度，小于1024长度的密钥已经被证实是不安全的，通常设置为1024或者2048，建议2048
     */
    public void generateRSAKeyPair() {

        try {

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            //设置密钥长度
            keyPairGenerator.initialize(KEYLENGTH);
            //产生密钥对
            keyPair = keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    /** 获取公钥 **/
    public PublicKey getPublicKey(){
        return keyPair.getPublic();
    }

    /** 获取私钥 **/
    public PrivateKey getPrivateKey(){
        return keyPair.getPrivate();
    }

    /**
     * 加密或解密数据的通用方法
     *
     * @param srcData 待处理的数据
     * @param key     公钥或者私钥
     * @param mode    指定是加密还是解密，值为Cipher.ENCRYPT_MODE或者Cipher.DECRYPT_MODE
     */
    public byte[] processData(byte[] srcData, Key key, int mode) {
        //用来保存处理结果
        byte[] resultBytes = null;
        try {
            //构建Cipher对象，需要传入一个字符串，格式必须为"algorithm/mode/padding"或者"algorithm/",意为"算法/加密模式/填充方式"
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            //初始化Cipher，mode指定是加密还是解密，key为公钥或私钥
            cipher.init(mode, key);
            //处理数据
            resultBytes = cipher.doFinal(srcData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return resultBytes;
    }

    /*******************************************************************************************************************************/


    /**
     * @函数名 - myEncrypt
     * @功能   - 公钥加密
     * @参数   - srcData 加密数据
     * @返回值 - 加密后的字符串
     **/
    public String myEncrypt(String srcData) {

        Cipher cipher = null;
        byte[] output = null;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
            output = cipher.doFinal(srcData.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(output, Base64.DEFAULT);

    }

    /**
     * @函数名 - myDecryption
     * @功能   - 私钥解密
     * @参数   - srcData 已加密的数据
     * @返回值 - 解密后的字符串
     **/
    public String myDecryption(String srcData) {

        Cipher cipher = null;
        byte[] output = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
            output = cipher.doFinal(Base64.decode(srcData, Base64.DEFAULT));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return new String(output);

    }

}