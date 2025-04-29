package org.prograIII.thread;

import org.prograIII.collectors.ExecutionCollector;
import org.prograIII.collectors.ProvinceCollector;
import org.prograIII.collectors.ReportCollector;
import org.prograIII.covidApis.CovidProvinces;
import org.prograIII.covidApis.CovidReports;
import org.prograIII.db.model.ExecutionModel;
import org.prograIII.db.model.RegionModel;
import org.prograIII.db.service.ExecutionService;
import org.prograIII.db.service.ProvinceService;
import org.prograIII.db.service.RegionService;
import org.prograIII.db.service.ReportService;
import org.prograIII.util.ProvinceLoader;
import org.prograIII.util.RegionLoader;
import org.prograIII.util.ReportLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CovidThread implements Runnable {

    private static final Logger logger = LogManager.getLogger(CovidThread.class);
    private final ProvinceService provinceService = new ProvinceService();
    private final ReportService reportService = new ReportService();
    private final ExecutionService executionService = new ExecutionService();
    private final RegionService regionService = new RegionService();

    @Override
    public void run() {
        try {
            logger.info("[INFO] Starting RegionLoader test...");
            Map<Integer, Map<String, String>> regions = RegionLoader.loadRegions();

            if (regions.isEmpty()) {
                logger.info("[INFO] No regions found.");
                return;
            }

            // Obtener todos los registros existentes en executed_reports
            List<ExecutionModel> existingExecutions = executionService.getAllExecutions();
            Set<String> existingIsoDatePairs = new HashSet<>();
            for (ExecutionModel execution : existingExecutions) {
                existingIsoDatePairs.add(execution.getCountryIso() + "|" + execution.getExecutionDate());
            }

            // Fecha que se usará para los reportes
            String queryDate = "2021-03-09";

            // Filtrar ISOs que ya existen con la misma fecha
            Set<String> isoSet = new HashSet<>();
            for (Map<String, String> values : regions.values()) {
                String iso = values.get("iso");
                String name = values.get("name");
                if (iso != null && !iso.isBlank()) {
                    String isoDatePair = iso + "|" + queryDate;
                    if (existingIsoDatePairs.contains(isoDatePair)) {
                        logger.info("[INFO] ISO '{}' omitted because it already exists with date '{}'.", iso, queryDate);
                    } else {
                        isoSet.add(iso);
                        logger.info("[INFO] ISO '{}' will be processed.", iso);

                        // Insertar en la tabla regions
                        RegionModel region = new RegionModel(0, iso, name);
                        boolean regionInserted = regionService.saveRegion(region);
                        if (regionInserted) {
                            logger.info("[INFO] Region inserted: {}", region);
                        } else {
                            logger.error("[ERROR] Could not insert region: {}", region);
                        }

                        // Guardar inmediatamente en executed_reports
                        ExecutionModel execution = new ExecutionModel(queryDate, iso);
                        boolean executionInserted = executionService.saveExecution(execution);
                        if (executionInserted) {
                            logger.info("[INFO] Execution record inserted for ISO: {}", iso);
                        } else {
                            logger.error("[ERROR] Could not insert execution record for ISO: {}", iso);
                        }
                    }
                }
            }

            if (isoSet.isEmpty()) {
                logger.info("[INFO] No new ISOs to process.");
                return;
            }

            // Procesar provincias
            logger.info("[INFO] Fetching provinces from API...");
            CovidProvinces service = new CovidProvinces();
            Map<String, List<ProvinceLoader>> allData = service.fetchAllRegionData();
            logger.info("[INFO] Finished fetching provinces.");

            ProvinceCollector provinceCollector = new ProvinceCollector();
            isoSet.forEach(iso -> {
                logger.info("[INFO] Processing provinces for ISO: {}", iso);
                List<ProvinceLoader> regionList = allData.get(iso);
                if (regionList != null) {
                    regionList.forEach(provinceCollector::collect);
                }
            });

            logger.info("[INFO] Inserting provinces into the database...");
            provinceCollector.getProvinces().forEach(province -> {
                boolean success = provinceService.saveProvince(province);
                if (success) {
                    logger.info("[INFO] Province inserted: {}", province.getProvince());
                } else {
                    logger.error("[ERROR] Could not insert province: {}", province.getProvince());
                }
            });
            logger.info("[INFO] Finished inserting provinces.");

            // Procesar reportes
            logger.info("[INFO] Fetching COVID reports from API...");
            CovidReports covidReportsService = new CovidReports();
            Map<String, List<ReportLoader>> covidReports = covidReportsService.fetchCovidDataForAllProvinces(isoSet, queryDate);
            logger.info("[INFO] Finished fetching COVID reports.");

            if (covidReports.isEmpty()) {
                logger.info("[INFO] No reports found for the given ISOs and date.");
                return;
            }

            ReportCollector reportCollector = new ReportCollector();
            covidReports.forEach((iso, reportList) -> {
                logger.info("[INFO] Processing reports for ISO: {}", iso);
                reportList.forEach(reportCollector::collect);
            });

            logger.info("[INFO] Inserting reports into the database...");
            reportCollector.getReports().forEach(report -> {
                boolean reportSuccess = reportService.saveReport(report);
                if (reportSuccess) {
                    logger.info("[INFO] Report inserted into the database: {}", report);
                } else {
                    logger.error("[ERROR] Could not insert the report: {}", report);
                }
            });
            logger.info("[INFO] Finished inserting reports.");

            // Guardar datos de ejecución
            logger.info("[INFO] Saving execution data...");
            ExecutionCollector executionCollector = new ExecutionCollector();
            executionCollector.collect(isoSet, queryDate);

            executionCollector.getExecutions().forEach(execution -> {
                boolean executionSuccess = executionService.saveExecution(execution);
                if (executionSuccess) {
                    logger.info("[INFO] Execution data inserted: {}", execution);
                } else {
                    logger.error("[ERROR] Could not insert execution data: {}", execution);
                }
            });
            logger.info("[INFO] Finished saving execution data.");

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