package com.neuedu.service.registration;

import com.neuedu.bean.RestBean;

import java.util.List;
import java.util.Map;

public interface ExpenseChargeService {

	Map<String, Object> searchExpenseChargePatient(String case_number, String real_name);

	RestBean expenseCharge(List<Map<String, Object>> list);

	Map<String, Object> searchExpenseRefundPatient(String case_number, String real_name);

	RestBean expenseRefund(List<Map<String, Object>> list);

	Map<String, Object> searchAllPricePatient(String case_number, String real_name);

}
