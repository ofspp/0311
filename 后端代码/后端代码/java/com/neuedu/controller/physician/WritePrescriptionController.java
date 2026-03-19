package com.neuedu.controller.physician;

import com.neuedu.bean.RestBean;
import com.neuedu.service.physician.WritePrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 医生看诊：开设处方
 */
@RestController
@CrossOrigin
public class WritePrescriptionController {
	@Autowired
	private WritePrescriptionService writePrescriptionService;

	
	@RequestMapping("searchDrug")
	public List<Map<String,Object>> searchDrug(@RequestParam Map<String,Object> map){
		return writePrescriptionService.searchDrug(map);
	}
	
	@PostMapping("addPrescription")
	public RestBean addPrescription(@RequestBody List<Map<String,Object>> submit_prescription) {
		
		return writePrescriptionService.addPrescription(submit_prescription);
	}

}
