package org.prograIII.collectors;

import org.prograIII.db.model.RegionModel;

import java.util.LinkedList;
import java.util.Map;

public class RegionCollector {
    private final LinkedList<RegionModel> regions = new LinkedList<>();

    // Recorre el mapa recibido y construye objetos RegionModel con los datos
    public void collect(Map<Integer, Map<String, String>> regionData) {
        for (Map.Entry<Integer, Map<String, String>> entry : regionData.entrySet()) {
            Map<String, String> data = entry.getValue();
            String iso = data.get("iso");
            String name = data.get("name");

            RegionModel region = new RegionModel(0, iso, name);
            regions.add(region);
        }
    }

    // Devuelve todas las regiones recolectadas
    public LinkedList<RegionModel> getRegions() {
        return regions;
    }
}
