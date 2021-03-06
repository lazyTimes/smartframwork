package org.smart4j.framework;

import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.helper.IocHelper;
import org.smart4j.framework.util.MyClassUtil;

/**
 * @program: smartframwork
 * @description: 加载相应的Helper类
 * @author: zhaoxudong
 * @create: 2019-09-09 22:45
 **/
public class HelperLoader {

    /**
     * 框架初始化
     */
    public static void init(){
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            MyClassUtil.loadClass(cls.getName(), true);
        }

    }
}
