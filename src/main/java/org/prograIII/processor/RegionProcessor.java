package org.prograIII.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prograIII.db.model.ExecutionModel;
import org.prograIII.db.model.RegionModel;
import org.prograIII.db.service.ExecutionService;
import org.prograIII.db.service.RegionService;
import org.prograIII.util.RegionLoader;

import java.util.*;

public class RegionProcessor {

    private static final Logger logger = LogManager.getLogger(RegionProcessor.class);
    private final ExecutionService executionService = new ExecutionService();
    private final RegionService regionService = new RegionService();

    public Set<String> processRegions(String queryDate) {
        Map<Integer, Map<String, String>> regions = RegionLoader.loadRegions();
        Set<String> isoSet = new HashSet<>();

        if (regions.isEmpty()) {
            logger.info("[INFO] No regions found.");
            return isoSet;
        }

        logger.info("[INFO] Processing {} regions for date '{}'.", regions.size(), queryDate);

        List<ExecutionModel> existingExecutions = executionService.getAllExecutions();
        Set<String> existingIsoDatePairs = new HashSet<>();
        for (ExecutionModel execution : existingExecutions) {
            existingIsoDatePairs.add(execution.getCountryIso() + "|" + execution.getExecutionDate());
        }

        for (Map<String, String> values : regions.values()) {
            String iso = values.get("iso");
            String name = values.get("name");
            if (iso != null && !iso.isBlank()) {
                String isoDatePair = iso + "|" + queryDate;
                if (existingIsoDatePairs.contains(isoDatePair)) {
                    logger.info("[INFO] ISO '{}' omitted because it already exists with date '{}'.", iso, queryDate);
                } else {
                    logger.info("[INFO] ISO '{}' will be processed and saved for date '{}'.", iso, queryDate);
                    isoSet.add(iso);
                    RegionModel region = new RegionModel(0, iso, name);
                    boolean regionInserted = regionService.saveRegion(region);
                    logger.info(regionInserted ? "[INFO] Region inserted: {}" : "[ERROR] Could not insert region: {}", region);
                }
            }
        }

        return isoSet;
    }
}
