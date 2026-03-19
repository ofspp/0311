package com.neuedu.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TimestampUtil {

	//将List中map项目的Timestamp格式化
	public static void formatTimestamp(List<Map<String,Object>> list,String name) {
		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 注意 yyyy HH
		for (Map<String, Object> m : list) {
			Object ct = m.get(name);
			if (ct != null) {
				String s;
				if (ct instanceof LocalDateTime) {
					// LocalDateTime -> 格式化
					s = ((LocalDateTime) ct).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				} else if (ct instanceof Timestamp) {
					// Timestamp -> 格式化
					s = smf.format((Timestamp) ct);
				} else if (ct instanceof Date) {
					// java.util.Date -> 格式化
					s = smf.format((Date) ct);
				} else {
					// 兜底处理，直接 toString
					s = ct.toString();
				}
				m.put(name, s);
			}
		}

	}
}
