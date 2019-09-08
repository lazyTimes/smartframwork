package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 属性文件工具类
 */
public final class PropsUtil {
    /**
     * 日志记录
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     * @param fileName
     * @return
     */
    public static Properties loadProps(String fileName){
        Properties properties = null;
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(inputStream == null){
                throw new FileNotFoundException(fileName +" file is not found");
            }
            properties = new Properties();
            properties.load(inputStream);
        }catch (IOException io){
            LOGGER.error("loadProps error",io);
            throw new RuntimeException(io);
        }finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (IOException e){
                    LOGGER.error("close input stream failure", e);
                }
            }
        }
        return properties;
    }

    /**
     * 获取属性字符串
     * @param properties 键值对信息
     * @param key 键
     * @param defualtValue 默认值
     * @return 结果
     */
    public static String getString(Properties properties, String key, String defualtValue){
        String value = defualtValue;
        if(properties.containsKey(key)){
            value = properties.getProperty(key);
        }
        return value;
    }

    /**
     * 获取字符属性，默认为0
     * @param properties 键值对信息
     * @param key 键
     * @return 结果
     */
    public static String getString(Properties properties, String key){
        return getString(properties, key, "");
    }

    /**
     * 获取整型内容
     * @param properties 键值对信息
     * @param key 键
     * @return
     */
    public static int getInt(Properties properties, String key){
        return getInt(properties, key, 0);
    }

    /**
     * 获取整型，带有默认值
     * @param properties 键值对信息
     * @param key 键
     * @param defualtValue 默认值
     * @return
     */
    public static int getInt(Properties properties, String key, int defualtValue){
        int value = defualtValue;
        if(properties.containsKey(key)){
            value = CastUtil.castInt(properties.getProperty(key));
        }
        return value;
    }

    /**
     * 获取boolean返回值
     * @param properties 键值对信息
     * @param key 键
     * @return
     */
    public static boolean getBoolean(Properties properties, String key){
        return getBoolean(properties, key, false);
    }

    /**
     * 获取boolean返回值
     * @param properties 键值对信息
     * @param key 键
     * @param defualtValue 默认值
     * @return
     */
    public static boolean getBoolean(Properties properties, String key, boolean defualtValue){
        boolean value = defualtValue;
        if(properties.containsKey(key)){
            value = CastUtil.castBoolean(properties.getProperty(key));
        }
        return value;
    }
}
