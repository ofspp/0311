package com.neuedu.service.physician;

import com.neuedu.bean.RestBean;

import java.util.List;
import java.util.Map;

public interface WritePrescriptionService {

	List<Map<String, Object>> searchDrug(Map<String, Object> map);

	RestBean addPrescription(List<Map<String, Object>> submit_prescription);

}
