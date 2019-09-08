package org.smart4j.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: smartframwork
 * @description: Framework控制器注解
 * @author: zxd
 * @create: 2019-09-08 19:53
 **/
// 注解类型
@Target(ElementType.TYPE)
// 生效周期
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {



}
