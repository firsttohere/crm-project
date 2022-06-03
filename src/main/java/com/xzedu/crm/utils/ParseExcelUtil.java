package com.xzedu.crm.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.xzedu.crm.pojo.Activity;

public class ParseExcelUtil {
	
	
	
	/**
	 * 从指定的HSSFCell对象获取值
	 * @return
	 */
	@SuppressWarnings("all")
	public static String getCellValue(HSSFCell cell) {
		String value = "";
		
		int cellType = cell.getCellType();
		switch (cellType) {
			case HSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				value = String.valueOf(cell.getBooleanCellValue());
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				value = cell.getNumericCellValue() + "";
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				value = cell.getCellFormula();
				break;
			default:
				break;
		}
		
		return value;
	}
	
	/**
	 * allowOwners的key是tbl_user中的user_divName,值是其user_id
	 */
	//FIXME 待修改
	public static List<Activity> transferSheetToList(HSSFSheet sheet,Map<String,String> allowOwners){
		
		String dateFormat = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";
		
		ArrayList<Activity> list = new ArrayList<Activity>();
		//负责人和项目名称是必须要有的
		int rowStart = sheet.getFirstRowNum();//第一行是表头
		int rowEnd = sheet.getLastRowNum();
		HSSFRow row;
		HSSFCell cell;
		row = sheet.getRow(rowStart);//表头 
		//把列号，与字段名称映射起来
		HashMap<Integer,String> map = new HashMap<Integer, String>();
		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			cell = row.getCell(i);
			map.put(Integer.valueOf(i), getCellValue(cell));
		}
		//表中至少要有"所有者"和"名称"字段
		if(!(map.containsValue("所有者") && map.containsValue("名称"))) {
			throw new RuntimeException("表头中必须存在'所有者'和'名称'");
		}
		//第二行开始，每一行数据封装成一个Activity对象，放在list中
		Activity activity;
		String activity_owner;
		for (int i = rowStart + 1; i <= rowEnd; i++) {
			row = sheet.getRow(i);
			activity = new Activity();
			for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
				String cellValue = getCellValue(row.getCell(j));
				String fieldName = map.get(Integer.valueOf(j));//字段名
				switch (fieldName) {
					case "所有者":
						if((activity_owner = allowOwners.get(cellValue)) != null) {
							//所有者是一个合法的用户
							activity.setActivityOwner(activity_owner);
						}else {
							throw new RuntimeException("表中存在不合法的owner值");
						}
						break;
					case "名称":
						if ("".equals(cellValue)) {
							throw new RuntimeException("表中存在活动名称为空的记录");
						}else {
							activity.setActivityName(cellValue);
						}
						break;
					case "开始日期":
						//不为空，格式还不对，就抛异常
						if (!"".equals(cellValue) && !cellValue.matches(dateFormat)) {
							throw new RuntimeException("日期格式有问题");
						}else {
							activity.setActivityStartDate(cellValue);
						}
						break;
					case "结束日期":
						//不为空，格式还不对，就抛异常
						if (!"".equals(cellValue) && !cellValue.matches(dateFormat)) {
							throw new RuntimeException("日期格式有问题");
						}else {
							activity.setActivityEndDate(cellValue);
						}
						break;
					case "成本":
						//保证成本是一个正整数
						try {
							int cost = Integer.parseInt(cellValue);
							if(cost < 0) {
								throw new RuntimeException();
							}
							activity.setActivityCost(cellValue);
						} catch (Exception e) {
							throw new RuntimeException("表中存在消费不数或者不是正整数");
						}
						break;
					case "描述":
						activity.setActivityDescription(cellValue);
						break;
					default:
						break;
				}
			}
			activity.setActivityId(UUIDUtil.generate32UUID());
			list.add(activity);
		}
		return list;
	}

}
