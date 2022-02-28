package com.craftmend.storm.connection;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface StormDriver {

    ResultSet executeQuery(String query, Object... arguments) throws SQLException;
    boolean execute(String query) throws SQLException;
    int executeUpdate(String query, Object... arguments) throws SQLException;
    DatabaseMetaData getMeta() throws SQLException;
    boolean isOpen();
    void close();

}
