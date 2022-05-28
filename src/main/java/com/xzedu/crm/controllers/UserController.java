package com.xzedu.crm.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xzedu.crm.common.domain.Message;
import com.xzedu.crm.pojo.User;
import com.xzedu.crm.service.UserService;

@Controller
@RequestMapping("/settings/qx/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	/*
		处理/crm/user/login
	 */
	@RequestMapping("/loginPage")
	public String loginPage() {
		return "settings/qx/user/login";
	}
	
	@RequestMapping("/login")
	@ResponseBody
	public Object login(String loginAct,String loginPwd,String isRemPwd,HttpServletRequest request) {
		//疯转成map
		Map<String,Object> map = new ConcurrentHashMap<>();
		map.put("loginAct", loginAct);
		map.put("loginPwd", loginPwd);
		User user = userService.queryUserByLoginActAndPwd(map);
		//封装返回结果信息
		Message message = new Message();
		message.setCode("0");//默认失败
		//根据查询结果，生成相应信息
		if (user == null) {
			//登录失败，用户名或者密码错误
			message.setMessage("用户名或者密码错误");
		}
		//进一步判断账号是否过期
		String expireTime = user.getuExpireTime();//2019-12-12
		//把当前时间转换成字符串
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowString = sdf.format(now);
		if (expireTime.compareTo(nowString) < 0) {
			//登录失败，用户已经过期
			message.setMessage("用户已过期");
		}
		//状态是不是被锁定了，lock_state是不是0，0代表就是被锁定
		if ("0".equals(user.getuLockState())) {
			//登录失败，用户被锁定
			message.setMessage("用户被锁定");
		}
		//进一步判断，判断用户的ip地址，判断是否合法
		String remoteAddr = request.getRemoteAddr();
		String uAllowIps = user.getuAllowIps();
		if (!uAllowIps.contains(remoteAddr)) {
			//登录失败，ip受到限制
			message.setMessage("ip受限制");
		}
		//登录成功
		message.setCode("1");
		return message;
	}
}
