package com.craftmend.storm.connection;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface StormDriver {

    ResultSet executeQuery(String query, Object... arguments) throws IOException, SQLException;
    int executeUpdate(String query, Object... arguments) throws IOException, SQLException;
    boolean isOpen();
    void close();

}
