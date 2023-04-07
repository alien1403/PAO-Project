package com.company.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface AuditServiceInterface {
    public void writeActionInCsv(String action) throws FileNotFoundException;
}
