package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * 类加载工具类
 */
public final class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);


    /**
     * @Description: 获取类加载器
     * @Param: 无参数
     * @return: 类加载器
     * @Author: zhaoxudong
     * @Date: 2019.9.8
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * @param className     类名
     * @param isInitialized 是否初始化
     * @return 实体类
     * @Description: 加载class类
     * @Author zhaoxudong
     * @Date 2019.9.8
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure", e);
            throw new RuntimeException(e);
        }
        return cls;
    }


    /**
     * 获取指定包名下面的所有类
     * 最为复杂也是最为核心的代码
     *
     * @param packageName 包名
     */
    public static Set<Class<?>> getClassesSet(String packageName) {
        // 1. 创建set集合
        Set<Class<?>> result = new HashSet<Class<?>>();
        // 2. 获取枚举迭代器，使用递归的方式迭代所有的包
        try {
            Enumeration<URL> enumeration = getClassLoader().getResources(packageName.replace(".", "/"));
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                if (null != url) {
                    // protocol 协议，获取协议
                    String protocool = url.getProtocol();
                    // 如果是一个文件
                    if (protocool.equals("file")) {
                        // 替换掉特殊符号
                        String urlPath = url.getPath().replaceAll("%20", "");
                        addClass(result, urlPath, packageName);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("get class set failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 将对应类型的文件路径加入到对应的set内部
     *
     * @param result
     * @param urlPath
     * @param packageName
     */
    private static void addClass(Set<Class<?>> result, String urlPath, final String packageName) {
        //对于文件路径进行过滤操作
        File[] files = new File(urlPath).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                // 如果是class 文件或者文件夹，允许
                return (pathname.isFile() && packageName.endsWith(".class")) || pathname.isDirectory();
            }
        });
        for (File file : files) {
            String fileName = file.getName(); // 文件名
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if(MyStringUtil.isNotEmpty(className)){
                    className = packageName + "." + className;
                }
                doAddClass(result, className);

            }else{
                // 如果不是文件，则根据路径加载文件
                String subPackPath = fileName;
                if(MyStringUtil.isNotEmpty(subPackPath)){
                    subPackPath = urlPath + "/" + subPackPath;
                }
                String subPackName = fileName;
                if(MyStringUtil.isNotEmpty(subPackPath)){
                    subPackName = urlPath + "." + subPackName;
                }
                // 递归获取子包下面的文件
                addClass(result, subPackPath, subPackName);
            }
        }
    }

    /**
     * 添加类名的统一方法
     * 主要为根据包名加载类
     * @param result 类集合
     * @param className 类名
     */
    private static void doAddClass(Set<Class<?>> result, String className) {
        Class<?> cls = loadClass(className, false);
        result.add(cls);
    }


}



