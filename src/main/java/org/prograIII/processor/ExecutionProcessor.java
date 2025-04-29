package org.prograIII.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prograIII.collectors.ExecutionCollector;
import org.prograIII.db.model.ExecutionModel;
import org.prograIII.db.service.ExecutionService;

import java.util.Set;

public class ExecutionProcessor {

    private static final Logger logger = LogManager.getLogger(ExecutionProcessor.class);
    private final ExecutionService executionService = new ExecutionService();

    public void processExecutions(Set<String> isoSet, String queryDate) {
        logger.info("[INFO] Saving execution data...");
        ExecutionCollector executionCollector = new ExecutionCollector();
        executionCollector.collect(isoSet, queryDate);

        executionCollector.getExecutions().forEach(execution -> {
            boolean success = executionService.saveExecution(execution);
            logger.info(success ? "[INFO] Execution inserted: {}" : "[ERROR] Could not insert execution: {}", execution);
        });
        logger.info("[INFO] Finished saving execution data.");
    }
}
