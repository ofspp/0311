package com.neuedu.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface MedicalTechnologyMapper {

	List<Map<String, Object>> getPhysicianCheckSearch(Map<String, Object> map);

	List<Map<String, Object>> getPhysicianInspectionSearch(Map<String, Object> map);

	List<Map<String, Object>> getPhysicianDisposalSearch(Map<String, Object> map);
	
}
