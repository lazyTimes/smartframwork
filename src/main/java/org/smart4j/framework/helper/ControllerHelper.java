package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;
import org.smart4j.framework.util.MyArrayUtil;
import org.smart4j.framework.util.MyCollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @program: smartframwork
 * @description: 控制器帮助类
 * @author: zhaoxudong
 * @create: 2019-09-09 22:19
 **/
public final class ControllerHelper {

    /**
     * 用于存放请求与处理器的映射关系
     */
    private static final Map<Request, Handler> ACTION_MAP =
            new HashMap<Request, Handler>();

    /**
     * 匹配请求路径的第一种
     */
//    private static final Pattern COMPILE_URL_ONE = Pattern.compile("\\w+:/\\w*");
    /**
     * 匹配请求路径的第二种
     */
    private static final Pattern COMPILE_URL_TWO = Pattern.compile("\\w+:\\w*");

    static {
        // 获取所有Controller
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if(MyCollectionUtil.isNotEmpty(controllerClassSet)){
            // 遍历这些Controller
            for (Class<?> controllerClass : controllerClassSet) {
                // 获取Controller 当中定义的方法
                Method[] declaredMethods = controllerClass.getDeclaredMethods();
                if(MyArrayUtil.isNotEmpty(declaredMethods)){
                    // 遍历所有的方法
                    for (Method method : declaredMethods) {
                        if(method.isAnnotationPresent(Action.class)){
                            // 从action 当中获取Url映射规则
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            if(isMatchCompileUrlOne(mapping)){
                                String[] split = mapping.split(":");
                                if(MyArrayUtil.isNotEmpty(split) && split.length == 2){
                                    // 获取请求方法和路径
                                    String requestMethod = split[0];
                                    String requestPath = split[1];
                                    // 如果没有 / 则进行添加
                                    if(!requestPath.startsWith("/")){
                                        requestPath = "/" + requestPath;
                                    }
                                    Request request = new Request(requestMethod, requestPath);
                                    Handler handler = new Handler(controllerClass, method);
                                    // 初始化Action Map
                                    ACTION_MAP.put(request, handler);
                                }
                            }
                            // 验证映射规则
//                            if(mapping.matches("\\w+:/\\w*")){
//                                String[] split = mapping.split(":");
//                                if(MyArrayUtil.isNotEmpty(split) && split.length == 2){
//                                    // 获取请求方法和路径
//                                    String requestMethod = split[0];
//                                    String requestPath = split[1];
//                                    Request request = new Request(requestMethod, requestPath);
//                                    Handler handler = new Handler(controllerClass, method);
//                                    // 初始化Action Map
//                                    ACTION_MAP.put(request, handler);
//                                }
//                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * 匹配请求路径的第一种或者第二种
     */
    private static boolean isMatchCompileUrlOne(String regex){
        return COMPILE_URL_TWO.matcher(regex).matches();
    }


    public static Handler getHandler(String requestMethod, String requestPath){
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
