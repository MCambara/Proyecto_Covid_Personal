package org.prograIII.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prograIII.collectors.ProvinceCollector;
import org.prograIII.covidApis.CovidProvinces;
import org.prograIII.db.service.ProvinceService;
import org.prograIII.util.ProvinceLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProvinceProcessor {

    private static final Logger logger = LogManager.getLogger(ProvinceProcessor.class);
    private final ProvinceService provinceService = new ProvinceService();

    public void processProvinces(Set<String> isoSet) {
        logger.info("[INFO] Fetching provinces from API...");
        CovidProvinces service = new CovidProvinces();
        Map<String, List<ProvinceLoader>> allData = service.fetchAllRegionData();
        logger.info("[INFO] Finished fetching provinces.");

        ProvinceCollector provinceCollector = new ProvinceCollector();

        // Contador de provincias procesadas
        int processedProvincesCount = 0;

        for (String iso : isoSet) {
            logger.info("[INFO] Processing provinces for ISO: {}", iso);
            List<ProvinceLoader> regionList = allData.get(iso);
            if (regionList != null) {
                regionList.forEach(provinceCollector::collect);
                processedProvincesCount += regionList.size();  // Contar cuántas provincias se procesaron para este ISO
            }
        }

        logger.info("[INFO] Total provinces fetched for date: {}", processedProvincesCount);

        logger.info("[INFO] Inserting provinces into the database...");
        provinceCollector.getProvinces().forEach(province -> {
            boolean success = provinceService.saveProvince(province);
            logger.info(success ? "[INFO] Province inserted: {}" : "[ERROR] Could not insert province: {}", province.getProvince());
        });

        logger.info("[INFO] Finished inserting provinces.");
    }
}
