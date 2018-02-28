package com.pubhealth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.Index;
import com.pubhealth.service.HealthDocService;
import com.pubhealth.util.ESConnector;


@Controller
@RequestMapping("/health")
public class HealthDocController {
	
	@Autowired
	private HealthDocService healthDocService;
	
	@RequestMapping("/search")
	@ResponseBody
	public String searchHealthDoc(HttpServletRequest request,HttpServletResponse response){
		String result=healthDocService.searchHealthDoc();
		return result;
	}
	
}
