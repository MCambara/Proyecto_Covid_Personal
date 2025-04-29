package org.prograIII.collectors;

import org.prograIII.db.model.ProvinceModel;
import org.prograIII.util.ProvinceLoader;

import java.util.LinkedList;

public class ProvinceCollector {
    private final LinkedList<ProvinceModel> provinces = new LinkedList<>();

    public void collect(ProvinceLoader loader) {
        ProvinceModel province = new ProvinceModel(
                loader.getIso(),
                loader.getProvince(),
                loader.getName(),
                loader.getLat(),
                loader.getLng()
        );
        provinces.add(province);
    }

    public LinkedList<ProvinceModel> getProvinces() {
        return provinces;
    }
}