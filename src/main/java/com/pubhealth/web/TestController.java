package com.pubhealth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pubhealth.service.MongoHealthDocService;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private MongoHealthDocService mongoHealthDocService;
	
	@RequestMapping("hello")
	@ResponseBody
	public String hello(){
		mongoHealthDocService.insert();
		return "hello";
	}
}
