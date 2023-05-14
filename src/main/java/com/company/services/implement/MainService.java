package com.company.services.implement;

import com.company.cards.Card;
import com.company.comparator.CustomerNameComparator;
import com.company.repository.AdminRepository;
import com.company.repository.CustomerRepository;
import com.company.services.MainServiceInterface;
import com.company.user.Admin;
import com.company.user.Customer;
import com.company.user.User;
import com.opencsv.exceptions.CsvException;
import org.jetbrains.annotations.NotNull;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.text.ParseException;
import java.util.*;

public class MainService implements MainServiceInterface {

    private static final List<String> loginCommands = Arrays.asList("1. Create Account", "2. Login", "3. Exit");
    private static final List<String> customerCommands = Arrays.asList("1. View account details", "2. Deposit cash", "3. Withdraw", "4. New Card", "5. Print expired cards","6. Print available cards","7. View all cards", "8. Logout", "9. Exit");
    private static final List<String> adminCommands = Arrays.asList( "1. View customer details", "2. Delete account", "3. Print expired cards", "4. Print available cards","5. Logout", "6. Exit");

    private ArrayList<Customer> customers = new ArrayList<Customer>();
    private static MainService instance = null;
    UserService userService = UserService.getInstance();
    CardService cardService = CardService.getInstance();
    AuditService auditService = AuditService.getInstance();

    public static MainService getInstance(){
        if(instance == null){
            instance = new MainService();
        }
        return instance;
    }
    @Override
    public void readFromCsv() throws ParseException {
        userService.readUsersFromCsv();
        cardService.readCardsFromCsv();

        // after reading go to log in menu
        loginMenu();
    }

    @Override
    public void login() throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        // call the appropriate menu by user type after logged in

        System.out.println("Enter email address: ");
        String email = in.nextLine();
        System.out.println("Enter password: ");
        String password = in.nextLine();

//        User loggedUser = userService.getUserByEmailAndPassword(email, password);
        User loggedUser = new User();
        AdminRepository adminRepository = new AdminRepository();
        Admin adminUser = adminRepository.getAdminByEmailAndPassword(email, password);
        CustomerRepository customerRepository = new CustomerRepository();
        Customer customerUser = customerRepository.getCustomerByEmailAndPassword(email, password);
        if(adminUser == null && customerUser != null){
            loggedUser = customerUser;
        }else if(adminUser != null && customerUser == null){
            loggedUser = adminUser;
        }else{
            loggedUser = null;
        }

