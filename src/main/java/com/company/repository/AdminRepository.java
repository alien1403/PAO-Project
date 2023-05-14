package com.company.repository;
import com.company.config.DatabaseConfiguration;
import com.company.user.Admin;
import com.company.user.Customer;
import com.company.user.User;

import java.sql.*;
import java.util.ArrayList;

public class AdminRepository {
    public void createTable(){
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Admins" +
                "(id INTEGER PRIMARY KEY, Firstname TEXT,Lastname TEXT,Email TEXT,Password TEXT)";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSQL);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addAdmin(String Firstname, String Lastname, String Email, String Password){
        String insertAdminSQL = "INSERT into Admins(Firstname,Lastname,Email,Password) values(?,?,?,?)";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(insertAdminSQL);
            preparedStatement.setString(1,Firstname);
            preparedStatement.setString(2,Lastname);
            preparedStatement.setString(3,Email);
            preparedStatement.setString(4,Password);

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void displayAdmin(){
        String selectAdminSQL = "SELECT * from Admins";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try{
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(selectAdminSQL);
            while(resultSet.next()){
                System.out.println("Firstname: " + resultSet.getString(2));
                System.out.println("Lastname: " + resultSet.getString(3));
                System.out.println("Email: " + resultSet.getString(4));
                System.out.println("Password: " + resultSet.getString(5));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Admin getAdminByEmailAndPassword(String Email, String Password){
        String selectSQL = "Select * FROM Admins where Email=? and Password=?";

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

    private Admin mapToUser(ResultSet resultSet) throws SQLException{
        if(resultSet.next()){
            return new Admin(
                    2,
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4));
        }
        return null;
    }

    public ArrayList<Customer> displayCustomers(){
        System.out.println("DISPLAY");
        String selectAdminSQL = "SELECT * from Customers";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        ArrayList<Customer> customersSorted = new ArrayList<>();
        try{
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(selectAdminSQL);
            while(resultSet.next()){
                Customer newCustomer = new Customer(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)
                );
                customersSorted.add(newCustomer);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return customersSorted;
    }


}
