package com.company.services.implement;

import com.company.services.CsvReaderServiceInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderService implements CsvReaderServiceInterface {

    private static CsvReaderService instance;

    public static CsvReaderService getInstance(){
        if(instance == null){
            instance = new CsvReaderService();
        }
        return instance;
    }

    public List<String[]> readCustomersFromCsv()
    {
        List<String[]> stringList = new ArrayList<>();
        String line = "";
        String splitBy = ",";
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("\\home\\alien14\\Projects\\Proiect PAO\\src\\main\\java\\com\\company\\resources\\customers.csv"));
            br.readLine(); // read header
            while ((line = br.readLine()) != null)
            //returns a Boolean value
            {
                String[] customer = line.split(splitBy);
                stringList.add(customer);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }

    public List<String[]> readAdminsFromCsv()
    {
        List<String[]> stringList = new ArrayList<>();
        String line = "";
        String splitBy = ",";
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("src\\main\\java\\com\\company\\resources\\admins.csv"));
            br.readLine(); // read header
            while ((line = br.readLine()) != null)
            //returns a Boolean value
            {
                String[] admin = line.split(splitBy);
                stringList.add(admin);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }
    public List<String[]> readStandardCardsFromCsv()
    {
        List<String[]> stringList = new ArrayList<>();
        String line = "";
        String splitBy = ",";
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("src\\main\\java\\com\\company\\resources\\standardcards.csv"));
            br.readLine(); // read header
            while ((line = br.readLine()) != null)
            //returns a Boolean value
            {
                String[] card = line.split(splitBy);
                stringList.add(card);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }

    public List<String[]> readPremiumCardsFromCsv()
    {
        List<String[]> stringList = new ArrayList<>();
        String line = "";
        String splitBy = ",";
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("src\\main\\java\\com\\company\\resources\\premiumcards.csv"));
            br.readLine(); // read header
            while ((line = br.readLine()) != null)
            //returns a Boolean value
            {
                String[] card = line.split(splitBy);
                stringList.add(card);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }
}
