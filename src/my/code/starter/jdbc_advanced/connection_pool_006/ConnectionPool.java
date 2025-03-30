package my.code.starter.jdbc_advanced.connection_pool_006;

import my.code.starter.exception.ConnectionException;
import my.code.starter.util.PropertiesUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class ConnectionPool {
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final Integer DEFAULT_POOL_SIZE = 10;

    private static BlockingQueue<Connection> pool;
    private static List<Connection> sourceConnections;

    static {
        innitConnectionPool();
    }

    private ConnectionPool() {
    }

    private static void innitConnectionPool() {
        var poolSize = PropertiesUtil.get(POOL_SIZE_KEY);
        var size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        sourceConnections = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            var connection = createConnection();
            var proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionPool.class.getClassLoader(), new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close")
                            ? pool.add((Connection) proxy)
                            : method.invoke(connection, args));
            pool.add(proxyConnection);
            sourceConnections.add(connection);
        }

    }

    private static Connection createConnection() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }

    public static Connection getConnection() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new ConnectionException(e);
        }
    }

    public static void shutdown() {
        try {
            for (Connection sourceConnection : sourceConnections) {
                sourceConnection.close();
            }
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }
}
