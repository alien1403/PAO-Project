package com.company.repository;

import com.company.cards.Card;
import com.company.cards.PremiumCard;
import com.company.cards.StandardCard;
import com.company.config.DatabaseConfiguration;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;

public class PremiumCardRepository {
    public void createTable(){
        String createTableSQL = "CREATE TABLE IF NOT EXISTS PremiumCards"+
                "(userID INTEGER NOT NULL, CardNumber TEXT NOT NULL, ExpirationDate DATE NOT NULL, Amount REAL NOT NULL,\n" +
                "    Cashback REAL NOT NULL,\n" +
                "    FOREIGN KEY (userID) REFERENCES Customers(id) )";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSQL);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addPremiumCard(Integer userID, String CardNumber, Date ExpirationDate, Double Amount, Double Cashback){
        String insertPremiumCardSQL = "INSERT INTO PremiumCards(userID, CardNumber, ExpirationDate, Amount, Cashback) values(?,?,?,?,?)";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(insertPremiumCardSQL);
            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, CardNumber);
            preparedStatement.setDate(3, ExpirationDate);
            preparedStatement.setDouble(4, Amount);
            preparedStatement.setDouble(5, Cashback);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ArrayList<PremiumCard> displayPremiumCards(){
        String selectPremiumCardsSQL = "SELECT * from PremiumCards";
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        ArrayList<PremiumCard> premiumCards = new ArrayList<>();
        try{
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(selectPremiumCardsSQL);
            while(resultSet.next()){
                PremiumCard newStandardCard = new PremiumCard(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getDate(3),
                        resultSet.getDouble(4),
                        resultSet.getDouble(5)
                );
                premiumCards.add(newStandardCard);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return premiumCards;
    }
    public void updateAmountPremiumCard(Integer userId, String CardNumber, Double amount){
        String updateSQL = "Update PremiumCards set Amount=? where userID = ? and CardNumber = ?";
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
