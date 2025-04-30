package org.prograIII.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prograIII.thread.CovidThread;
import org.prograIII.util.PropertyReader;
import org.prograIII.thread.ReportQueryExecutor;

import java.util.Scanner;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String queryDate = PropertyReader.getCovidQueryDate();
        String iso = PropertyReader.getCovidIso();

        logger.info("Select an option:");
        logger.info("1. Insert data using the thread.");
        logger.info("2. Show existing data by ISO and date.");
        System.out.print("Option: ");

        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                logger.info("[INFO] Starting the thread to insert data...");
                CovidThread.startThreadWithDelay();
                break;

            case 2:
                logger.info("[INFO] Running query for ISO '{}' and date '{}'.", iso, queryDate);
                ReportQueryExecutor queryExecutor = new ReportQueryExecutor(queryDate, iso);
                queryExecutor.run();
                break;
            default:
                logger.error("[ERROR] Invalid option.");
        }
        scanner.close();
    }
}
