package com.ebizprise.winw.project.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("sysMenuJDBC")
public class SysMenuJDBC extends BaseJDBC {

	@Transactional(readOnly = true)
	public List<Map<String, Object>> findByLevel(int treeLevel) {
		List<Map<String, Object>> mapList = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		for (int i = 0; i <= treeLevel; i++) {
			sb.append("level" + i + ".menu_name AS level" + i + "_name");
			sb.append(", ");
		}
		sb.append(" CASE ");
		for (int i = 0; i <= treeLevel; i++) {
			sb.append(" WHEN LEN(level" + i + ".path) > 0 THEN level" + i + ".path ");
		}
		sb.append(" END AS path");
		for (int i = 0; i <= treeLevel; i++) {
			if (i == 0) {
				sb.append(" FROM sys_menu AS level0 ");
			} else {
				sb.append(" LEFT OUTER JOIN sys_menu AS level" + i + " ON " + "level" + i + ".parent_id = ");
				sb.append(" level" + (i - 1) + ".id ");
				sb.append(" AND level" + i + ".enabled = 1 ");
			}
		}
		sb.append(" WHERE level0.parent_id=0 AND level0.enabled=1");
		sb.append(" ORDER BY level0.parent_id, level0.order_id, level0_name");
		for (int i = 1; i <= treeLevel; i++) {
			sb.append(", ");
			sb.append("level" + i + ".order_id");
		}

		try {
			// userInfo = jdbcTemplate.queryForObject("select * from user_info as info where
			// info.user_id=?", new BeanPropertyRowMapper<>(UserInfo.class), userId);
			mapList = jdbcTemplate.queryForList(sb.toString());
		} catch (Exception e) {
			throw e;
		}
		return mapList;
	}
}
