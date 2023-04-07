package com.company.services;

import com.company.cards.PremiumCard;
import com.company.cards.StandardCard;
import com.company.user.Admin;
import com.company.user.Customer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public interface CsvWriterServiceInterface {
    public void writeCustomerInCsv(Customer customer) throws FileNotFoundException;

    private static String convertToCsvFormat(Customer customer) {

        return  customer.getFirstName() +
                "," +
                customer.getLastName() +
                "," +
                customer.getEmail() +
                "," +
                customer.getPassword() +
                "," +
                customer.getCnp() +
                "," +
                customer.getPassword() + "\n";
    }

    // Admin
    public void writeAdminInCsv(Admin admin) throws FileNotFoundException;

    private static String convertToCsvFormat(Admin admin) {

        return  admin.getFirstName() +
                "," +
                admin.getLastName() +
                "," +
                admin.getEmail() +
                "," +
                admin.getPassword() + "\n";
    }

    public void writeStandardCardInCsv(StandardCard standardCard) throws FileNotFoundException;

    private static String convertToCsvFormat(StandardCard standardCard) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(standardCard.getExpirationDate());
        return  standardCard.getUserUniqueId() +
                "," +
                standardCard.getCardNumber() +
                "," +
                strDate +
                "," +
                standardCard.getAmount() +
                "," +
                standardCard.getWithdrawFee() + "\n";
    }


    public void writePremiumCardInCsv(PremiumCard premiumCard) throws FileNotFoundException;

    private static String convertToCsvFormat(PremiumCard premiumCard) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(premiumCard.getExpirationDate());
        return  premiumCard.getUserUniqueId() +
                "," +
                premiumCard.getCardNumber() +
                "," +
                strDate +
                "," +
                premiumCard.getAmount() +
                "," +
                premiumCard.getCashBack() + "\n";
    }
}
