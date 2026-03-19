package com.neuedu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data   // 自动生成 getter/setter、toString、equals、hashCode
@NoArgsConstructor // 生成无参构造方法
@AllArgsConstructor // 生成全参构造方法
public class RestBean {
	private List<Map<String, Object>> list;  // 对应查询数据
	private int totalCount;                  // 对应查询总条目数量
	private String msg;                      // 前端显示内容
	private String status;                   // 前端状态：success/warning/info/error
}
