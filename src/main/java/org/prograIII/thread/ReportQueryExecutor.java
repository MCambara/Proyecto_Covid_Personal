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

    // Ejecuta la consulta para recuperar los reportes por fecha e ISO, luego imprímelos en la consola

    @Override
    public void run() {
        ReportService service = new ReportService();

        TreeMap<String, ReportModel> reports = service.getReportsByDateAndIso(date, iso);

        System.out.printf("%-5s %-30s %-10s %-12s %-12s %-12s %-15s %-20s\n",
                "No.", "Province", "ISO", "Date", "Confirmed", "Deaths", "Recovered", "Region");

        if (reports.isEmpty()) {
            System.out.println("No reports found for date " + date + " and ISO " + iso);
        } else {
            int index = 1;
            for (Map.Entry<String, ReportModel> entry : reports.entrySet()) {
                ReportModel report = entry.getValue();
                System.out.printf("%-5d %-30s %-10s %-12s %-12d %-12d %-15d %-20s\n",
                        index++,
                        entry.getKey(),
                        iso,
                        date,
                        report.getConfirmed(),
                        report.getDeaths(),
                        report.getRecovered(),
                        report.getRegionName());
            }
            System.out.println("\nTotal records: " + (index - 1));
        }
    }
}
