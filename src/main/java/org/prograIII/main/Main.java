package org.prograIII.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prograIII.thread.CovidThread;
import org.prograIII.util.ReportQueryExecutor;

import java.util.Scanner;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String queryDate = "2024-03-09";
        String iso = "USA";

        logger.info("Seleccione una opción:");
        logger.info("1. Insertar datos con el hilo.");
        logger.info("2. Mostrar datos existentes por ISO y fecha.");

        int option = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        switch (option) {
            case 1:
                logger.info("[INFO] Iniciando el hilo para insertar datos...");
                CovidThread.startThreadWithDelay();
                break;

            case 2:
                logger.info("[INFO] Ejecutando consulta para ISO '{}' y fecha '{}'.", iso, queryDate);
                ReportQueryExecutor queryExecutor = new ReportQueryExecutor(queryDate, iso);
                queryExecutor.run();
                break;
            default:
                logger.error("[ERROR] Opción no válida.");
        }

        scanner.close();
    }
}