package com.jiahe.iot.common.util;

import com.jiahe.iot.common.constant.Constants;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptionUtil {
    // 编码方式
    public static final String CODE_TYPE = "UTF-8";
    // 填充类型
    public static final String AES_TYPE = "AES/ECB/PKCS5Padding";

    // AES固定格式为128/192/256bits.即：16/24/32bytes。DES固定格式为128bits，即8bytes。
    // 字符补全
    private static final String[] consult = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B",
            "C", "D", "E", "F", "G"};

    /**
     * 加密
     *
     * @param cleartext
     * @return
     */
    public static String encrypt(String cleartext, String aesKey) {
        // 加密方式： AES128(CBC/PKCS5Padding) + Base64, 私钥：1111222233334444
        try {
            // 两个参数，第一个为私钥字节数组， 第二个为加密方式 AES或者DES
            SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(), "AES");
            // 实例化加密类，参数为加密方式，要写全
            Cipher cipher = Cipher.getInstance(AES_TYPE); // PKCS5Padding比PKCS7Padding效率高，PKCS7Padding可支持IOS加解密
            // 初始化，此方法可以采用三种方式，按加密算法要求来添加。（1）无第三个参数（2）第三个参数为SecureRandom random = new
            // SecureRandom();中random对象，随机数。(AES不可采用这种方法)（3）采用此代码中的IVParameterSpec
            // 加密时使用:ENCRYPT_MODE; 解密时使用:DECRYPT_MODE;
            cipher.init(Cipher.ENCRYPT_MODE, key); // CBC类型的可以在第三个参数传递偏移量zeroIv,ECB没有偏移量
            // 加密操作,返回加密后的字节数组，然后需要编码。主要编解码方式有Base64, HEX, UUE,7bit等等。此处看服务器需要什么编码方式
            byte[] encryptedData = cipher.doFinal(cleartext.getBytes(CODE_TYPE));

            return new BASE64Encoder().encode(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解密
     *
     * @param encrypted
     * @return
     */
    public static String decrypt(String encrypted, String aesKey) {
        try {
            byte[] byteMi = new BASE64Decoder().decodeBuffer(encrypted);
            SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            // 与加密时不同MODE:Cipher.DECRYPT_MODE
            cipher.init(Cipher.DECRYPT_MODE, key); // CBC类型的可以在第三个参数传递偏移量zeroIv,ECB没有偏移量
            byte[] decryptedData = cipher.doFinal(byteMi);
            return new String(decryptedData, CODE_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) throws Exception {

        String encrypt = encrypt("aaaaaaaaaaaaaaaaaaaaa", Constants.passwordAesKey);
        System.out.println("encrypt:" + encrypt);

        String decrypt = decrypt(encrypt, Constants.passwordAesKey);
        System.out.println("decrypt:" + decrypt);
    }
}

