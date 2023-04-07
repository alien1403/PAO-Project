package com.company.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface CsvReaderServiceInterface {

    public List<String[]> readCustomersFromCsv();

    public List<String[]> readAdminsFromCsv();
    public List<String[]> readStandardCardsFromCsv();

    public List<String[]> readPremiumCardsFromCsv();
}
