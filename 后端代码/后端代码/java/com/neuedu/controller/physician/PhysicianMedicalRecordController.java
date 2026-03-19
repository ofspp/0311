package com.neuedu.controller.physician;

import com.neuedu.service.physician.PhysicianMedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 医生看诊：病历管理
 */
@RestController
@CrossOrigin
public class PhysicianMedicalRecordController {
	@Autowired
	private PhysicianMedicalRecord physicianMedicalRecord;
	
	@RequestMapping("addHomeMedicalRecord")
	public String addHomeMedicalRecord(@RequestParam Map<String,Object> map) {
		System.out.println(map);
		String s = physicianMedicalRecord.addHomeMedicalRecord(map);
		return s;
	}
}
