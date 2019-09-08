package org.smart4j.framework.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.MyCollectionUtil;
import org.smart4j.framework.util.PropsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库工具类
 */
public class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    /**
     * dbUtil 工具类
     */
    private static final QueryRunner QUERY_RUNNER;

    /**
     * 当前线程
     */
    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL;
    /**
     * 数据库连接池
     */
    private static final BasicDataSource DATA_SOURCE;


    /**
     * jdbc
     */
    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    /**
     * 静态初始化获取jdbc的相关内容
     */
    static {
        CONNECTION_THREAD_LOCAL = new ThreadLocal<Connection>();

        QUERY_RUNNER = new QueryRunner();



        Properties properties = PropsUtil.loadProps("config.properties");
        DRIVER = properties.getProperty("jdbc.driver");
        URL = properties.getProperty("jdbc.url");
        USERNAME = properties.getProperty("jdbc.username");
        PASSWORD = properties.getProperty("jdbc.password");
        // 使用数据库连接池进行管理
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);

//        try {
//            Class.forName(DRIVER);
//        } catch (ClassNotFoundException e) {
//            LOGGER.error("CAN NOT LOAD jdbc driver", e);
//            throw new RuntimeException(e);
//        }

    }

    /**
     * 执行sql文件
     * @param filePath
     */
    public static void executeFile(String filePath){
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
        try {
            String sql;
            while ((sql = bufferedReader.readLine()) != null){
                DatabaseHelper.executeUpdate(sql,null);
            }
        } catch (IOException e) {
            LOGGER.error("execute sql file failure", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用threadLocal 改写的数据库链接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection connection = CONNECTION_THREAD_LOCAL.get();
        if (connection == null) {
            try {
                // 改写为数据库连接池获取连接
                connection = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_THREAD_LOCAL.set(connection);
            }
        }
        return connection;
    }

    /**
     * 查询的第二种方式，使用内置connection 的threadlocal进行操作
     * 查询数据列表， 使用dbutil 进行查询
     * BeanListHandler 处理bean 结果
     *
     * @param entityClass 实体对象class
     * @param sql         查询语句
     * @param params      查询参数
     * @param <T>         泛型参数
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList = null;
        Connection connection = getConnection();
        try {
            entityList = QUERY_RUNNER.query(connection, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return entityList;
    }

    /**
     * 用于复杂查询使用，Map列名与列值成对应关系
     *
     * @param sql    查询语句
     * @param params 参数
     * @return List<Map < String, Object>> 键值对对象集合
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result;
        try {
            Connection connection = getConnection();
            result = QUERY_RUNNER.query(connection, sql, new MapListHandler(), params);
        } catch (Exception e) {
            LOGGER.error("execute query failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }


    /**
     * 唯一查询，根据主键查找实体类
     *
     * @param entityClass 实体类
     * @param sql         数据库链接语句
     * @param params      参数
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity = null;
        try {
            Connection connection = getConnection();
            entity = QUERY_RUNNER.query(connection, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return entity;
    }

    /**
     * 更新记录
     *
     * @param params 请求参数
     * @param sql    查询语句
     * @return
     */
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;
        Connection connection = getConnection();
        try {
            rows = QUERY_RUNNER.update(connection, sql, params);
        } catch (SQLException e) {
            LOGGER.error("update failure", e);
            throw new RuntimeException();
        }
        return rows;
    }

    /**
     * 插入实体
     *
     * @param entityClass 实体类
     * @param filedMap    参数
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> filedMap) {
        if (MyCollectionUtil.isEmpty(filedMap)) {
            LOGGER.error("can not insert entity: filedMap is empty");
            return false;
        }

        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String key : filedMap.keySet()) {
            columns.append(key).append(",");
            values.append("?,");
        }
        // 去掉最尾部的逗号
        columns.replace(columns.lastIndexOf(","), columns.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");
        sql += columns + " VALUES " + values;

        Object[] params = filedMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }

    /**
     * 更新实体类
     * @param entityClass
     * @param id
     * @param filedMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass, int id, Map<String, Object> filedMap) {
        if (MyCollectionUtil.isEmpty(filedMap)) {
            LOGGER.error("can not insert entity: filedMap is empty");
            return false;
        }

        String sql = "UPDATE " + getTableName(entityClass) + " SET ";
        StringBuilder columns = new StringBuilder();
        for (String filedName : filedMap.keySet()) {
            columns.append(filedName).append("=?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id=?";
        // 拼接更新参数
        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(filedMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();

        return executeUpdate(sql, params) == 1;

    }

    /**
     * 删除实体
     * @param entityClass 实体类
     * @param id 主键
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, int id){
        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id=?";
        return executeUpdate(sql, id) == 1;
    }

    /**
     * 获取表名
     *
     * @param entityClass
     * @return
     */
    private static <T> String getTableName(Class<T> entityClass) {
        return entityClass.getSimpleName().toLowerCase();
    }


    /**
     * 此方式不建议使用
     * 查询数据列表， 使用dbutil 进行查询
     * BeanListHandler 处理bean 结果
     *
     * @param connection  数据库链接
     * @param entityClass 实体对象class
     * @param sql         查询语句
     * @param params      查询参数
     * @param <T>         泛型参数
     * @return
     */
    @Deprecated
    public static <T> List<T> queryEntityList(Connection connection, Class<T> entityClass, String sql, Object... params) {
        List<T> entityList = null;
        try {
            entityList = QUERY_RUNNER.query(connection, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
        return entityList;

    }



    /*
     * 获取数据库连接
     * @deprecated 此方法被弃用
     */
//    @Deprecated
//    public static Connection getConnection(){
//        Connection connection = null;
//        try {
//            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//        } catch (SQLException e) {
//            LOGGER.error("connection error failure", e);
//            throw new RuntimeException(e);
//        }
//        return connection;
//    }

    /**
     * 使用threadlocal 进行关闭链接操作
     */
    public static void closeConnection() {
        Connection connection = CONNECTION_THREAD_LOCAL.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("connection close failure", e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 此方法以及被后续的threadlocal 弃用
     * 关闭链接
     *
     * @param connection 数据库链接
     * @deprecated 被新方法替代
     */
    @Deprecated
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("connection close failure", e);
                throw new RuntimeException(e);
            }
        }
    }
}
