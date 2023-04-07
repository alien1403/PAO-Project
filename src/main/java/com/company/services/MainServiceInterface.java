package com.company.services;

import com.company.user.Admin;
import com.company.user.Customer;
import com.company.user.User;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Objects;
import java.util.Scanner;

public interface MainServiceInterface {
    public void readFromCsv() throws ParseException;

    public void login() throws FileNotFoundException;

    public void depositCash(double amount, Customer loggedCustomer) throws FileNotFoundException;

    public void withdraw(double amount, Customer loggedCustomer) throws FileNotFoundException;

    public void createCard(Customer customer) throws ParseException, FileNotFoundException;

    public void deleteCard(Admin loggedAdmin);

    public void viewAllCards(Admin loggedAdmin) throws FileNotFoundException;

    public void viewAllCardsCustomer(Customer customer) throws FileNotFoundException;

    public void viewAvailableCardsCustomer(Customer customer) throws FileNotFoundException;

    public void viewExpiredCardsCustomer(Customer customer) throws FileNotFoundException;

    public void deleteAccount(Admin loggedAdmin);

    public void printExpiredCards(Admin loggedAdmin) throws FileNotFoundException;

    public void printAvailableCards(Admin loggedAdmin) throws FileNotFoundException;

    public void createAccount() throws ParseException, FileNotFoundException;

    public void viewCustomerDetailsAdmin(Customer customer) throws FileNotFoundException;

    public void viewCustomerDetails(Customer customer) throws FileNotFoundException;

    public void customerMenu(Customer loggedCustomer);
    public void adminMenu(Admin loggedAdmin);

    public void loginMenu();
}
