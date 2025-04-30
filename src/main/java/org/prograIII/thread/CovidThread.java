package org.prograIII.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prograIII.processor.ExecutionProcessor;
import org.prograIII.processor.ProvinceProcessor;
import org.prograIII.processor.RegionProcessor;
import org.prograIII.processor.ReportProcessor;
import org.prograIII.util.PropertyReader;

import java.util.Set;

public class CovidThread implements Runnable {

    private static final Logger logger = LogManager.getLogger(CovidThread.class);

    // Ejecuta el procesamiento de regiones, provincias, reportes y ejecuciones para una fecha específica
    @Override
    public void run() {
        try {
            String queryDate = PropertyReader.getCovidApiTargetDate();

            logger.info("[INFO] Starting processing for date: {}", queryDate);

            // Procesa las regiones para la fecha indicada y obtiene los ISOs a procesar
            RegionProcessor regionProcessor = new RegionProcessor();
            Set<String> newIsos = regionProcessor.processRegions(queryDate);

            if (newIsos.isEmpty()) {
                logger.info("[INFO] No new ISOs to process.");
                return;
            }

            // Procesa las provincias para los ISOs obtenidos
            ProvinceProcessor provinceProcessor = new ProvinceProcessor();
            provinceProcessor.processProvinces(newIsos);

            // Procesa los reportes para los ISOs y la fecha indicada
            ReportProcessor reportProcessor = new ReportProcessor();
            reportProcessor.processReports(newIsos, queryDate);

            // Procesa las ejecuciones para los ISOs y la fecha indicada
            ExecutionProcessor executionProcessor = new ExecutionProcessor();
            executionProcessor.processExecutions(newIsos, queryDate);

            logger.info("[INFO] Query finished for date: {}", queryDate);

        } catch (Exception e) {
            logger.error("[ERROR] Error during thread execution: ", e);
        }
    }

    public static void startThreadWithDelay() {
        try {
            logger.info("[INFO] Waiting 15 seconds before starting the thread...");
            Thread.sleep(15000);
            new Thread(new CovidThread()).start();
            logger.info("[INFO] Thread started.");
        } catch (InterruptedException e) {
            logger.error("[ERROR] Error waiting to start the thread: ", e);
        }
    }
}
