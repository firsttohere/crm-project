package com.xzedu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

public class Test1 {
	@Test
	public void testPOI() throws IOException {
		//使用Apache-poi创建一个excel文件
				//创建HSSFWorkbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		//往文件中写内容
		//1.创建页
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("名字");
		cell = row.createCell(1);
		cell.setCellValue("学号");
		cell = row.createCell(2);
		cell.setCellValue("年龄");
		
		for (int i = 1; i < 10; i++) {
			row = sheet.createRow(i);
			for (int j = 0;j < 3; j++) {
				cell = row.createCell(j);
				cell.setCellValue(i + j);
			}
		}
		//调用工具函数，生成excel文件
		FileOutputStream stream = new FileOutputStream("d:/abc.xls");
		workbook.write(stream);
		
		stream.close();
		workbook.close();
	}
	@Test
	public void t() throws IOException {
		File file = new File("src/main/resources/a.txt");
		file.createNewFile();
	}
}
