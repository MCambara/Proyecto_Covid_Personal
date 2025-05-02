package org.prograIII.covidApis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
import org.prograIII.util.PropertyReader;

public class CovidRegions {

    private static final Logger logger = Logger.getLogger(CovidRegions.class.getName());

    private static final String API_URL = PropertyReader.getCovidApiRegionsUrl();
    private static final String API_KEY = PropertyReader.getCovidApiKey();
    private static final String API_HOST = PropertyReader.getCovidApiHost();

    public String fetchRegionJson() {
        try {
            logger.info("[API] Fetching regions from API...");

            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-RapidAPI-Key", API_KEY);
            conn.setRequestProperty("X-RapidAPI-Host", API_HOST);

            int responseCode = conn.getResponseCode();
            logger.info("[API] Response Code: " + responseCode);

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } else {
                logger.warning("[API] Failed to fetch regions. Response Code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            logger.severe("[API] Error fetching regions: " + e.getMessage());
            return null;
        }
    }
}
