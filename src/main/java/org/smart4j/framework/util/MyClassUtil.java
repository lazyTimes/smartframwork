package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类加载工具类
 */
public final class MyClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyClassUtil.class);


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
                    } else if (protocool.equals("jar")) {
                        // 如果是jar包，获取jar包路径
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (null != jarURLConnection) {
                            // 获取jar包文件
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (null != jarFile) {
                                Enumeration<JarEntry> entries = jarFile.entries();
                                // 枚举迭代获取所有的jar包类
                                while (entries.hasMoreElements()) {
                                    JarEntry jarEntry = entries.nextElement();
                                    // 获取名称
                                    String entryName = jarEntry.getName();
                                    if (entryName.equals(".class")) {
                                        String className = entryName.substring(0, entryName.lastIndexOf("."))
                                                .replaceAll("/", ".");
                                        doAddClass(result, className);
                                    }
                                }
                            }
                        }
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
     * @param classSet      所有的应用宝
     * @param packagePath 文件路径
     * @param packageName 包名
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        //对于文件路径进行过滤操作
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                // 如果是class 文件或者文件夹，允许
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        for (File file : files) {
            // 当前遍历目录的文件名
            String fileName = file.getName();
            // 如果是文件类型
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (MyStringUtil.isNotEmpty(className)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);

            } else {
                // 如果不是文件，则根据路径加载文件
                String subPackagePath = fileName;
                if (MyStringUtil.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (MyStringUtil.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                // 递归获取子包下面的文件
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    /**
     * 添加类名的统一方法
     * 主要为根据包名加载类
     *
     * @param result    类集合
     * @param className 类名
     */
    private static void doAddClass(Set<Class<?>> result, String className) {
        Class<?> cls = loadClass(className, false);
        result.add(cls);
    }


}



