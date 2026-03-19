package com.neuedu.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DiseaseMapper {
	
	List<Map<String, Object>> getDisease(Map<String, Object> map);

}
