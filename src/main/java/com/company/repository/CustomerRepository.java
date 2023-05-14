package com.company.repository;
import com.company.config.DatabaseConfiguration;
import com.company.user.Customer;
import com.company.user.User;

import java.sql.*;

public class CustomerRepository {
    public void createTable(){
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Customers" +
                "(id INTEGER PRIMARY KEY,Firstname TEXT,Lastname TEXT,Email TEXT,Password TEXT,CNP TEXT,Phonenumber TEXT)";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSQL);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addCustomer(String Firstname, String Lastname, String Email, String Password, String CNP, String Phonenumber){
        String insertCustomerSQL = "INSERT into Customers(Firstname,Lastname,Email,Password,CNP,Phonenumber) values(?,?,?,?,?,?)";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(insertCustomerSQL);
            preparedStatement.setString(1,Firstname);
            preparedStatement.setString(2,Lastname);
            preparedStatement.setString(3,Email);
            preparedStatement.setString(4,Password);
            preparedStatement.setString(5,CNP);
            preparedStatement.setString(6,Phonenumber);

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void displayCustomer(){
        String selectCustomerSQL = "SELECT * from Customers";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try{
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(selectCustomerSQL);
            while(resultSet.next()){
                System.out.println("Firstname: " + resultSet.getString(2));
                System.out.println("Lastname: " + resultSet.getString(3));
                System.out.println("Email: " + resultSet.getString(4));
                System.out.println("Password: " + resultSet.getString(5));
                System.out.println("CNP: " + resultSet.getString(6));
                System.out.println("Phone: " + resultSet.getString(7));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Customer getCustomerByEmailAndPassword(String Email, String Password){
        String selectSQL = "Select * FROM Customers where Email=? and Password=?";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1,Email);
            preparedStatement.setString(2, Password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToUser(resultSet);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private Customer mapToUser(ResultSet resultSet) throws SQLException{
        if(resultSet.next()){
            return new Customer(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7));
        }
        return null;
    }

    public void deleteCustomerById(int id){
        String deleteSQL = "DELETE FROM Customers where id=?";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1,id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if(rowsDeleted > 0){
                deleteSQL = "DELETE FROM StandardCards where userID=?";
                preparedStatement = connection.prepareStatement(deleteSQL);
                preparedStatement.setInt(1,id);
                rowsDeleted = preparedStatement.executeUpdate();

                deleteSQL = "DELETE FROM PremiumCards where userID=?";
                preparedStatement = connection.prepareStatement(deleteSQL);
                preparedStatement.setInt(1,id);
                rowsDeleted = preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
