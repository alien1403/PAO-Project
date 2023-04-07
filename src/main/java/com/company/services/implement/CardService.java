package com.company.services.implement;

import com.company.cards.Card;
import com.company.cards.PremiumCard;
import com.company.cards.StandardCard;
import com.company.services.CardServiceInterface;
import com.company.user.Customer;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CardService implements CardServiceInterface {
    private static CardService instance;

    private final List<Card> cards = new ArrayList<>();

    CsvReaderService csvReaderService = CsvReaderService.getInstance();
    CsvWriterService csvWriterService = CsvWriterService.getInstance();

    public static CardService getInstance(){
        if(instance == null){
            instance = new CardService();
        }
        return instance;
    }


    public StandardCard createStandardCard(long userUniqueId, String cardNumber, Date expirationDate, double ammount, double withdrawFee) throws ParseException
    {
        return new StandardCard(userUniqueId, cardNumber, expirationDate, ammount, withdrawFee);
    }

    public PremiumCard createPremiumCard(long userUniqueId, String cardNumber, Date expirationDate, double ammount, double cashBack) throws ParseException
    {
        return new PremiumCard(userUniqueId, cardNumber, expirationDate, ammount, cashBack);
    }

    public void readCardsFromCsv() throws ParseException {
        List<String[]> standardCardList = csvReaderService.readStandardCardsFromCsv();
        for (String[] strings : standardCardList) {

            String sDate = strings[2];
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);

            StandardCard newStandardCard = createStandardCard(Long.parseLong(strings[0]), strings[1], date, Double.parseDouble(strings[3]), Double.parseDouble(strings[4]));
            cards.add(newStandardCard);
        }

        List<String[]> premiumCardList = csvReaderService.readPremiumCardsFromCsv();
        for (String[] strings : premiumCardList) {

            String sDate = strings[2];
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);

            PremiumCard newPremiumCard = createPremiumCard(Long.parseLong(strings[0]), strings[1], date, Double.parseDouble(strings[3]), Double.parseDouble(strings[4]));
            cards.add(newPremiumCard);
        }
    }


    public void viewAllCards(){
        System.out.println("Standard cards:");
        for(Card card: cards){
            if(card instanceof StandardCard){
                System.out.println(card.getCardNumber());
            }
        }

        System.out.println("Premium cards:");
        for(Card card: cards){
            if(card instanceof PremiumCard){
                System.out.println(card.getCardNumber());
            }
        }
    }

    public void viewAllCardsCustomer(Customer loggedCustomer){
        System.out.println("Standard cards:");
        for(Card card: cards){
            if(card instanceof StandardCard && card.getUserUniqueId() == loggedCustomer.getUniqueId()){
                System.out.println(card.getCardNumber());
            }
        }

        System.out.println("Premium cards:");
        for(Card card: cards){
            if(card instanceof PremiumCard && card.getUserUniqueId() == loggedCustomer.getUniqueId()){
                System.out.println(card.getCardNumber());
            }
        }
    }

    public void viewAvailableCardsCustomer(Customer loggedCustomer){
        System.out.println("Available cards for " + loggedCustomer.getFirstName() + " " + loggedCustomer.getLastName());
        System.out.println("Standard cards:");
        for(Card card: cards){
            if(card instanceof StandardCard && card.getUserUniqueId() == loggedCustomer.getUniqueId() && card.isAvailable()){
                System.out.println(card.getCardNumber());
            }
        }

        System.out.println("Premium cards:");
        for(Card card: cards){
            if(card instanceof PremiumCard && card.getUserUniqueId() == loggedCustomer.getUniqueId() && card.isAvailable()){
                System.out.println(card.getCardNumber());
            }
        }
    }

    public void viewExpiredCardsCustomer(Customer loggedCustomer){
        System.out.println("Expired cards for " + loggedCustomer.getFirstName() + " " + loggedCustomer.getLastName());
        System.out.println("Standard cards:");
        for(Card card: cards){
            if(card instanceof StandardCard && card.getUserUniqueId() == loggedCustomer.getUniqueId() && card.isExpired()){
                System.out.println(card.getCardNumber());
            }
        }

        System.out.println("Premium cards:");
        for(Card card: cards){
            if(card instanceof PremiumCard && card.getUserUniqueId() == loggedCustomer.getUniqueId() && card.isExpired()){
                System.out.println(card.getCardNumber());
            }
        }
    }

    public void depositCash(double amount, Customer loggedCustomer)
    {
        boolean hasCard = false;

        for (Card card : cards)
        {
            if (card.getUserUniqueId() == loggedCustomer.getUniqueId())
            {
                hasCard = true;
                break;
            }
        }
        if (!hasCard)
        {
            System.out.println("You don't have any cards in which to deposit money!\n");
            return;
        }
        int cardIndex = 1;
        System.out.println("Select the card in which to deposit:");
        for (Card card : cards)
        {
            if (card.getUserUniqueId() == loggedCustomer.getUniqueId())
            {
                System.out.println(cardIndex + ". Card number: " + card.getCardNumber());
                System.out.println("Current amount: " + card.getAmount() + "\n");
                cardIndex++;
            }
        }

        Scanner in = new Scanner(System.in);
        int command = Integer.parseInt(in.nextLine());
        while (command < 1 || command >= cardIndex)
        {
            System.out.println("Enter a valid number!");
            command = Integer.parseInt(in.nextLine());
        }

        cardIndex = 1;
        for (Card card : cards) {
            if (card.getUserUniqueId() == loggedCustomer.getUniqueId()) {
                if (cardIndex == command) {
                    // increment the amount
                    card.setAmount(card.getAmount() + amount);
                    break;
                } else cardIndex++;
            }
        }
    }

    public void withdraw(double amount, Customer loggedCustomer){
        boolean hasCard = false;

        for (Card card : cards)
        {
            if (card.getUserUniqueId() == loggedCustomer.getUniqueId())
            {
                hasCard = true;
                break;
            }
        }
        if (!hasCard)
        {
            System.out.println("You don't have any cards available!\n");
            return;
        }
        int cardIndex = 1;
        System.out.println("Select the card in which to withdraw:");
        for (Card card : cards)
        {
            if (card.getUserUniqueId() == loggedCustomer.getUniqueId())
            {
                if(card instanceof StandardCard){
                    System.out.println("Standard Card with " + ((StandardCard) card).getWithdrawFee() + "% fee");
                } else if (card instanceof PremiumCard) {
                    System.out.println("Premium Card with " + ((PremiumCard)card).getCashBack() + "% cashback");
                }
                System.out.println(cardIndex + ". Card number: " + card.getCardNumber());
                System.out.println("Current amount: " + card.getAmount() + "\n");

                cardIndex++;
            }
        }

        Scanner in = new Scanner(System.in);
        int command = Integer.parseInt(in.nextLine());
        while (command < 1 || command >= cardIndex)
        {
            System.out.println("Enter a valid number!");
            command = Integer.parseInt(in.nextLine());
        }

        cardIndex = 1;
        for (Card card : cards) {
            if (card.getUserUniqueId() == loggedCustomer.getUniqueId()) {
                if (cardIndex == command) {
                    if(card instanceof StandardCard){
                        double creditWithComission = ((StandardCard)card).getAmount() - amount - ((StandardCard) card).getWithdrawFee()/100*amount;
                        if(((StandardCard)card).getAmount() < amount){
                            System.out.println("Insufficient funds");
                            return;
                        }else{
                            card.setAmount(creditWithComission);
                        }
                    } else if (card instanceof PremiumCard) {
                        double new_cashback;
                        if(((PremiumCard)card).getAmount() < amount){
                            System.out.println("Insufficient funds");
                            return;
                        }else{
                            double newCredit = ((PremiumCard)card).getAmount() - amount;
                            new_cashback = (((PremiumCard)card).getCashBack() / 100) * amount;
                            card.setAmount(newCredit + new_cashback);
                        }
                    }
                    break;
                } else cardIndex++;
            }
        }

        String csvFileStandard = "src\\main\\java\\com\\company\\resources\\standardcards.csv";
        String csvFilePremium = "src\\main\\java\\com\\company\\resources\\premiumcards.csv";
        try{
            CSVReader readerStandard = new CSVReader(new FileReader(csvFileStandard));
            String[] headerStandard = readerStandard.readNext();
            List<String[]> rowsStandard = readerStandard.readAll();
            readerStandard.close();

            FileWriter writerStandard = new FileWriter(csvFileStandard);
            writerStandard.append("userId,CardNumber,ExpirationDate,Amount,WithdrawFee\n");

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            for(Card card: cards){
                if(card instanceof StandardCard){
                    String strDate = formatter.format(card.getExpirationDate());
                    writerStandard.append(String.valueOf(((StandardCard) card).getUserUniqueId())).append(",").append(((StandardCard) card).getCardNumber()).append(",").append(strDate).append(",").append(String.valueOf(((StandardCard) card).getAmount())).append(",").append(String.valueOf(((StandardCard) card).getWithdrawFee())).append("\n");
                }
            }

            writerStandard.close();

            CSVReader readerPremium = new CSVReader(new FileReader(csvFilePremium));
            String[] headerPremium = readerPremium.readNext();
            List<String[]> rowsPremium = readerPremium.readAll();
            readerPremium.close();

            FileWriter writerPremium = new FileWriter(csvFilePremium);
            writerPremium.append("userId,CardNumber,ExpirationDate,Amount,Cashback\n");

            SimpleDateFormat formatterPremium = new SimpleDateFormat("dd/MM/yyyy");

            for(Card card: cards){
                if(card instanceof PremiumCard){
                    String strDate = formatter.format(card.getExpirationDate());
                    writerPremium.append(String.valueOf(((PremiumCard) card).getUserUniqueId())).append(",").append(((PremiumCard) card).getCardNumber()).append(",").append(strDate).append(",").append(String.valueOf(((PremiumCard) card).getAmount())).append(",").append(String.valueOf(((PremiumCard) card).getCashBack())).append("\n");
                }
            }

            writerPremium.close();



        }catch (IOException | CsvException e){
            e.printStackTrace();
        }



    }


    public void createStandardCard_(long uniqueId) throws ParseException, FileNotFoundException {
        Random rand = new Random();
        int rand_month = rand.nextInt(1,13); // generate random month for exp date



        Scanner in = new Scanner(System.in);
        StringBuilder cardNumber = new StringBuilder();
        Date expirationDate = new Date(2025, rand_month, 1);

        double amount, withdrawFee;

        for(int i = 0; i < 16; i++)
        {
            int rand_number = rand.nextInt(9);
            cardNumber.append(rand_number);
        }

        System.out.println("Amount to deposit ($): ");
        amount = Double.parseDouble(in.nextLine());
        System.out.println("Withdrawal fee (%): ");
        withdrawFee = Double.parseDouble(in.nextLine());


        StandardCard newStandardCard = createStandardCard(uniqueId, String.valueOf(cardNumber), expirationDate, amount, withdrawFee);
        cards.add(newStandardCard);

        // add to csv
        csvWriterService.writeStandardCardInCsv(newStandardCard);
    }
    public void createPremiumCard_(long uniqueId) throws ParseException, FileNotFoundException {
        Random rand = new Random();
        int rand_month = rand.nextInt(1,13); // generate random month for exp date

        Scanner in = new Scanner(System.in);
        StringBuilder cardNumber = new StringBuilder();
        Date expirationDate = new Date(2025, rand_month, 1);

        double amount, cashBack;

        for(int i = 0; i < 16; i++)
        {
            int rand_number = rand.nextInt(9);
            cardNumber.append(rand_number);
        }

        System.out.println("Amount to deposit ($): ");
        amount = Double.parseDouble(in.nextLine());
        System.out.println("Cashback  (%): ");
        cashBack = Double.parseDouble(in.nextLine());

        PremiumCard newPremiumCard = createPremiumCard(uniqueId, String.valueOf(cardNumber), expirationDate, amount, cashBack);
        cards.add(newPremiumCard);

        // add to csv
        csvWriterService.writePremiumCardInCsv(newPremiumCard);
    }

    public void viewCardDetails(Customer customer)
    {
        System.out.println("Standard cards:");
        for (Card card : cards) {
            if (card.getUserUniqueId() == customer.getUniqueId() && card instanceof StandardCard) {
                System.out.println("\nCard details: ");
                System.out.println("Card number: " + card.getCardNumber());

                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
                String formattedDate = formatter.format(card.getExpirationDate());

                System.out.println("Exp. Date: " + formattedDate);
                System.out.println("Amount on card: " + card.getAmount());
                System.out.println("Fee: " + ((StandardCard) card).getWithdrawFee());
            }
        }

        System.out.println();
        System.out.println("Premium cards:");
        for (Card card : cards) {
            if (card.getUserUniqueId() == customer.getUniqueId() && card instanceof PremiumCard) {
                System.out.println("\nCard details: ");
                System.out.println("Card number: " + card.getCardNumber());

                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
                String formattedDate = formatter.format(card.getExpirationDate());

                System.out.println("Exp. Date: " + formattedDate);
                System.out.println("Amount on card: " + card.getAmount());
                System.out.println("Cashback: " + ((PremiumCard) card).getCashBack());
            }
        }
    }

    public void printExpiredCards()
    {
        System.out.println("Expired cards:");
        cards.stream().filter(card -> card.isExpired()).forEach(card -> System.out.println("Card number: " + card.getCardNumber()));
    }

    public void printAvailableCards(){
        System.out.println("Available cards:");
        cards.stream().filter(card -> card.isAvailable()).forEach(card -> System.out.println("Card number: " + card.getCardNumber()));
    }


}
