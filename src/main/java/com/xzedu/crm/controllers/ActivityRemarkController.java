package com.xzedu.crm.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xzedu.crm.common.domain.Message;
import com.xzedu.crm.pojo.ActivityRemark;
import com.xzedu.crm.pojo.User;
import com.xzedu.crm.service.ActivityRemarkService;
import com.xzedu.crm.utils.DateUtil;
import com.xzedu.crm.utils.UUIDUtil;

@Controller
@RequestMapping("/workbench/activityRemark")
public class ActivityRemarkController {
	@Autowired
	private ActivityRemarkService activityRemarkService;
	
	//处理修改请求
	@PostMapping("/update")
	@ResponseBody
	public Object update(String id,String content,HttpSession session) {
		Message message = new Message();
		//修改
		ActivityRemark activityRemark = new ActivityRemark();
		activityRemark.setActivityRemarkId(id);
		activityRemark.setArNoteContent(content);
		//修改时间，和修改者需要重置一下
		activityRemark.setArEditFlag("1");//被修改了
		activityRemark.setArEditTime(DateUtil.getNowFullDate());
		activityRemark.setArEditBy(((User)session.getAttribute("currentUser")).getUserId());
		ActivityRemark remark = activityRemarkService.updateSelective(activityRemark);
		if (remark == null) {
			//失败
			message.setCode("0");
			message.setMessage("更新失败");
		}else {
			message.setCode("1");
			message.setMessage("更新成功");
			message.setOtherData(remark);//这里面的数据都是人性化的数据
		}
		return message;
	}
	
	//处理删除的请求
	@GetMapping("/delete")
	@ResponseBody
	public Object delete(String id) {
		Message message = new Message();
		//直接删除，返回1就代表成功了，0就失败了
		int effectRows = activityRemarkService.deleteById(id);
		if(effectRows == 1) {
			//成功了
			message.setCode("1");
		}else {
			message.setCode("0");
			message.setMessage("系统繁忙，请稍后再试一试");
		}
		return message;
	}
	
	//查询所有的活动id为xxx的活动remark
	@RequestMapping("/showRemarks")
	public String showRemarks(String id,HttpServletRequest request) {
		List<ActivityRemark> remarks = activityRemarkService.getAllRemarkByActivityId(id);
		request.setAttribute("remarks", remarks);
		return "workbench/activity/detail";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public Object save(String arNoteContent,String activityId,HttpSession session) {
		Message message = new Message();
		//疯转一个activityRemark对象
		ActivityRemark remark = new ActivityRemark();
		remark.setActivityRemarkId(UUIDUtil.generate32UUID());
		remark.setActivityId(activityId);
		remark.setArNoteContent(arNoteContent);
		String nowFullDate = DateUtil.getNowFullDate();
		remark.setArCreateTime(nowFullDate);
		String userId = ((User)session.getAttribute("currentUser")).getUserId();
		remark.setArCreateBy(userId);//存当前用户的id值
		remark.setArEditBy(userId);
		remark.setArEditTime(nowFullDate);
		remark.setArEditFlag("0");//表示没有被修改过
		//请求service层添加
		try {
			int effectRows = activityRemarkService.insertOne(remark);
			if (effectRows == 0) {
				//插入失败
				message.setCode("0");
				message.setMessage("系统繁忙");
			}else {
				//插入成功
				message.setCode("1");
				message.setMessage("成功插入一条备注");
				remark.setArCreateBy(((User)session.getAttribute("currentUser")).getDivName());
				remark.setArEditBy(((User)session.getAttribute("currentUser")).getDivName());
				message.setOtherData(remark);
			}
		} catch (Exception e) {
			message.setCode("0");
			message.setMessage("主键异常，或者其他异常");
		}
		
		
		return message;
	}
}
