package com.neuedu.controller.registration;

import com.neuedu.service.registration.RegistLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class RegistLevelController {
	@Autowired
	private RegistLevelService registLevelService;
	//得到所有挂号级别
	@RequestMapping("/getRegistLevelList")
	public List<Map<String,Object>> getRegistLevelList() {
		return registLevelService.getRegistLevelList();
	}
}
