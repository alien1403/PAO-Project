package com.company.services;

import com.company.cards.Card;
import com.company.cards.PremiumCard;
import com.company.cards.StandardCard;
import com.company.user.Customer;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public interface CardServiceInterface {
    public StandardCard createStandardCard(long userUniqueId, String cardNumber, Date expirationDate, double ammount, double withdrawFee) throws ParseException;

    public PremiumCard createPremiumCard(long userUniqueId, String cardNumber, Date expirationDate, double ammount, double cashBack) throws ParseException;

    public void readCardsFromCsv() throws ParseException;


    public void depositCash(double amount, Customer loggedCustomer);


    public void createStandardCard_(long uniqueId) throws ParseException, FileNotFoundException;
    public void createPremiumCard_(long uniqueId) throws ParseException, FileNotFoundException;

    public void viewCardDetails(Customer customer);

    public void printExpiredCards();
}
