package com.neuedu.controller.drugstore;

import com.neuedu.bean.RestBean;
import com.neuedu.service.drugstore.DrugStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 药库管理：药库管理
 *
 */
@RestController
@CrossOrigin
public class DrugStorageController {
	@Autowired
	private DrugStorageService drugStorageService;
	
	@RequestMapping("getDrugInfo")
	public RestBean getDrugInfo(@RequestParam Map<String,Object> map) {
		return drugStorageService.getDrugInfo(map);
	}

	@PostMapping("addDrug")
	public RestBean addDrug(@RequestParam Map<String,Object> map) {
		return drugStorageService.addDrug(map);
	}

	@PostMapping("updateDrug")
	public RestBean updateDrug(@RequestParam Map<String,Object> map) {
		return drugStorageService.updateDrug(map);
	}

	@PostMapping("deleteDrug")
	public RestBean deleteDrug(@RequestParam("id") Integer id) {
		return drugStorageService.deleteDrug(id);
	}
}
