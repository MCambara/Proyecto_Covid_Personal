package org.prograIII.db.service;

import org.prograIII.db.dao.ProvinceDao;
import org.prograIII.db.model.ProvinceModel;

public class ProvinceService {
    private final ProvinceDao provinceDao;

    public ProvinceService() {
        this.provinceDao = new ProvinceDao();
    }

    public boolean saveProvince(ProvinceModel province) {
        if (!provinceDao.exists(province)) {
            return provinceDao.save(province);
        }
        return false;
    }

}
