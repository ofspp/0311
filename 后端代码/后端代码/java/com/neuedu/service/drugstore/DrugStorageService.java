package com.neuedu.service.drugstore;

import com.neuedu.bean.RestBean;

import java.util.Map;

public interface DrugStorageService {

	RestBean getDrugInfo(Map<String, Object> map);

	RestBean addDrug(Map<String, Object> map);

	RestBean updateDrug(Map<String, Object> map);

	RestBean deleteDrug(Integer id);

}
