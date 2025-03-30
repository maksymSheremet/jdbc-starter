package my.code.starter.jdbc_advanced.metaData_002;

import my.code.starter.util.ConnectionManager;

import java.sql.SQLException;

public class DemoMetaData {
    public static void main(String[] args) throws SQLException {
        checkMetaData();
    }

    public static void checkMetaData() throws SQLException {
        try (var connection = ConnectionManager.open()) {
            var metaData = connection.getMetaData();
            var catalogs = metaData.getCatalogs();

            while (catalogs.next()) {
                var catalog = catalogs.getString(1);
//                System.out.println(catalog);

                var schemas = metaData.getSchemas();
                while (schemas.next()) {
                    var schema = schemas.getString("TABLE_SCHEM");
//                    System.out.println(schema);

                    var tables = metaData.getTables(catalog, schema, "%", new String[]{"TABLE"});
                    if (schema.equals("public")) {
                        while (tables.next()) {
                            System.out.println(tables.getString("TABLE_NAME"));

//                            var columns = metaData.getColumns(catalog, schema, "%", null);
                        }
                    }

                }
            }
        }
    }
}
