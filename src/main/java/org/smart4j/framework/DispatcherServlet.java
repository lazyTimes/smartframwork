package org.smart4j.framework;

import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: smartframwork
 * @description: 请求分发器
 * @author: zhaoxudong
 * @create: 2019-09-09 23:07
 **/
@WebServlet(value = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {


    /**
     * 初始化
     *
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        // 1. 初始化所有的helper
        HelperLoader.init();
        // 2. 获取ServletContext （注册Servlet）
        ServletContext servletContext = servletConfig.getServletContext();
        // 3. 处理JSP的Servlet
        ServletRegistration registration = servletContext.getServletRegistration("jsp");
        registration.addMapping(ConfigHelper.getAppJspPath() + "*");
        // 4. 注册处理静态资源的默认Servlet
        ServletRegistration defualt = servletContext.getServletRegistration("default");
        defualt.addMapping(ConfigHelper.getAppAssetPath() + "*");

    }

    /**
     * 处理所有的请求
     *
     * @param req  请求对象
     * @param resp 返回对象
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求方法和请求路径
        String requestMethod = req.getMethod().toLowerCase();
        String pathInfo = req.getPathInfo();
        // 获取Action处理器
        Handler handler = ControllerHelper.getHandler(requestMethod, pathInfo);
        if (null != handler) {
            // 获取哦controller类以及bean实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);
            // 创建请求对象
            Map<String, Object> paramMap = new HashMap<String, Object>();
            Enumeration<String> parameterNames = req.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String paramValue = req.getParameter(paramName);
                paramMap.put(paramName, paramValue);


            }
            // 编解码处理流数据
            // TODO: 此部分的代码不清楚作用，需要实际启动项目测试
            String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if (MyStringUtil.isNotEmpty(body)) {
                String[] params = MyStringUtil.spliteString(body, "&");
                if (MyArrayUtil.isNotEmpty(params)) {
                    for (String param : params) {
                        String[] array = MyStringUtil.spliteString(param, "=");
                        if (MyArrayUtil.isNotEmpty(array) && array.length == 2) {
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName, paramValue);
                        }
                    }
                }
            }

            Param param = new Param(paramMap);
            // 调用Action 的方法
            Method actionMethod = handler.getActionMethod();
            Object result = RelfectionUtil.invokeMethod(controllerBean, actionMethod, param);
            // 处理返回值
            if (result instanceof View) {
                // 返回JSP 页面
                View view = (View) result;
                String path = view.getPath();
                if (MyStringUtil.isNotEmpty(path)) {
                    // 如果是从根路径开始，不设置上下文
                    if (path.startsWith("/")) {
                        resp.sendRedirect(req.getContextPath() + path);
                    } else {
                        // 如果存在参数，则根据上下文跳转页面
                        Map<String, Object> model = view.getModel();
                        for (Map.Entry<String, Object> stringObjectEntry : model.entrySet()) {
                            req.setAttribute(stringObjectEntry.getKey(), stringObjectEntry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
                    }
                }

            }else if(result instanceof Data){
                // 返回Json数据
                Data data = (Data) result;
                Object model = data.getModel();
                if( model != null ){
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter printWriter = resp.getWriter();
                    String toJson = JsonUtil.toJson(model);
                    printWriter.write(toJson);
                    printWriter.flush();
                    printWriter.close();

                }

            }


        }


    }
}
