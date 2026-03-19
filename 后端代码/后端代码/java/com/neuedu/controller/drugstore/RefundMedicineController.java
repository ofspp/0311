package com.neuedu.controller.drugstore;

import com.neuedu.bean.RestBean;
import com.neuedu.service.drugstore.RefundMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 药库管理：退药
 *
 */
@RestController
@CrossOrigin
public class RefundMedicineController {

	@Autowired
	private RefundMedicineService refundMedicineService;
	
	@RequestMapping("getGivePricePatient")
	public RestBean getGivePricePatient(@RequestParam Map<String,Object> map) {
		return refundMedicineService.getGivePricePatient(map);
	}
	
	@RequestMapping("getGivePatientDrug")
	public List<Map<String,Object>> getGivePatientDrug(Integer id) {
		return refundMedicineService.getGivePatientDrug(id);
	}
	
	@RequestMapping("refundPatientDrugs")
	public RestBean refundPatientDrugs(Integer id) {
		return refundMedicineService.refundPatientDrugs(id);
	}
}
