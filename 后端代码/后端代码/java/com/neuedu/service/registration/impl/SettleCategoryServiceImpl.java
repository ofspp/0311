package com.neuedu.service.registration.impl;

import com.neuedu.mapper.SettleCategoryMapper;
import com.neuedu.service.registration.SettleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class SettleCategoryServiceImpl implements SettleCategoryService {
	@Autowired
	private SettleCategoryMapper settleCategoryMapper;
	@Override
	public List<Map<String, Object>> getSettleCategory() {
		return settleCategoryMapper.getSettleCategory();
	}

}
