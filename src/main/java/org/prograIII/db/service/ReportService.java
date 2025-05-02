package org.prograIII.db.service;

import org.prograIII.db.dao.ReportDao;
import org.prograIII.db.model.ReportModel;
import java.util.TreeMap;

public class ReportService {
    private final ReportDao reportDao;

    public ReportService() {
        this.reportDao = new ReportDao();
    }

    public boolean saveReport(ReportModel report) {
        if (!reportDao.exists(report)) {
            return reportDao.save(report);
        }
        return false;
    }

    public TreeMap<String, ReportModel> getReportsByDateAndIso(String date, String iso) {
        return reportDao.findByDateAndIso(date, iso);
    }
}
