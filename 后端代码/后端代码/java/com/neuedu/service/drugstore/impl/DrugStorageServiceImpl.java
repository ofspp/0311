package com.neuedu.service.drugstore.impl;

import com.neuedu.bean.RestBean;
import com.neuedu.mapper.DrugInfoMapper;
import com.neuedu.service.drugstore.DrugStorageService;
import com.neuedu.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class DrugStorageServiceImpl implements DrugStorageService {
	@Autowired
	private DrugInfoMapper drugInfoMapper;

	@Override
	public RestBean getDrugInfo(Map<String, Object> map) {
		RestBean rest = new RestBean();
		rest.setList(drugInfoMapper.searchPageDrug(PageUtil.objectToInt(map)));
		rest.setTotalCount(drugInfoMapper.searchPageDrugCount(map));
		return rest;
	}

	@Override
	public RestBean addDrug(Map<String, Object> map) {
		RestBean rest = new RestBean();
		int rows = drugInfoMapper.insertDrug(map);
		rest.setMsg(rows > 0 ? "新增成功" : "新增失败");
		rest.setStatus(rows > 0 ? "success" : "error");
		return rest;
	}

	@Override
	public RestBean updateDrug(Map<String, Object> map) {
		RestBean rest = new RestBean();
		int rows = drugInfoMapper.updateDrug(map);
		rest.setMsg(rows > 0 ? "修改成功" : "修改失败");
		rest.setStatus(rows > 0 ? "success" : "error");
		return rest;
	}

	@Override
	public RestBean deleteDrug(Integer id) {
		RestBean rest = new RestBean();
		int rows = drugInfoMapper.deleteDrugById(id);
		rest.setMsg(rows > 0 ? "删除成功" : "删除失败");
		rest.setStatus(rows > 0 ? "success" : "error");
		return rest;
	}

}
