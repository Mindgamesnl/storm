package com.craftmend.storm.connection;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface StormDriver {

    void executeQuery(String query, Callback callback, Object... arguments) throws Exception;
    boolean execute(String query) throws SQLException;
    int executeUpdate(String query, Object... arguments) throws SQLException;
    DatabaseMetaData getMeta() throws SQLException;
    boolean isOpen();
    void close();

    public interface Callback {
        void onAccept(ResultSet rs) throws Exception;
    }

}
