package cc.zzr.helper;

import cc.zzr.ConfigConstant;
import cc.zzr.util.CollectionUtil;
import cc.zzr.util.PropsUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final QueryRunner QUERY_RUNNER;
    private static final BasicDataSource DATA_SOURCE;


    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();
        Properties props = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);
        String driver = props.getProperty(ConfigConstant.JDBC_DRIVER);
        String url = props.getProperty(ConfigConstant.JDBC_URL);
        String username = props.getProperty(ConfigConstant.JDBC_USERNAME);
        String password = props.getProperty(ConfigConstant.JDBC_PASSWORD);
      /*  try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver ", e);
        }*/
        //设置数据库连接池
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);
    }

    /**
     * 获得connection
     *
     * @return
     */
    public static Connection getConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        try {
            if (connection == null) {
                connection = DATA_SOURCE.getConnection();
            }
        } catch (SQLException e) {
            LOGGER.error("get connection failure", e);
        } finally {
            CONNECTION_HOLDER.set(connection);
        }
        return connection;
    }

    /**
     * 关闭connection
     *
     * @param connection
     */
    public static void close() {
        Connection connection = CONNECTION_HOLDER.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("close connetion failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }


    /**
     * 查询列表
     *
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList = null;
        try {
            entityList = QUERY_RUNNER.query(getConnection(), sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query list failure", e);
            throw new RuntimeException(e);
        } finally {
            close();
        }
        return entityList;
    }

    /**
     * 返回List<Map<String,Object>>
     *
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> results = null;
        try {
            results = QUERY_RUNNER.query(getConnection(), sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("execute query failure", e);
            throw new RuntimeException(e);
        } finally {
            close();
        }
        return results;
    }

    /**
     * 执行更新操作（update,insert,delete)
     *
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, Object... params) {
        int result = 0;
        try {
            result = QUERY_RUNNER.update(getConnection(), sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure", e);
            throw new RuntimeException(e);
        } finally {
            close();
        }
        return result;
    }

    /**
     * 插入实体
     *
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not insert entity:fieldMap is empty");
            return false;
        }
        String sql = "insert into " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(","), columns.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");
        sql += columns + " values " + values;

        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }

    /**
     * 更新实体
     *
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not update entity:fieldMap is empty");
            return false;
        }
        String sql = "update " + getTableName(entityClass) + " SET ";
        StringBuilder columns = new StringBuilder("");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append("=?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(",")) + " where id = ? ";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);

        Object[] params = paramList.toArray();

        return executeUpdate(sql, params) == 1;
    }

    /**
     * 删除实体
     *
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = " delete from " + getTableName(entityClass) + " where id = ? ";
        return executeUpdate(sql, id) == 1;
    }

    /**
     * 获取单个entity
     *
     * @param entityClass
     * @param sql
     * @param id
     * @param <T>
     * @return
     */
    public static <T> T getEntityById(Class<T> entityClass, String sql, long id) {
        try {
            return QUERY_RUNNER.query(getConnection(), sql, new BeanHandler<T>(entityClass), id);
        } catch (SQLException e) {
            LOGGER.error("execute query entity by id failure", e);
            throw new RuntimeException(e);
        } finally {
            close();
        }

    }


    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

    /**
     * 开启事务
     */
    public static void beginTransaction() {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("begin transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("commit transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("rollback transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }
}
