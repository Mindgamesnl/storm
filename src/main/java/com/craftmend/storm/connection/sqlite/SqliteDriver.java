package com.craftmend.storm.connection.sqlite;

import com.craftmend.storm.connection.StormDriver;
import com.craftmend.storm.utils.Syntax;
import lombok.Getter;

import java.io.File;
import java.sql.*;
import java.util.function.Consumer;

public class SqliteDriver implements StormDriver {

    private Connection conn;
    @Getter private Syntax syntax = new Syntax();

    public SqliteDriver(File dataFile) throws SQLException {
        String url = "jdbc:sqlite:" + dataFile.getAbsolutePath();
        conn = DriverManager.getConnection(url);
    }

    @Override
    public void executeQuery(String query, Callback callback, Object... arguments) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (int i = 0; i < arguments.length; i++) {
                ps.setObject(i + 1, arguments[i]);
            }
            callback.onAccept(ps.executeQuery());
        }
    }

    @Override
    public boolean execute(String query) throws SQLException {
        try (Statement ps = conn.createStatement()) {
            return ps.execute(query);
        }
    }

    @Override
    public int executeUpdate(String query, Object... arguments) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (int i = 0; i < arguments.length; i++) {
                ps.setObject(i + 1, arguments[i]);
            }
            return ps.executeUpdate();
        }
    }

    @Override
    public DatabaseMetaData getMeta() throws SQLException {
        return conn.getMetaData();
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
