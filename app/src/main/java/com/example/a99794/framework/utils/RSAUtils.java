package com.example.a99794.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.util.Base64;

import com.blankj.utilcode.util.LogUtils;

public class RSAUtils {
    private static RSAUtils sRSAUtils;

    private RSAUtils() {
    }

    public static synchronized RSAUtils getRSAUtils() {
        if(sRSAUtils == null){
            sRSAUtils = new RSAUtils();
        }
        return sRSAUtils;
    }

    private static String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"; // 加密算法
    private static int KEYLENGTH = 2048;// 密钥位数
    private static int RESERVEBYTES = 11;// 加密block需要预留字节数
    private static int decryptBlock = KEYLENGTH / 8; // 每段解密block数，256 bytes
    private static int encryptBlock = decryptBlock - RESERVEBYTES; // 每段加密block数245bytes

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
            Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
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

    /**
     * 加密或解密数据的通用方法
     *
     * @param srcData 待处理的数据
     * @param key     公钥或者私钥
     * @param mode    指定是加密还是解密，值为Cipher.ENCRYPT_MODE或者Cipher.DECRYPT_MODE
     */
    public byte[] processDataByString(String srcData, Key key, int mode) {

        byte[] srcData1 = srcData.getBytes();
        //用来保存处理结果
        byte[] resultBytes = null;

        try {

            //构建Cipher对象，需要传入一个字符串，格式必须为"algorithm/mode/padding"或者"algorithm/",意为"算法/加密模式/填充方式"
            Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
            //初始化Cipher，mode指定是加密还是解密，key为公钥或私钥
            cipher.init(mode, key);
            //处理数据
            resultBytes = cipher.doFinal(srcData1);

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

    /** 使用公钥加密数据，结果用Base64转码 **/
    public String encryptDataByPublicKey(byte[] srcData, PublicKey publicKey) {

        byte[] resultBytes = processData(srcData, publicKey, Cipher.ENCRYPT_MODE);

        return Base64.encodeToString(resultBytes, Base64.DEFAULT);

    }

    /** 使用私钥解密，返回解码数据 **/
    public byte[] decryptDataByPrivate(String encryptedData, PrivateKey privateKey) {

        byte[] bytes = Base64.decode(encryptedData, Base64.DEFAULT);

        return processData(bytes, privateKey, Cipher.DECRYPT_MODE);
    }

    /** 使用私钥进行解密，解密数据转换为字符串，使用utf-8编码格式 **/
    public String decryptedToStrByPrivate(String encryptedData, PrivateKey privateKey) {
        return new String(decryptDataByPrivate(encryptedData, privateKey));
    }

    /** 使用私钥解密，解密数据转换为字符串，并指定字符集 **/
    public String decryptedToStrByPrivate(String encryptedData, PrivateKey privateKey, String charset) {
        try {

            return new String(decryptDataByPrivate(encryptedData, privateKey), charset);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**********************************************************************************************************************************************v*/

    /**
     * 获取公钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(key, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 获取私钥
     *
     * @param key pkcs#8
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(key, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static String getKeyString(Key key) {
        byte[] keyBytes = key.getEncoded();
        String s = Base64.encodeToString(keyBytes, Base64.DEFAULT);
        return s;
    }

    /**
     * 加密
     *
     * @param pubKey 公钥
     * @param data   加密报文 getBytes()
     * @return
     */
    public static String encode(String pubKey, byte[] data) {
        // 计算分段加密的block数 (向上取整)
        int nBlock = (data.length / encryptBlock);
        if ((data.length % encryptBlock) != 0) { // 余数非0，block数再加1
            nBlock += 1;
        }
        // 输出buffer, 大小为nBlock个decryptBlock
        ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * decryptBlock);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            PublicKey publicKey = getPublicKey(pubKey);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            for (int offset = 0; offset < data.length; offset += encryptBlock) {
                // block大小: encryptBlock 或剩余字节数
                int inputLen = (data.length - offset);
                if (inputLen > encryptBlock) {
                    inputLen = encryptBlock;
                }
                // 得到分段加密结果
                byte[] encryptedBlock = cipher.doFinal(data, offset, inputLen);
                // 追加结果到输出buffer中
                outbuf.write(encryptedBlock);
            }
            return Base64.encodeToString(outbuf.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                outbuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解密
     *
     * @param priKey pkcs#8
     * @param data   解密报文 "string".getBytes();
     * @return new String( byte[] )
     */
    public static byte[] decode(String priKey, byte[] data) {
        // 计算分段加密的block数 (向上取整)
        int nBlock = (data.length / encryptBlock);
        if ((data.length % encryptBlock) != 0) { // 余数非0，block数再加1
            nBlock += 1;
        }
        ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * encryptBlock);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            PrivateKey privateKey = getPrivateKey(priKey);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            for (int offset = 0; offset < data.length; offset += decryptBlock) {
                // block大小: decryptBlock 或剩余字节数
                int inputLen = (data.length - offset);
                if (inputLen > decryptBlock) {
                    inputLen = decryptBlock;
                }
                // 得到分段加密结果
                byte[] decryptedBlock = cipher.doFinal(data, offset, inputLen);
                // 追加结果到输出buffer中
                outbuf.write(decryptedBlock);
            }
            return outbuf.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                outbuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 普通解密
     *
     * @param key
     * @param data
     * @return new String()
     */
    public static byte[] decode(String key, String data) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            PrivateKey privateKey = getPrivateKey(key);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(Base64.decode(data.getBytes(), Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 普通加密
     *
     * @param key
     * @param data getBytes()
     * @return Bsae64.enCodeToString()
     */
    public static byte[] encode2(String key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            PublicKey publicKey = getPublicKey(key);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}