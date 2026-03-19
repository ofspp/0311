package com.neuedu.service.physician;

import com.neuedu.bean.RestBean;

import java.util.Map;

public interface PhysicianPatientService {

	Integer getFinishPatientCount(Integer employee_id);

	Integer getWaitPatientCount(Integer employee_id);

	RestBean getWaitPatient(Integer employee_id,String case_number,String real_name);

	RestBean getPatientQuery(Map<String, Object> map);

	RestBean getPatientItemByItemtype(Integer register_id, String string);

}
