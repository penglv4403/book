package com.orange.book.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 类名称：RowReader 类描述：Excel行处理
 */
public class RowReader {
	private int count = 0;
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	List<String> titleList = new ArrayList<String>();
	private Integer curRow = -1;
	private int flag = 0;


	public List<Map<String, String>> getList() {
		return this.list;
	}

	public void getRows(int sheetIndex, int curRow, List<String> row) {
		if (this.curRow != curRow) {
			if (curRow == 1) {
				for (int i = 0; i < row.size(); i++) {
					titleList.add(toUpperCaseFirstOne(row.get(i)));
				}
			} else {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < row.size(); i++) {
					if (row.get(i).equals("null")) {
						continue;
					}
				//	map.put(titleList.get(i), row.get(i).equals("null") ? "" : row.get(i));
				}
				list.add(map);
				count++;
				if (list.size() != 0 && list.size() % 100 == 0) {
					//installData();
				}

			}
			this.curRow = curRow;
		}

	}



	// 首字母转大写
	public static String toUpperCaseFirstOne(String s) {
		String[] split = s.split("_");
		StringBuffer sb = new StringBuffer();
		sb.append(split[0]);
		for (int i = 1; i < split.length; i++) {
			if (Character.isUpperCase(split[i].charAt(0))) {
				sb.append(split[i]);
			} else {
				sb.append((new StringBuilder()).append(Character.toUpperCase(split[i].charAt(0)))
						.append(split[i].substring(1)).toString());
			}
		}
		return sb.toString();
	}
}
