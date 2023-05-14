package com.company;

import com.company.services.implement.MainService;

import java.text.ParseException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws ParseException {
        MainService mainService = MainService.getInstance();
        mainService.readFromCsv();
    }
}