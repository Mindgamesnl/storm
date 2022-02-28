package com.craftmend.storm.connection.sqlite;

import com.craftmend.storm.connection.StormDriver;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SqliteDriver implements StormDriver {

    private Connection conn;

    public SqliteDriver(File dataFile) throws SQLException {
        String url = "jdbc:sqlite:" + dataFile.getAbsolutePath();
        conn = DriverManager.getConnection(url);
    }

    @Override
    public ResultSet executeQuery(String query, Object... arguments) throws IOException, SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (int i = 0; i < arguments.length; i++) {
                ps.setObject(i + 1, arguments[i]);
            }
            return ps.executeQuery();
        }
    }

    @Override
    public int executeUpdate(String query, Object... arguments) throws IOException, SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (int i = 0; i < arguments.length; i++) {
                ps.setObject(i + 1, arguments[i]);
            }
            return ps.executeUpdate();
        }
    }

    @Override
    public boolean isOpen() {
        try {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void close() {
        if (isOpen()) {
            try {
                conn.close();
            } catch (SQLException e) {
                // ignored
            }
        }
    }
}
