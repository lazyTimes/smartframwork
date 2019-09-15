package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @program: smartframwork
 * @description: 处理流的工具类
 * @author: zhaoxudong
 * @create: 2019-09-10 00:34
 **/
public final class StreamUtil {

    /**
     * 日志操作
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

    /**
     * 输入流中获取字符串
     * @param inputStream 输入流
     * @return
     */
    public static String getString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("get String failure", e);
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }
}
