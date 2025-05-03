package org.prograIII.thread;

import org.prograIII.db.model.ReportModel;
import org.prograIII.db.service.ReportService;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ReportQueryExecutor implements Runnable {

    private static final Logger logger = Logger.getLogger(ReportQueryExecutor.class.getName());

    static {
        // Configurar el logger para imprimir solo el mensaje (sin nivel ni timestamp)
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getMessage() + "\n";
            }
        });

        logger.setUseParentHandlers(false); // Desactiva handlers por defecto
        logger.addHandler(handler);        // Añade nuestro handler limpio
    }

    private final String date;
    private final String iso;

    public ReportQueryExecutor(String date, String iso) {
        this.date = date;
        this.iso = iso;
    }

    // Ejecuta la consulta para recuperar los reportes por fecha e ISO, luego imprímelos con logs

    @Override
    public void run() {
        ReportService service = new ReportService();

        TreeMap<String, ReportModel> reports = service.getReportsByDateAndIso(date, iso);

        logger.info(String.format("%-5s %-30s %-10s %-12s %-12s %-12s %-15s %-20s",
                "No.", "Province", "ISO", "Date", "Confirmed", "Deaths", "Recovered", "Region"));

        if (reports.isEmpty()) {
            logger.info("No reports found for date " + date + " and ISO " + iso);
        } else {
            int index = 1;
            for (Map.Entry<String, ReportModel> entry : reports.entrySet()) {
                ReportModel report = entry.getValue();
                logger.info(String.format("%-5d %-30s %-10s %-12s %-12d %-12d %-15d %-20s",
                        index++,
                        entry.getKey(),
                        iso,
                        date,
                        report.getConfirmed(),
                        report.getDeaths(),
                        report.getRecovered(),
                        report.getRegionName()));
            }
            logger.info("\nTotal records: " + (index - 1));
        }
    }
}
