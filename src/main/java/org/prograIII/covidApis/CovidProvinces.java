package org.prograIII.covidApis;

import org.prograIII.util.ProvinceLoader;
import org.json.JSONException;
import org.prograIII.util.RegionLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prograIII.util.PropertyReader;

public class CovidProvinces {

    private static final String API_URL = PropertyReader.getCovidApiProvincesUrl();
    private static final String API_KEY = PropertyReader.getCovidApiKey();
    private static final String API_HOST = PropertyReader.getCovidApiHost();
    private static final Logger logger = LogManager.getLogger(CovidProvinces.class);

    public Map<String, List<ProvinceLoader>> fetchAllRegionData() throws JSONException {
        Set<String> isoSet = getIsoSet();
        Map<String, List<ProvinceLoader>> regionDataMap = new HashMap<>();

        logger.info("[INFO] Processing...");

        for (String iso : isoSet) {
            JSONObject response = fetchDataByIso(iso);
            storeProvinceData(iso, response, regionDataMap);
        }

        logger.info("[INFO] Finished processing all ISOs.");
        return regionDataMap;
    }

    private Set<String> getIsoSet() {
        Map<Integer, Map<String, String>> regions = RegionLoader.loadRegions();
        Set<String> isoSet = new HashSet<>();

        for (Map<String, String> values : regions.values()) {
            String iso = values.get("iso");
            if (iso != null && !iso.isBlank()) {
                isoSet.add(iso);
            }
        }

        return isoSet;
    }

    private JSONObject fetchDataByIso(String iso) throws JSONException {
        try {
            URL url = new URL(API_URL + iso);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-RapidAPI-Key", API_KEY);
            conn.setRequestProperty("X-RapidAPI-Host", API_HOST);

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return new JSONObject(response.toString());
            } else {
                return new JSONObject("{\"error\":\"Failed to fetch data for ISO " + iso + "\"}");
            }

        } catch (Exception e) {
            logger.error("[ERROR] Exception for ISO {}: {}", iso, e.getMessage());
            return new JSONObject("{\"error\":\"Exception for ISO " + iso + "\"}");
        }
    }

    private void storeProvinceData(String iso, JSONObject response, Map<String, List<ProvinceLoader>> dataMap) throws JSONException {
        JSONArray dataArray = response.optJSONArray("data");
        if (dataArray == null) return;

        List<ProvinceLoader> regions = new ArrayList<>();

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject obj = dataArray.getJSONObject(i);

            String province = obj.optString("province", "");
            String name = obj.optString("name", "");
            double lat = obj.optDouble("lat", 0.0);
            double lng = obj.optDouble("long", 0.0);

            regions.add(new ProvinceLoader(iso, province, name, lat, lng));
        }

        dataMap.put(iso, regions);
    }
}
