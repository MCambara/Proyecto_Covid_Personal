package org.prograIII.db.model;

public class ExecutionModel {
    private String executionDate;
    private String countryIso;


    public ExecutionModel(String executionDate, String countryIso) {
        this.executionDate = executionDate;
        this.countryIso = countryIso;
    }


    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    @Override
    public String toString() {
        return "ExecutionModel{" +
                "executionDate='" + executionDate + '\'' +
                ", countryIso='" + countryIso + '\'' +
                '}';
    }
}