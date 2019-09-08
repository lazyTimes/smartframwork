package org.smart4j.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program smartframwork
 * @description Action 方法注解
 * @author zhaoxudong
 * @Date 2019-09-08 19:57
 **/
// 注解类型
@Target(ElementType.METHOD)
// 生效周期
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    /**
     * 请求路径与类型 "get:/xxx" 使用此格式
     * @return
     */
    String value();
}
