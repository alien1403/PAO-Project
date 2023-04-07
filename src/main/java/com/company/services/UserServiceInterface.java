package com.company.services;

import com.company.user.Admin;
import com.company.user.Customer;
import com.company.user.User;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public interface UserServiceInterface {

    public Customer createCustomer(String firstName, String lastName, String email, String password, String cnp, String phoneNumber) throws ParseException;

    public Admin createAdmin(String firstName, String lastName, String email, String password) throws ParseException;
    public void readUsersFromCsv() throws ParseException;

    public User getUserByEmailAndPassword(String email, String password);

    public void createCustomerAccount() throws ParseException, FileNotFoundException;

    public void createAdminAccount() throws ParseException, FileNotFoundException;
}
