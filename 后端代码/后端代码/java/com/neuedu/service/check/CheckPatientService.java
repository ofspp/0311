package com.neuedu.service.check;

import com.neuedu.bean.RestBean;

import java.util.List;
import java.util.Map;

public interface CheckPatientService {

	List<Map<String, Object>> getCheckPatient(String register_id);

	List<Map<String, Object>> getCheckPatientDept();

	List<Map<String, Object>> getCheckPatientEmp(String deptment_id);

	RestBean updataCheckPatient(String id, String check_employee_id);

}
