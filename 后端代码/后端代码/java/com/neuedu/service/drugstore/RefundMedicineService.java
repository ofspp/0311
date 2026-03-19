package com.neuedu.service.drugstore;

import com.neuedu.bean.RestBean;

import java.util.List;
import java.util.Map;

public interface RefundMedicineService {

	RestBean getGivePricePatient(Map<String, Object> map);

	List<Map<String, Object>> getGivePatientDrug(Integer id);

	RestBean refundPatientDrugs(Integer id);

}
