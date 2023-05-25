package com.company.services.implement;

import com.company.services.AuditServiceInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuditService implements AuditServiceInterface
{
    private static AuditService instance;
    public static AuditService getInstance(){
        if(instance == null){
            instance = new AuditService();
        }
        return instance;
    }

    public void writeActionInCsv(String action) throws FileNotFoundException
    {
        System.out.println("AUDIT!");
        File csvOutputFile = new File("/home/alien14/Projects/Proiect PAO/src/main/java/com/company/resources/audit.csv");

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date currentDateAndTime = new Date(System.currentTimeMillis());


        String log = action + "," + currentDateAndTime + "\n";

        try (PrintWriter pw = new PrintWriter(new FileOutputStream(csvOutputFile, true))) {
            pw.append(log);
        }
    }
}
