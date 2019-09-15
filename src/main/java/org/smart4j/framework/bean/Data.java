package org.smart4j.framework.bean;

/**
 * @program: smartframwork
 * @description: 返回数据对象
 * @author: zhaoxudong
 * @create: 2019-09-09 23:04
 **/
public class Data {

    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
