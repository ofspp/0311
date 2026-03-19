package com.neuedu.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RegistLevelMapper {
	//得到所有挂号级别
	List<Map<String,Object>> getRegistLevelList();
    
}
