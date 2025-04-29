package org.prograIII.collectors;

import org.prograIII.db.model.ReportModel;
import org.prograIII.util.ReportLoader;

import java.util.LinkedList;

public class ReportCollector {
    private final LinkedList<ReportModel> reports = new LinkedList<>();

    public void collect(ReportLoader loader) {
        ReportModel report = new ReportModel(
                0, // ID inicializado en 0
                loader.getDate(),
                loader.getConfirmed(),
                loader.getDeaths(),
                loader.getRecovered(),
                loader.getIso(),
                loader.getRegionName(),
                loader.getProvince()
        );
        reports.add(report);
    }

    public LinkedList<ReportModel> getReports() {
        return reports;
    }
}