package com.data.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Jackson 解析库工具类
 * @Author zhangr132
 * @Date 2024/4/3 15:55
 * @注释
 */
public class JacksonUtil {
    // 创建 ObjectMapper 对象，用于序列化和反序列化 JSON 数据
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 将 Java 对象转换为 JSON 字符串
     * @param obj 要转换的 Java 对象
     * @return 转换后的 JSON 字符串
     */
    public static String bean2Json(Object obj) {
        try {
            // 使用 ObjectMapper 的 writeValueAsString 方法将 Java 对象转换为 JSON 字符串
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // 如果转换过程中出现异常，则打印异常信息并返回 null
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为 Java 对象
     * @param jsonStr 要转换的 JSON 字符串
     * @param objClass 目标 Java 对象的类
     * @param <T> 目标 Java 对象的类型
     * @return 转换后的 Java 对象
     */
    public static <T> T json2Bean(String jsonStr, Class<T> objClass) {
        try {
            // 使用 ObjectMapper 的 readValue 方法将 JSON 字符串转换为 Java 对象
            return mapper.readValue(jsonStr, objClass);
        } catch (IOException e) {
            // 如果转换过程中出现异常，则打印异常信息并返回 null
            e.printStackTrace();
            return null;
        }
    }
}
