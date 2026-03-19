package com.neuedu.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
/**
 * 科室操作
 *
 *
 */
@Mapper
public interface DepartmentMapper {
	//得到所有科室
	List<Map<String,Object>> getAllDeptList();

	List<Map<String, Object>> getDeptmentByType(String dept_type);
}
