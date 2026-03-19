package com.neuedu.service.registration;

import com.neuedu.bean.RestBean;

import java.util.Map;
//患者挂号操作
public interface RegisterService {

	String addRegister(Map<String, Object> map);

	String getAlreadyRegisterCount(String visitDate, String noon, String employeeId);

	String getMaxCaseNumber();

	//退号
	RestBean refundMedicalRecord(Integer id);

	RestBean getRecordRefundPatient(String case_number, String real_name
			, Integer nowPageNumber,Integer pageSize);

	RestBean treatPatient(Integer id);
	
}
