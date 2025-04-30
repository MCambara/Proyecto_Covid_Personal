package org.prograIII.thread;

import org.prograIII.db.model.ReportModel;
import org.prograIII.db.service.ReportService;

import java.util.Map;
import java.util.TreeMap;

public class ReportQueryExecutor implements Runnable {

    private final String date;
    private final String iso;

    public ReportQueryExecutor(String date, String iso) {
        this.date = date;
        this.iso = iso;
    }

    // Executes the query to fetch reports by date and ISO, and prints them to the console
    @Override
    public void run() {
        ReportService service = new ReportService();

        TreeMap<String, ReportModel> reports = service.getReportsByDateAndIso(date, iso);

        System.out.printf("%-30s %-10s %-12s %-12s %-12s %-15s %-20s\n",
                "Province", "ISO", "Date", "Confirmed", "Deaths", "Recovered", "Region");

        if (reports.isEmpty()) {
            System.out.println("No reports found for date " + date + " and ISO " + iso);
        } else {

            for (Map.Entry<String, ReportModel> entry : reports.entrySet()) {
                ReportModel report = entry.getValue();
                System.out.printf("%-30s %-10s %-12s %-12d %-12d %-15d %-20s\n",
                        entry.getKey(),
                        iso,
                        date,
                        report.getConfirmed(),
                        report.getDeaths(),
                        report.getRecovered(),
                        report.getRegionName());
            }
        }
    }
}
