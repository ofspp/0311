package com.neuedu.service.registration;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

	List<Map<String, Object>> getRegistDoctorList(String deptmentId, String registLevelId);

}
