package com.jiahe.iot.common.util;//package com.jiahe.login.util;
//
////import org.apache.commons.codec.binary.Base64;
////import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.security.Provider;
//import java.security.SecureRandom;
//import java.security.Security;
//import java.util.Arrays;
//
//
//public class AESUtils {
//
//    private final static Logger logger = LoggerFactory.getLogger(AESUtils.class);
//
//    /**
//     * 使用PKCS7Padding填充必须添加一个支持PKCS7Padding的Provider
//     * 类加载的时候就判断是否已经有支持256位的Provider,如果没有则添加进去
//     */
//    static {
//        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
//            System.out.println("=====================");
//            Security.addProvider(new BouncyCastleProvider());
//        }
//    }
//
//
//    public static byte[] aesEncrypt(String content, String pkey) {
//        try {
//            SecretKeySpec skey = new SecretKeySpec(pkey.getBytes(), "AES");
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");// "算法/加密/填充"
//            cipher.init(Cipher.ENCRYPT_MODE, skey);//初始化加密器
//            byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
//            return encrypted; // 加密
//        } catch (Exception e) {
//            logger.info("aesEncrypt() method error:", e);
//        }
//        return null;
//    }
//    private static SecretKey generateKey(String secretKey) throws Exception {
//        //防止linux下 随机生成key
//        Provider p = Security.getProvider("SUN");
//        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", p);
//        secureRandom.setSeed(secretKey.getBytes());
//        KeyGenerator kg = KeyGenerator.getInstance("AES");
//        kg.init(secureRandom);
//        // 生成密钥
//        return kg.generateKey();
//    }
//
//    /**
//     * @param content 加密前原内容
//     * @param pkey    长度为16个字符,128位
//     * @return base64EncodeStr   aes加密完成后内容
//     * @throws
//     * @Title: aesEncryptStr
//     * @Description: aes对称加密
//     */
//    public static String aesEncryptStr(String content, String pkey) {
//        byte[] aesEncrypt = aesEncrypt(content, pkey);
//        System.out.println("加密后的byte数组:" + Arrays.toString(aesEncrypt));
//        String base64EncodeStr = Base64.encodeBase64String(aesEncrypt);
//        System.out.println("加密后 base64EncodeStr:" + base64EncodeStr);
//        return base64EncodeStr;
//    }
//
//    /**
//     * @param content base64处理过的字符串
//     * @param pkey    密匙
//     * @return String    返回类型
//     * @throws Exception
//     * @throws
//     * @Title: aesDecodeStr
//     * @Description: 解密 失败将返回NULL
//     */
//    public static String aesDecodeStr(String content, String pkey) throws Exception {
//        try {
//            System.out.println("待解密内容:" + content);
//            byte[] base64DecodeStr = Base64.decodeBase64(content);
//            System.out.println("base64DecodeStr:" + Arrays.toString(base64DecodeStr));
//            byte[] aesDecode = aesDecode(base64DecodeStr, pkey);
//            System.out.println("aesDecode:" + Arrays.toString(aesDecode));
//            if (aesDecode == null) {
//                return null;
//            }
//            String result;
//            result = new String(aesDecode, "UTF-8");
//            System.out.println("aesDecode result:" + result);
//            return result;
//        } catch (Exception e) {
//            throw new Exception("解密异常");
//        }
//    }
//
//    /**
//     * 解密 128位
//     *
//     * @param content 解密前的byte数组
//     * @param pkey    密匙
//     * @return result  解密后的byte数组
//     * @throws Exception
//     */
//    public static byte[] aesDecode(byte[] content, String pkey) throws Exception {
//        SecretKeySpec skey = new SecretKeySpec(pkey.getBytes(), "AES");
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");// 创建密码器
//        cipher.init(Cipher.DECRYPT_MODE, skey);// 初始化解密器
//        byte[] result = cipher.doFinal(content);
//        return result; // 解密
//    }
//
//
//    public static void main(String[] args) throws Exception {
//
//
////        String content = "5C30E342C083.dbJdBMgSefOX95qet3.QFUysTdfKQ9eYbge.1601188210457";
//////        //密匙
////        String pkey = "QFUysTdfKQ9eYbge";
////        System.out.println("待加密报文:" + content);
////        System.out.println("密匙:" + pkey);
////        String aesEncryptStr = aesEncryptStr(content, pkey);
////        System.out.println("加密报文:" + aesEncryptStr);
////
////        aesEncryptStr = "rA5+ebu0JQ5hETjEfYRuxo1DkXAyoloYCQorGoP48IK9AwCtvcCMMBa7RBl1kj9G74ZdggEU1g6yu8vYM/mc0Q==";
////        String aesDecodeStr = aesDecodeStr(aesEncryptStr, pkey);
////        System.out.println("解密报文:" + aesDecodeStr);
//////        System.out.println("加解密前后内容是否相等:" + aesDecodeStr.equals(content));
//
//        String pkey = "ec21405e468d17d629229b38edbb0c98";
//
//        String aesEncryptStr = aesEncryptStr("aaaaa",pkey);
//
//        System.out.println("aesEncryptStr:"+aesEncryptStr);
//    }
//}
//
//
//
