package com.neuedu.service.check.impl;

import com.neuedu.bean.RestBean;
import com.neuedu.mapper.CheckRequestMapper;
import com.neuedu.service.check.CheckInputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
@Service
public class CheckInputServiceImpl implements CheckInputService {
	@Autowired
	private CheckRequestMapper checkRequestMapper;
	@Override
	public RestBean getFinishCheck(String case_number, String real_name, Integer nowPageNumber, Integer pageSize) {
		RestBean rest = new RestBean();
		//完成检查患者信息
		List<Map<String,Object>> list = checkRequestMapper.getFinishCheck(case_number,real_name,nowPageNumber,pageSize);
		rest.setList(list);
		//完成检查的患者数量		
		Integer count = checkRequestMapper.getFinishCheckCountNumber(case_number,real_name);
		rest.setTotalCount(count);
		return rest;
	}
	@Override
	public List<Map<String, Object>> getFinishCheckByRegistid(String id) {
		System.out.println("id-->getFinishCheckByRegistid" + id);
		List<Map<String,Object>> list = checkRequestMapper.getFinishCheckByRegistid(id);
		System.out.println("result-->" + list);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		for (Map<String, Object> m : list) {
			Object ct = m.get("check_time");
			if (ct != null) {
				if (ct instanceof LocalDateTime) {
					String s = ((LocalDateTime) ct).format(dtf);
					m.put("check_time", s);
				} else if (ct instanceof Timestamp) {
					String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Timestamp) ct);
					m.put("check_time", s);
				}
			}
		}
		return list;
	}
	@Override
	public RestBean updateCheckInput(String id, String employee_id, String check_result) {
		RestBean rest = new RestBean();
		checkRequestMapper.updateCheckInput(id,employee_id,check_result);
		rest.setMsg("添加成功");
		return rest;
	}

}
