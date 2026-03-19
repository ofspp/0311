package com.neuedu.service.inspection;

import com.neuedu.bean.RestBean;

import java.util.List;
import java.util.Map;

public interface InspectionPatientService {

	List<Map<String, Object>> getInspectionPatient(String register_id);

	List<Map<String, Object>> getInspectionPatientDept();

	List<Map<String, Object>> getInspectionPatientEmp(String deptment_id);

	RestBean updataInspectionPatient(String id, String inspection_employee_id);

}
