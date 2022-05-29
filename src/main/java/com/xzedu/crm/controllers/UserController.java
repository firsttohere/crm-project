package com.xzedu.crm.controllers;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xzedu.crm.common.domain.Message;
import com.xzedu.crm.pojo.User;
import com.xzedu.crm.service.UserService;
import com.xzedu.crm.utils.DateUtil;

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
	public Object login(String loginAct,String loginPwd,String isRemPwd,HttpServletRequest request,HttpServletResponse response) {
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
			return message;
		}
		//进一步判断账号是否过期
		String expireTime = user.getuExpireTime();//2019-12-12
		//把当前时间转换成字符串
		String nowString = DateUtil.toFullDate(new Date());
		if (nowString.compareTo(expireTime) > 0) {
			//登录失败，用户已经过期
			message.setMessage("用户已过期");
			return message;
		}
		//状态是不是被锁定了，lock_state是不是0，0代表就是被锁定
		if ("0".equals(user.getuLockState())) {
			//登录失败，用户被锁定
			message.setMessage("用户被锁定");
			return message;
		}
		//进一步判断，判断用户的ip地址，判断是否合法
		String remoteAddr = request.getRemoteAddr();
		String uAllowIps = user.getuAllowIps();
		if (uAllowIps == null || (!uAllowIps.contains(remoteAddr) && !"all".equals(uAllowIps))) {
			//登录失败，ip受到限制
			message.setMessage("ip受限制");
			return message;
		}
		//登录成功
		message.setCode("1");
		//登录成功后把user放在session中，以便后面的请求视图展示的时候需要
		request.getSession().setAttribute("currentUser", user);
		//如果需要记住密码，向外写cookic
		if ("true".equals(isRemPwd)) {
			Cookie c1 = new Cookie("loginAct", user.getuLoginAct());
			c1.setMaxAge(10 * 24 * 60 * 60);
			response.addCookie(c1);
			Cookie c2 = new Cookie("loginPwd", user.getuLoginPwd());
			c2.setMaxAge(10 * 24 * 60 * 60);
			response.addCookie(c2);
		}else {
			//删除没有过期的cookie
			Cookie c1 = new Cookie("loginAct", "");
			c1.setMaxAge(0);
			Cookie c2 = new Cookie("loginPwd", "");
			c2.setMaxAge(0);
			response.addCookie(c1);
			response.addCookie(c2);
		}
		return message;
	}
}
