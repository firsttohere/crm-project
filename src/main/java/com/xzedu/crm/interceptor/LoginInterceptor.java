package com.xzedu.crm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//		//如果不是登录请求，判断是否登录过，即session域中有没有currentUser
//		String servletPath = request.getServletPath();//  /settings/...
//		//但是当servletPath是根路径，就不能拦截，即
//		if ("/".equals(servletPath)) {
//			return true;
//		}
		//上面的在拦截器中配置了
		
		HttpSession session = request.getSession();
		if (session != null && session.getAttribute("currentUser") != null) {
			//证明登录过，也不需要拦截
			return true;
		}
		response.sendRedirect(request.getContextPath() + "/");
		return false;
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
}
