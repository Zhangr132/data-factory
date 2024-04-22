package com.data.utils;

import java.util.Random;

/**
 * 验证码工具类
 * 生成验证码
 * @Author zhangr132
 * @Date 2024/4/19 17:14
 * @注释
 */

public class CodeUtils {
    public static void main(String[] args) {
        String s = creatCode(4);
        System.out.println("随机验证码为：" + s);

    }

    //定义一个方法返回一个随机验证码
    public static String creatCode(int n) {

        String code = "";
        Random r = new Random();
        //2.在方法内部使用for循环生成指定位数的随机字符，并连接起来
        for (int i = 0; i <= n; i++) {
            //生成一个随机字符：大写 ，小写 ，数字（0  1  2）
            int type = r.nextInt(3);
            switch (type) {
                case 0:
                    char ch = (char) (r.nextInt(26) + 65);
                    code += ch;
                    break;
                case 1:
                    char ch1 = (char) (r.nextInt(26) + 97);
                    code += ch1;
                    break;
                case 2:
                    code +=  r.nextInt(10);
                    break;
            }

        }
        return code;
    }
}

