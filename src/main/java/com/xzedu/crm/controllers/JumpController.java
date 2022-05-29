package com.xzedu.crm.controllers;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JumpController {
	/*
		按理说，给controller方法分配url时，要写：http://localhost:8080/crm + /...
		但springmvc，要求我们直接写后面的“/welcome”
	 */
	@RequestMapping("/")
	public String toIndex() {
		//视图转发
		return "index";
	}
	@RequestMapping("/toMainPage")
	public String toMainPage() {
		return "workbench/index";
	}
}
