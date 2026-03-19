package com.neuedu.service.check;

import com.neuedu.bean.RestBean;

import java.util.List;
import java.util.Map;

public interface CheckInputService {

	RestBean getFinishCheck(String case_number, String real_name, Integer nowPageNumber, Integer pageSize);

	List<Map<String, Object>> getFinishCheckByRegistid(String id);

	RestBean updateCheckInput(String id, String employee_id, String check_result);

}
