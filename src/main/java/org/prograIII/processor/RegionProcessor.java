package org.prograIII.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prograIII.collectors.RegionCollector;
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

    // Procesa las regiones usando el collector y devuelve el conjunto de ISOs a insertar
    public Set<String> processRegions(String queryDate) {
        Map<Integer, Map<String, String>> regionsMap = RegionLoader.loadRegions();
        Set<String> isoSet = new HashSet<>();

        if (regionsMap.isEmpty()) {
            logger.info("[INFO] No regions found.");
            return isoSet;
        }

        logger.info("[INFO] Processing {} regions for date '{}'.", regionsMap.size(), queryDate);

        // Cargar ejecuciones previas
        List<ExecutionModel> existingExecutions = executionService.getAllExecutions();
        Set<String> existingIsoDatePairs = new HashSet<>();
        for (ExecutionModel execution : existingExecutions) {
            existingIsoDatePairs.add(execution.getCountryIso() + "|" + execution.getExecutionDate());
        }

        // Recolectar regiones usando el collector
        RegionCollector collector = new RegionCollector();
        collector.collect(regionsMap);
        LinkedList<RegionModel> regionList = collector.getRegions();

        for (RegionModel region : regionList) {
            String iso = region.getIso();
            String name = region.getName();
            String isoDatePair = iso + "|" + queryDate;

            if (existingIsoDatePairs.contains(isoDatePair)) {
                logger.info("[INFO] ISO '{}' omitted because it already exists with date '{}'.", iso, queryDate);
            } else {
                logger.info("[INFO] ISO '{}' will be processed and saved for date '{}'.", iso, queryDate);
                isoSet.add(iso);
                boolean inserted = regionService.saveRegion(new RegionModel(0, iso, name));
                logger.info(inserted ? "[INFO] Region inserted: {}" : "[INFO] Region already exists or could not be inserted: {}", region);
            }
        }

        return isoSet;
    }
}
