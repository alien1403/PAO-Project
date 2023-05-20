package com.company.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {
    private static  final String DB_URL = "jdbc:sqlite:database.sqlite";
    private static Connection databaseConnection;
    private DatabaseConfiguration(){
    }

    public static Connection getDatabaseConnection(){
        try{
            if(databaseConnection == null || databaseConnection.isClosed()){
                databaseConnection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return databaseConnection;
    }

    public static void closeDatabaseConnection(){
        try{
            if(databaseConnection != null || !databaseConnection.isClosed()){
                databaseConnection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
