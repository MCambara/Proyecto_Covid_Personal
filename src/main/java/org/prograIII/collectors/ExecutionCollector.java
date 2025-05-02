package org.prograIII.collectors;

import org.prograIII.db.model.ExecutionModel;
import java.util.LinkedList;
import java.util.Set;

public class ExecutionCollector {
    private final LinkedList<ExecutionModel> executions = new LinkedList<>();

    // Guarda una ejecución por cada ISO recibido
    public void collect(Set<String> isoSet, String executionDate) {
        for (String iso : isoSet) {
            ExecutionModel execution = new ExecutionModel(executionDate, iso);
            executions.add(execution);
        }
    }

    // Devuelve todas las ejecuciones guardadas
    public LinkedList<ExecutionModel> getExecutions() {
        return executions;
    }
}
