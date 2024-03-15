package com.data.utils;


import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Md5Util {

    //记录日志信息
    private static final Logger log = LoggerFactory.getLogger(Md5Util.class);

    // 实现一个md5加解密
    public final static String md5Key = "abcabcdefhghijklnmoqprstuvwvxyz";

    /**
     *
     * @param strPwd 明文密码
     * @return 密文
     * @throws Exception
     */
    public static String md5(String strPwd) throws Exception{
        try {
            // 获取加密后的字符串
            String encodeStr = DigestUtils.md5Hex(strPwd+md5Key);
            return encodeStr;
        }catch (Exception e){
            log.error("MD5 Error...\", e");
        }
        return null;
    }

    /**
     * 用户登录，密码验证
     * @param pwdStr 明文字符串
     * @param md5Str 密文字符串
     * @return
     */
    public static boolean passwordVerify(String pwdStr,String md5Str) throws Exception {
        String md5Pwd= md5(pwdStr);
        if (md5Pwd.equalsIgnoreCase(md5Str)){
            return true;
        }
        return false;
    }
}