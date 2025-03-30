package my.code.starter.jdbc_advanced.blob_clob_005;

import my.code.starter.util.ConnectionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;

/**
 * blob - bytearray in postgres
 * clob - TEXT in postgres
 */
public class BlobRunner {
    public static void main(String[] args) throws SQLException, IOException {
        getImage();
    }

    private static void getImage() throws SQLException, IOException {
        var sql = """
                SELECT image
                FROM aircraft
                WHERE id = ?
                """;
        try (var connection = ConnectionManager.open();
             var prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setLong(1, 1);
            var resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                var image = resultSet.getBytes("image");
                Files.write(Path.of("resources", "boin777_downloaded.jpg"), image, StandardOpenOption.CREATE);
            }
        }
    }

    private static void saveImage() throws SQLException, IOException {
        var sql = """
                UPDATE aircraft
                SET image = ?
                WHERE id = 1
                """;
        try (var connection = ConnectionManager.open();
             var prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setBytes(1, Files.readAllBytes(Path.of("resources", "boin777.jpg")));
            prepareStatement.executeUpdate();
        }
    }

//    private static void saveImage() throws SQLException, IOException {
//        String sql = """
//                UPDATE aircraft
//                SET image = ?
//                WHERE id = 1
//                """;
//        try (var connection = ConnectionManager.open();
//             var prepareStatement = connection.prepareStatement(sql)) {
//
//            connection.setAutoCommit(false);
//
//            var blob = connection.createBlob();
//            blob.setBytes(1, Files.readAllBytes(Path.of("resources", "boin777.jpg")));
//
//            prepareStatement.setBlob(1, blob);
//            prepareStatement.executeUpdate();
//
//            connection.commit();
//        }
//    }
}
