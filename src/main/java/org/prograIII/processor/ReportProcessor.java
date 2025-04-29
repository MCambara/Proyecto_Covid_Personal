package org.prograIII.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prograIII.collectors.ReportCollector;
import org.prograIII.covidApis.CovidReports;
import org.prograIII.db.service.ReportService;
import org.prograIII.util.ReportLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportProcessor {

    private static final Logger logger = LogManager.getLogger(ReportProcessor.class);
    private final ReportService reportService = new ReportService();

    public void processReports(Set<String> isoSet, String queryDate) {
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
            boolean success = reportService.saveReport(report);
            logger.info(success ? "[INFO] Report inserted: {}" : "[ERROR] Could not insert report: {}", report);
        });
        logger.info("[INFO] Finished inserting reports.");
    }
}
