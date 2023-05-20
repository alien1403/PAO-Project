package com.company.services.implement;

import com.company.config.DatabaseConfiguration;
import com.company.repository.AdminRepository;
import com.company.repository.CustomerRepository;
import com.company.services.UserServiceInterface;
import com.company.user.Admin;
import com.company.user.Customer;
import com.company.user.User;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService implements UserServiceInterface {

    private static long uniqueId = 0;

    private static UserService instance;
    private final List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    CsvReaderService csvReaderService = CsvReaderService.getInstance();
    CsvWriterService csvWriterService = CsvWriterService.getInstance();

    public static UserService getInstance(){
        if(instance == null){
            instance = new UserService();
        }
        return instance;
    }

    public Customer createCustomer(String firstName, String lastName, String email, String password, String cnp, String phoneNumber) throws ParseException {
        return new Customer(++uniqueId, firstName, lastName, email, password, cnp, phoneNumber);
    }

    public Admin createAdmin(String firstName, String lastName, String email, String password) throws ParseException {
        return new Admin(++uniqueId, firstName, lastName, email, password);
    }

    public void readUsersFromCsv() throws ParseException {
        List<String[]> customersList = csvReaderService.readCustomersFromCsv();
        for (String[] strings : customersList) {
            Customer newCustomer = createCustomer(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5]);
            users.add(newCustomer);
        }

        List<String[]> adminList = csvReaderService.readAdminsFromCsv();
        for (String[] strings : adminList) {
            Admin newAdmin = createAdmin(strings[0], strings[1], strings[2], strings[3]);
            users.add(newAdmin);
        }
    }



    public User getUserByEmailAndPassword(String email, String password)
    {
        for (User user : users) {
            if (Objects.equals(user.getEmail(), email) && Objects.equals(user.getPassword(), password))
                return user;
        }
        return null;
    }

    public void createCustomerAccount() throws ParseException, FileNotFoundException {
        Scanner in = new Scanner(System.in);
        String firstName, lastName, email, password, cnp, phoneNumber;

        System.out.println("First name: ");
        firstName = in.nextLine();
        System.out.println("Last name: ");
        lastName = in.nextLine();
        System.out.println("CNP: ");
        cnp = in.nextLine();

        String regexCNP = "(5|6)([1-9]{1}[0-9]{1})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])([0-9]{6})";
        Pattern patternCNP = Pattern.compile(regexCNP);
        Matcher matcherCNP = patternCNP.matcher(cnp);
        if(!matcherCNP.matches())
        {
            System.out.println("Invalid CNP!");
            return;
        }

        System.out.println("Email address: ");
        email = in.nextLine();

        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@"
                + "[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern patternEmail = Pattern.compile(regexPattern);
        Matcher matcherEmail = patternEmail.matcher(email);
        if(!matcherEmail.matches())
        {
            System.out.println("Invalid email!");
            return;
        }

        System.out.println("Password: ");
        password = in.nextLine();

        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        Pattern pattern = Pattern.compile(patterns);

        System.out.println("Phone number: ");
        phoneNumber = in.nextLine();
        Matcher matcherPhoneNumber = pattern.matcher(phoneNumber);
        if(!matcherPhoneNumber.matches())
        {
            System.out.println("Invalid phone number!");
            return;
        }


        Customer newCustomer = createCustomer(firstName, lastName, email, password, cnp, phoneNumber);
        users.add(newCustomer);

        CustomerRepository customerRepository = new CustomerRepository();
        customerRepository.createTable();
        customerRepository.addCustomer(firstName, lastName, email, password, cnp, phoneNumber);

        DatabaseConfiguration.closeDatabaseConnection();

        // add to csv
        csvWriterService.writeCustomerInCsv(newCustomer);
    }

    public void createAdminAccount() throws ParseException, FileNotFoundException {
        Scanner in = new Scanner(System.in);
        String firstName, lastName, email, password;

        System.out.println("First name: ");
        firstName = in.nextLine();
        System.out.println("Last name: ");
        lastName = in.nextLine();

        System.out.println("Email address: ");
        email = in.nextLine();

        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@"
                + "[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern patternEmail = Pattern.compile(regexPattern);
        Matcher matcherEmail = patternEmail.matcher(email);
        if(!matcherEmail.matches())
        {
            System.out.println("Invalid email!");
            return;
        }

        System.out.println("Password: ");
        password = in.nextLine();

        AdminRepository adminRepository = new AdminRepository();
        adminRepository.createTable();
        adminRepository.addAdmin(firstName, lastName, email, password);

        DatabaseConfiguration.closeDatabaseConnection();

        Admin newAdmin = createAdmin(firstName, lastName, email, password);
        users.add(newAdmin);

        // add to csv
        csvWriterService.writeAdminInCsv(newAdmin);
    }


}




