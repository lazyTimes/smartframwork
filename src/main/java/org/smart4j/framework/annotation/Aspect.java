package org.smart4j.framework.annotation;

import java.lang.annotation.*;

/**
 * @program: smartframwork
 * @description: 切面注解
 * @author: zhaoxudong
 * @create: 2019-09-21 20:50
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 切面注解
     */
    Class<? extends Annotation> value();

}
