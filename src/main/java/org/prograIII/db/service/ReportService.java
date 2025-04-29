package org.prograIII.db.service;

import org.prograIII.db.dao.ReportDao;
import org.prograIII.db.model.ReportModel;

import java.util.TreeMap;

public class ReportService {
    private final ReportDao reportDao;

    public ReportService() {
        this.reportDao = new ReportDao();
    }

    // Guardar un reporte
    public boolean saveReport(ReportModel report) {
        return reportDao.save(report);
    }

    public TreeMap<String, ReportModel> getReportsByDateAndIso(String date, String iso) {
        return reportDao.findByDateAndIso(date, iso);
    }
}
