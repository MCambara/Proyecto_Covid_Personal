package org.prograIII.db.service;

import org.prograIII.db.dao.ExecutionDao;
import org.prograIII.db.model.ExecutionModel;

import java.util.List;

public class ExecutionService {
    private final ExecutionDao executionDao;

    public ExecutionService() {
        this.executionDao = new ExecutionDao();
    }

    public boolean saveExecution(ExecutionModel execution) {
        return executionDao.save(execution);
    }

    public List<ExecutionModel> getAllExecutions() {
        return executionDao.getAll();
    }
}