package com.neuedu.service.physician.impl;

import com.neuedu.mapper.CheckRequestMapper;
import com.neuedu.service.physician.CheckResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class CheckResultsServiceImpl implements CheckResultsService {
	@Autowired
	private CheckRequestMapper checkRequestMapper;

	@Override
	public List<Map<String, Object>> getCheckPatientTableByRegist(String registid) {
		List<Map<String ,Object>> list = checkRequestMapper.getCheckPatientTableByRegist(registid);
		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // yyyy + HH
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		for (Map<String, Object> m : list) {
			// 处理 creation_time
			Object t1 = m.get("creation_time");
			if (t1 != null) {
				String s1;
				if (t1 instanceof LocalDateTime) {
					s1 = ((LocalDateTime) t1).format(dtf);
				} else if (t1 instanceof Timestamp) {
					s1 = smf.format((Timestamp) t1);
				} else if (t1 instanceof Date) {
					s1 = smf.format((Date) t1);
				} else {
					s1 = t1.toString();
				}
				m.put("creation_time", s1);
			}

			// 处理 check_time
			Object t2 = m.get("check_time");
			if (t2 != null) {
				String s2;
				if (t2 instanceof LocalDateTime) {
					s2 = ((LocalDateTime) t2).format(dtf);
				} else if (t2 instanceof Timestamp) {
					s2 = smf.format((Timestamp) t2);
				} else if (t2 instanceof Date) {
					s2 = smf.format((Date) t2);
				} else {
					s2 = t2.toString();
				}
				m.put("check_time", s2);
			}
		}


		return list;
	}

}
