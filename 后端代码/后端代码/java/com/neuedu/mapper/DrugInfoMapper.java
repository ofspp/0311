package com.neuedu.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DrugInfoMapper {
	//根据药品名称和拼音助记码进行模糊查询
	List<Map<String, Object>> searchDrug(Map<String, Object> map);

	//根据药品名称和拼音助记码进行模糊查询(分页)
	List<Map<String, Object>> searchPageDrug(Map<String, Object> map);
	int searchPageDrugCount(Map<String, Object> map);

	// 新增药品
	int insertDrug(Map<String, Object> map);

	// 修改药品
	int updateDrug(Map<String, Object> map);

	// 删除药品
	int deleteDrugById(Integer id);
}
