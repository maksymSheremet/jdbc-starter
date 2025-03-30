package my.code.starter.jdbc_advanced.connection_pool_006;

import java.sql.SQLException;

public class ConnectionPoolRunner {
    public static void main(String[] args) throws SQLException {
        try (var connection = ConnectionPool.getConnection()) {
            var metaData = connection.getMetaData();
            var catalogs = metaData.getCatalogs();

            while (catalogs.next()) {
                var catalog = catalogs.getString(1);
                System.out.println(catalog);
            }
        } finally {
            ConnectionPool.shutdown();
        }
    }
}
