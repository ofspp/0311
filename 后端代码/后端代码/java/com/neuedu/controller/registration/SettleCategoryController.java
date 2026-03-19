package com.neuedu.controller.registration;

import com.neuedu.service.registration.SettleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class SettleCategoryController {
	@Autowired
	private SettleCategoryService settleCategoryService;
	@RequestMapping("getSettleCategory")
	public List<Map<String,Object>> getSettleCategory(){
		System.out.println(settleCategoryService.getSettleCategory());
		return settleCategoryService.getSettleCategory();
	}
}
