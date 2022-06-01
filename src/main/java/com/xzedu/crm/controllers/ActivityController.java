package com.xzedu.crm.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
}
