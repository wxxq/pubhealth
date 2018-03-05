package com.pubhealth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pubhealth.entity.Document;
import com.pubhealth.service.MongoHealthDocService;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private MongoHealthDocService mongoHealthDocService;
	
	@RequestMapping(value="hello",method=RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public Document hello(@RequestBody Document document){
//		mongoHealthDocService.insert();
		return document;
	}
}
