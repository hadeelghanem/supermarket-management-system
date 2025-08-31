package DataAccessLayer.util;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Could not load SQLite JDBC driver: " + e.getMessage());
        }
    }

    private Database() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                File dbFile = extractDatabaseOnce();
                connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            } catch (IOException e) {
                throw new SQLException("Failed to extract or connect to Deliveries.db", e);
            }
        }
        return connection;
    }

    private static File extractDatabaseOnce() throws IOException {
        File dbFile = new File("Deliveries_runtime.db");

        if (!dbFile.exists()) {
            try (InputStream input = Database.class.getResourceAsStream("/db/Deliveries.db");
                 OutputStream out = new FileOutputStream(dbFile)) {

                if (input == null) {
                    throw new FileNotFoundException("Deliveries.db not found in JAR");
                }

                byte[] buffer = new byte[1024];
                int len;
                while ((len = input.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            }
        }

        return dbFile;
    }

}
