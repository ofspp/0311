package com.neuedu.service.disposal;

import com.neuedu.bean.RestBean;

import java.util.List;
import java.util.Map;

public interface DisposalPatientService {

	List<Map<String, Object>> getDisposalPatient(String register_id);

	List<Map<String, Object>> getDisposalPatientDept();

	List<Map<String, Object>> getDisposalPatientEmp(String deptment_id);

	RestBean updataDisposalPatient(String id, String disposal_employee_id);

}
