package com.neuedu.service.drugstore;

import com.neuedu.bean.RestBean;

import java.util.List;
import java.util.Map;

public interface GiveMedicineService {

	RestBean getFinishPricePatient(Map<String, Object> map);

	List<Map<String, Object>> getPricePatientDrug(Integer id);

	RestBean givePatientDrugs(Integer id);

}
