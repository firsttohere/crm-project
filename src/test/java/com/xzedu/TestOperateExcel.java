package com.xzedu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

/**
 * 使用apache-poi对excel文件进行操作
 * @author 上善若水
 *
 */
public class TestOperateExcel {

	@Test
	public void t() throws FileNotFoundException, IOException {
		String filePath = TestOperateExcel.class.getClassLoader().getResource("excels/activities.xls").getPath();
		File file = new File(filePath);
		//这个对象就封装了文件的所有内容
		InputStream is = null;
		HSSFWorkbook workbook = new HSSFWorkbook(is = new FileInputStream(file));
		is.close();
		HSSFSheet sheet = workbook.getSheetAt(0);//获取第一页
		//HSSFRow row = sheet.getRow(1);//获取第二行
		HSSFRow row = null;
		HSSFCell cell = null;
		int maxRowNum;
		for (int i = sheet.getFirstRowNum(); i <= (maxRowNum = sheet.getLastRowNum()); i++) {
			row = sheet.getRow(i);
			int maxCellNum;
			for (int j = row.getFirstCellNum(); j < (maxCellNum = row.getLastCellNum()); j++) {
				cell = row.getCell(j);
				if(j == maxCellNum - 1) {
					System.out.println(cell.getStringCellValue());
				}else {
					System.out.print(cell.getStringCellValue() + "|");
				}
			}
			if(i != maxRowNum) {
				System.out.println("----------------------------------------");
			}else {
				System.out.println();
			}
		}
		workbook.close();
	}
}
