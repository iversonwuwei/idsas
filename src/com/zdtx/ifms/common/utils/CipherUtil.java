package com.zdtx.ifms.common.utils;

import java.security.MessageDigest;

import org.apache.tomcat.util.codec.binary.Base64;

/**
 * @ClassName: CipherUtil
 * @Description: MD5加密,2013-7-23新增base64编解码
 * @author Leon Liu
 * @date 2012-9-11 下午4:18:35
 * @version V1.0
 */
public class CipherUtil {
	
	private final static String[] hexDigits = {"0", "1", "2", "3", "4",    
        "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};    
        
	/**
	 * 將inputString加密
	 * @param inputString	原文
	 * @return 密文
	 */
    public static String generatePassword(String inputString){    
        return encodeByMD5(inputString);    
    }    

    /**
     * 验证加密前后字符串是否符合
     * @param password	密文
     * @param inputString	原文
     * @return
     */
    public static Boolean validatePassword(String password, String inputString){
    	return password.equals(encodeByMD5(inputString));
    }
    
    /**
     * 解密
     * @param originString
     * @return
     */
    public static String returnEncodeByMde(String originString){  
        return encodeByMD5(originString);  
    }
    
    /**
     * MD5加密    
     * @param originString	原文
     * @return	密文
     */
    private static String encodeByMD5(String originString) {
    	if (null == originString) {
        	return null;
        }
    	try {
    		//获得MD5摘要
    		MessageDigest md = MessageDigest.getInstance("MD5");
    		//使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
    		byte[] results = md.digest(originString.getBytes());
    		//将得到的字节数组变成16进制字符串返回
    		String resultString = byteArrayToHexString(results);
    		String pass =  resultString.toUpperCase();
    		return pass;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		return null;
    	}
    }

    /**
     * 字节数组转16进制数字字符串
     * @param b	字节数组
     * @return
     */
    private static String byteArrayToHexString(byte[] b){
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 字节转16进制数字字符串
     * @param b	字节
     * @return
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;   
        int d2 = n % 16;    
        return hexDigits[d1] + hexDigits[d2];    
    }
    
    /**
     * base64编码
     * @param original 原文
     * @return	String 密文
     */
	public static String encodeBase64(String original) {
        return Base64.encodeBase64String(original.getBytes());
    }
    
    /**
     * base64解码
     * @param ciphertext 密文
     * @return	String	原文
     */
	public static String decodeBase64(String ciphertext) {
		return new String(Base64.decodeBase64(ciphertext));
	}
}