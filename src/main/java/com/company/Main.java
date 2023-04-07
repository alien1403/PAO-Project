package com.company;

import com.company.services.implement.MainService;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {
        MainService mainService = MainService.getInstance();
        mainService.readFromCsv();
    }
}