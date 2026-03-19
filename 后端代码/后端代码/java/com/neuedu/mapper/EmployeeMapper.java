package com.neuedu.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface EmployeeMapper {

	List<Map<String, Object>> getRegistDoctorList(String deptmentId, String registLevelId);

	List<Map<String, Object>> getEmployeeByDeptid(String deptment_id);

	List<Map<String, Object>> getEmployeeAndDeptByNamePass(String realname, String password);

}