        while(loggedUser == null)
        {
            System.out.println("Email/Password not found. Try again.");
            System.out.println("Enter email address: ");
            email = in.nextLine();
            System.out.println("Enter password: ");
            password = in.nextLine();
            adminUser = adminRepository.getAdminByEmailAndPassword(email, password);
            customerUser = customerRepository.getCustomerByEmailAndPassword(email, password);
            if(adminUser == null && customerUser != null){
                loggedUser = customerUser;
            }else if(adminUser != null && customerUser == null){
                loggedUser = adminUser;
            }
        }
        auditService.writeActionInCsv("user logged in");
        if(Objects.equals(loggedUser.getTypeOfUser(), "customer")){
            customerMenu((Customer) loggedUser);
        }
        else adminMenu((Admin) loggedUser);
    }
    @Override
    public void depositCash(double amount, Customer loggedCustomer) throws FileNotFoundException {
        cardService.depositCash(amount, loggedCustomer);
        auditService.writeActionInCsv("deposit cash");
        // go back to customer menu
        customerMenu(loggedCustomer);
    }
    @Override
    public void withdraw(double amount, Customer loggedCustomer) throws FileNotFoundException {
        cardService.withdraw(amount, loggedCustomer);
        auditService.writeActionInCsv("withdraw cash");

        customerMenu(loggedCustomer);
    }
    @Override
    public void createCard(Customer customer) throws ParseException, FileNotFoundException {
        // choose what type of card to create:
        Scanner in = new Scanner(System.in);

        System.out.println("Type of card (1 - standard, 2 - premium): ");
        String command = in.nextLine();
        while(!Objects.equals(command, "1") || !Objects.equals(command, "2"))
        {
            if (Objects.equals(command, "1"))
            {
                cardService.createStandardCard_((int) customer.getUniqueId());
                break;
            }
            else if (Objects.equals(command, "2"))
            {
                cardService.createPremiumCard_((int) customer.getUniqueId());
                break;
            }
            else
            {
                System.out.println("Command unknown. Please try again");
                System.out.println("Type of card (1 - standard, 2 - premium): ");
                command = in.nextLine();
            }
        }
        auditService.writeActionInCsv("created card");
        // after the card creation, return to the customer menu
        customerMenu(customer);
    }
    @Override
    public void deleteCard(Admin loggedAdmin){
        cardService.viewAllCards();
        adminMenu(loggedAdmin);
    }
    @Override
    public void viewAllCards(Admin loggedAdmin) throws FileNotFoundException{
        cardService.viewAllCards();
        auditService.writeActionInCsv("view all cards");

        adminMenu(loggedAdmin);

    }
    @Override
    public void deleteAccount(Admin loggedAdmin){

        AdminRepository adminRepository = new AdminRepository();
        ArrayList<Customer> customers = adminRepository.displayCustomers();

        System.out.println("Choose a number");
        for(int i = 0;i<customers.size();i++){
            System.out.print(customers.get(i).getUniqueId() + " ");
            System.out.println(customers.get(i).getFirstName() + " " + customers.get(i).getLastName());
        }

        Scanner in = new Scanner(System.in);
        int command = in.nextInt();

        CustomerRepository customerRepository = new CustomerRepository();
        customerRepository.deleteCustomerById(command);

        adminMenu(loggedAdmin);
    }
    @Override
    public void printExpiredCards(Admin loggedAdmin) throws FileNotFoundException {
        cardService.printExpiredCards();
        auditService.writeActionInCsv("print expired cards");
        adminMenu(loggedAdmin);
    }

    @Override
    public void printAvailableCards(Admin loggedAdmin) throws  FileNotFoundException{
        cardService.printAvailableCards();
        auditService.writeActionInCsv("print available cards");
        adminMenu(loggedAdmin);
    }
    @Override
    public void createAccount() throws ParseException, FileNotFoundException {
        // choose what type of accout to create:
        Scanner in = new Scanner(System.in);

        System.out.println("Type of account (1 - customer, 2 - admin): ");
        String command = in.nextLine();
        while(!Objects.equals(command, "1") || !Objects.equals(command, "2"))
        {
            if (Objects.equals(command, "1"))
            {
                userService.createCustomerAccount();
                break;
            }
            else if (Objects.equals(command, "2"))
            {
                userService.createAdminAccount();
                break;
            }
            else
            {
                System.out.println("Command unknown. Please try again");
                System.out.println("Type of account (1 - customer, 2 - admin): ");
                command = in.nextLine();
            }
        }
        auditService.writeActionInCsv("created account");
        // after the account creation, return to the login panel
        loginMenu();
    }

    @Override
    public void viewAllCardsCustomer(Customer customer) throws FileNotFoundException{
        cardService.viewAllCardsCustomer(customer);
        auditService.writeActionInCsv("view customer cards");
        customerMenu(customer);

    }
    @Override
    public void viewCustomerDetailsAdmin(Customer customer) throws FileNotFoundException{
        System.out.println("Name: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Identified by CNP: " + customer.getCnp());
        System.out.println("Email address: " + customer.getEmail());
        System.out.println("Phone number: " + customer.getPhoneNumber());
        System.out.println();
    }
    @Override
    public void viewCustomerDetails(Customer customer) throws FileNotFoundException {
        System.out.println();
        System.out.println("Name: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Identified by CNP: " + customer.getCnp());
        System.out.println("Email address: " + customer.getEmail());
        System.out.println("Phone number: " + customer.getPhoneNumber());

        cardService.viewCardDetails(customer);
        auditService.writeActionInCsv("customer show details");
        customerMenu(customer);
    }

    @Override
    public void viewAvailableCardsCustomer(Customer customer) throws FileNotFoundException{
        cardService.viewAvailableCardsCustomer(customer);
        auditService.writeActionInCsv("view available cards customer");
        customerMenu(customer);
    }

    @Override
    public void viewExpiredCardsCustomer(Customer customer) throws FileNotFoundException{
        cardService.viewExpiredCardsCustomer(customer);
        auditService.writeActionInCsv("view expired cards customer");
        customerMenu(customer);
    }
    @Override
    public void customerMenu(@NotNull Customer loggedCustomer) {

        System.out.println("\nWelcome, " + loggedCustomer.getFirstName() + "!\n");

        for (String customerCommand : customerCommands) {
            System.out.println(customerCommand);
        }

        while(true)
        {
            Scanner in = new Scanner(System.in);
            String command = in.nextLine();
            try
            {
                switch (command)
                {
                    case "1" -> viewCustomerDetails(loggedCustomer);
                    case "2" -> {
                        System.out.println("Enter the amount to deposit: ");
                        Scanner in_amount = new Scanner(System.in);
                        double amount = Double.parseDouble(in_amount.nextLine());
                        depositCash(amount, loggedCustomer);
                    }
                    case "3" -> {
                        System.out.println("Enter the amount to withdraw: ");
                        Scanner in_amount = new Scanner(System.in);
                        double amount = Double.parseDouble(in_amount.nextLine());
                        withdraw(amount, loggedCustomer);
                    }
                    case "4" -> createCard(loggedCustomer);
                    case "5" -> viewExpiredCardsCustomer(loggedCustomer);
                    case "6" -> viewAvailableCardsCustomer(loggedCustomer);
                    case "7" -> viewAllCardsCustomer(loggedCustomer);
                    case "8" -> loginMenu();
                    case "9" -> System.exit(0);
                }
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }

        }
    }
    @Override
    public void adminMenu(Admin loggedAdmin)
    {
        System.out.println("\nWelcome, " + loggedAdmin.getFirstName() + "!\n");
        for (String adminCommand : adminCommands) {
            System.out.println(adminCommand);
        }

        while(true)
        {
            Scanner in = new Scanner(System.in);
            String command = in.nextLine();
            try
            {
                switch (command)
                {
                    case "1" -> {
                        AdminRepository adminRepository = new AdminRepository();//                        ArrayList<Customer> customersSorted = new ArrayList<>();
                        ArrayList<Customer> customersSorted = adminRepository.displayCustomers();
                        Collections.sort(customersSorted, new CustomerNameComparator());
                        System.out.println("Afisare in ordina crescatoare dupa First Name:");
                        for (Customer customer : customersSorted) {
                            viewCustomerDetailsAdmin(customer);
                        }

                        adminMenu(loggedAdmin);
                    }
                    case "2" -> deleteAccount(loggedAdmin);
                    case "3" -> printExpiredCards(loggedAdmin);
                    case "4" -> printAvailableCards(loggedAdmin);
                    case "5" -> loginMenu();
                    case "6" -> System.exit(0);
                }
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }

        }


    }
    @Override
    public void loginMenu()
    {

        boolean exit = false;
        for (String loginCommand : loginCommands)
        {
            System.out.println(loginCommand);
        }

        while(!exit)
        {
            Scanner in = new Scanner(System.in);
            String command = in.nextLine();
            try
            {
                switch (command)
                {
                    case "1" -> {
                        exit = true;
                        createAccount();
                    }
                    case "2" -> {
                        exit = true;
                        login();
                    }
                    case "3" -> System.exit(0);
                }
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }

        }
    }
}
