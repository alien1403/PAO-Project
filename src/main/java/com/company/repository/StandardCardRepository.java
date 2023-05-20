package com.company.repository;

import com.company.cards.Card;
import com.company.cards.StandardCard;
import com.company.config.DatabaseConfiguration;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class StandardCardRepository {
    public void createTable(){
        String createTableSQL = "CREATE TABLE IF NOT EXISTS StandardCards"+
                "(userID INTEGER NOT NULL, CardNumber TEXT NOT NULL, ExpirationDate DATE NOT NULL, Amount REAL NOT NULL,\n" +
                "    WithdrawFee REAL NOT NULL,\n" +
                "    FOREIGN KEY (userID) REFERENCES Customers(id) )";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSQL);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addStandardCard(Integer userID, String CardNumber, Date ExpirationDate, Double Amount, Double WithdrawFee){
        String insertStandardCardSQL = "INSERT INTO StandardCards(userID, CardNumber, ExpirationDate, Amount, WithdrawFee) values(?,?,?,?,?)";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(insertStandardCardSQL);
            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, CardNumber);
            preparedStatement.setDate(3, ExpirationDate);
            preparedStatement.setDouble(4, Amount);
            preparedStatement.setDouble(5, WithdrawFee);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ArrayList<StandardCard> displayStandardCards(){
        String selectStandardCardsSQL = "SELECT * from StandardCards";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        ArrayList<StandardCard> standardCards = new ArrayList<>();
        try{
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(selectStandardCardsSQL);
            while(resultSet.next()){
                StandardCard newStandardCard = new StandardCard(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getDate(3),
                        resultSet.getDouble(4),
                        resultSet.getDouble(5)
                );
                standardCards.add(newStandardCard);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return standardCards;
    }
    public void updateAmountStandardCard(Integer userId, String CardNumber, Double amount){
        String updateSQL = "Update StandardCards set Amount=? where userID = ? and CardNumber = ?";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3,CardNumber);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
