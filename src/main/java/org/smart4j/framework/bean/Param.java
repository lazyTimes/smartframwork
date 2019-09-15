package org.smart4j.framework.bean;

import org.smart4j.framework.util.CastUtil;

import java.util.Map;

/**
 * @program: smartframwork
 * @description: 请求参数对象
 * @author: zhaoxudong
 * @create: 2019-09-09 22:56
 **/
public class Param {

    /**
     * 请求对象
     */
    private Map<String, Object> paramMap;


    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 根据参数名获取long 型参数值
     * @param name 参数名
     * @return
     */
    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }

    /**
     * 获取所有字段信息
     */
    public Map<String, Object> getParamMap() {
        return paramMap;
    }
}
