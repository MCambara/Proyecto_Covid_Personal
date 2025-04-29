package org.prograIII.util;

import org.prograIII.db.model.ReportModel;
import org.prograIII.db.service.ReportService;

import java.util.Map;
import java.util.TreeMap;

public class ReportQueryExecutor implements Runnable {

    private final String date;
    private final String iso;

    // Constructor con los parámetros necesarios
    public ReportQueryExecutor(String date, String iso) {
        this.date = date;
        this.iso = iso;
    }

    @Override
    public void run() {
        ReportService service = new ReportService();

        // Obtener los reportes filtrados por fecha e ISO
        TreeMap<String, ReportModel> reports = service.getReportsByDateAndIso(date, iso);

        // Imprimir encabezados con un poco más de espacio para la provincia
        System.out.printf("%-30s %-10s %-12s %-12s %-12s %-15s %-20s\n",
                "Provincia", "ISO", "Fecha", "Confirmados", "Muertes", "Recuperados", "Región");

        // Mostrar los resultados en consola
        if (reports.isEmpty()) {
            System.out.println("No reports found for date " + date + " and ISO " + iso);
        } else {
            // Mostrar los resultados de los reportes con formato alineado
            for (Map.Entry<String, ReportModel> entry : reports.entrySet()) {
                ReportModel report = entry.getValue();
                System.out.printf("%-30s %-10s %-12s %-12d %-12d %-15d %-20s\n",
                        entry.getKey(), // Provincia
                        iso,            // ISO
                        date,           // Fecha
                        report.getConfirmed(), // Confirmados
                        report.getDeaths(),    // Muertes
                        report.getRecovered(), // Recuperados
                        report.getRegionName()); // Región
            }
        }
    }
}
