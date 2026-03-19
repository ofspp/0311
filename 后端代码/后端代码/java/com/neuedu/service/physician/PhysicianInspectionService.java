package com.neuedu.service.physician;

import com.neuedu.bean.RestBean;

import java.util.List;
import java.util.Map;

public interface PhysicianInspectionService {

	List<Map<String, Object>> getInspection(Map<String, Object> map);

	RestBean addInspectionRequest(String register_id, String[] medical_technology_ids,
			String inspection_info, String inspection_position, String inspection_remark);

}
