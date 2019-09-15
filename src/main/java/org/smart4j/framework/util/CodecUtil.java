package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @program: smartframwork
 * @description: 编解码的工具类
 * @author: zhaoxudong
 * @create: 2019-09-10 00:33
 **/
public final class CodecUtil {

    /**
     * 日志操作
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CodecUtil.class);

    /**
     * URL 解码
     * @param source 资源
     * @return
     */
    public static String encodeURL(String source){
        String target = null;

        try {
            target = URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("encode failure",e);
            throw new RuntimeException(e);
        }
        return target;
    }

    /**
     * URL 解码
     */
    public static String decodeURL(String source){
        String targer;

        try {
            targer = URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("decode failure",e);
            throw new RuntimeException(e);
        }
        return targer;
    }
}
