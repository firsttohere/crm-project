package com.xzedu.crm.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xzedu.crm.common.domain.CountAndActivities;
import com.xzedu.crm.common.domain.Message;
import com.xzedu.crm.pojo.Activity;
import com.xzedu.crm.pojo.User;
import com.xzedu.crm.service.ActivityService;
import com.xzedu.crm.service.UserService;
import com.xzedu.crm.utils.DateUtil;
import com.xzedu.crm.utils.UUIDUtil;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ActivityService activityService;
	
	@RequestMapping("/index")
	public String toActivityIndex(HttpServletRequest request) {
		//因为创建活动的模态窗口需要当前系统中所有的用户，展示在其下拉列表中
		//所以要查出user表中所有的记录
		List<User> users = userService.queryAllUser();
		request.setAttribute("users", users);
		return "workbench/activity/index";
	}
	@RequestMapping("/create/save")
	@ResponseBody
	public Object createActivity(Activity activity,HttpSession session) {
		//生成一个32位的随机id，uuid
		String activity_id = UUIDUtil.generate32UUID();
		activity.setActivityId(activity_id);
		//生成当前时间，作为活动创建时间
		String now;
		activity.setActivityCreateTime(now = DateUtil.getNowFullDate());
		//当前session域中的User就是创造者的divName
		User user = (User)session.getAttribute("currentUser");
		activity.setActivityCreateBy(user.getDivName());
		//edit人和时间现在是创造这
		activity.setActivityEditTime(now);
		activity.setActivityEditBy(user.getDivName());
		
		//调用service层的添加方法
		int effectRow = activityService.createActivity(activity);
		
		Message message = new Message();
		message.setCode(effectRow + "");
		if (effectRow == 0) {
			message.setMessage("添加失败！");
		}else {
			message.setMessage("添加成功*");
			message.setOtherData(activity);
		}
		
		return message;
	}
	
	@ResponseBody
	@RequestMapping(value="/query",params = {"pageNo","pageSize"},method = {RequestMethod.POST})
	public Object query(@RequestParam Map<String, Object> map) {
		//把结果封装到一个对象中
		CountAndActivities result = new CountAndActivities();
		int count = activityService.queryCount();
		result.setCount(count);
		//查询对应页的数据
		List<Activity> page = null;
		try {
			page = activityService.queryByPage(map);
		} catch (Exception e) {
		}
		
		result.setActivities(page);
		return result;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam String[] ids) {
		int effectRows = activityService.batchDelete(ids);
		Message message = new Message();
		if (effectRows == ids.length) {
			message.setCode("1");
		}else {
			message.setCode("0");
			message.setMessage("删除失败或者部分失败");
		}
		return message;
	}
	
	@RequestMapping(value = "/queryById",method = {RequestMethod.GET})
	@ResponseBody
	public Object queryById(String id) {
		return activityService.queryById(id);
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public Object update(Activity activity) {
		int effectRows = activityService.updatePart(activity);
		Message message = new Message();
		if(effectRows == 1) {
			message.setCode("1");
		}else {
			message.setCode("0");
			message.setMessage("系统繁忙");
		}
		return message;
	}
	
	@RequestMapping("/filedownload")
	public void fileDownload(HttpServletResponse response) throws IOException {
		//查询所有的活动
		List<Activity> activities = activityService.queryAll();
		//创建
		String filePath = this.getClass().getClassLoader().getResource("excels/activities.xls").getPath();
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		HSSFWorkbook workbook = new HSSFWorkbook();//代表一个excel文件
		HSSFRow row;
		HSSFCell cell;
		HSSFSheet sheet = workbook.createSheet();//创建一页
		//创建表头
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("名称");
		cell = row.createCell(1);
		cell.setCellValue("负责人");
		cell = row.createCell(2);
		cell.setCellValue("开始日期");
		cell = row.createCell(3);
		cell.setCellValue("结束日期");
		cell = row.createCell(4);
		cell.setCellValue("预算花费");
		for (int i = 1; i <= activities.size(); i++) {
			Activity activity = activities.get(i - 1);
			row = sheet.createRow(i);//创建一行
			cell = row.createCell(0);
			cell.setCellValue(activity.getActivityName());
			cell = row.createCell(1);
			cell.setCellValue(activity.getActivityOwner());
			cell = row.createCell(2);
			cell.setCellValue(activity.getActivityStartDate());
			cell = row.createCell(3);
			cell.setCellValue(activity.getActivityEndDate());
			cell = row.createCell(4);
			cell.setCellValue(activity.getActivityCost());
		}
		OutputStream o;
		workbook.write(o = new FileOutputStream(file));
		o.close();
		workbook.close();
		
		//把文件读进来
		response.setContentType("application/octet-stream;charset=UTF-8");
		ServletOutputStream outputStream = response.getOutputStream();
		response.addHeader("Content-Disposition", "attachment;filename=activityList.xls");
		FileInputStream fis = new FileInputStream(file);
		byte[] buf = new byte[1024];
		int readRow = -1;
		while((readRow = fis.read(buf)) != -1) {
			//把buf中的数据输出到write中
			outputStream.write(buf, 0, readRow);
		}
		fis.close();
		outputStream.flush();
		
	}
}
