package org.prograIII.db.service;

import org.prograIII.db.dao.RegionDao;
import org.prograIII.db.model.RegionModel;
import java.util.List;

public class RegionService {
    private final RegionDao regionDao;

    public RegionService() {
        this.regionDao = new RegionDao();
    }

    public boolean saveRegion(RegionModel region) {
        if (!regionDao.exists(region)) {
            return regionDao.save(region);
        }
        return false;
    }

    public List<RegionModel> getAllRegions() {
        return regionDao.getAll();
    }

    public RegionModel getRegionById(int id) {
        return regionDao.getById(id);
    }
}
