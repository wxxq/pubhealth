package com.pubhealth.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.Document;
import com.pubhealth.entity.SuperInfo;
import com.pubhealth.entity.Index;
import com.pubhealth.service.SuperInfoService;
import com.pubhealth.util.ESConnector;


@Controller
@RequestMapping("/pub_service")
public class SuperInfoController {
	
	Logger log=Logger.getLogger(SuperInfoController.class);
	@Autowired
	private SuperInfoService superInfoService;
	
	@RequestMapping(value="/super_info_search",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public String searchBasicIndv(@RequestBody SuperInfo superInfo,HttpServletRequest request,HttpServletResponse response){
		log.info(superInfo.getPersonalName());
		String result=superInfoService.search(superInfo);
		return result;
	}
	
//	@RequestMapping(value="/health_doc_search2",method = RequestMethod.POST,consumes = "application/json")
//	@ResponseBody
//	public String searchHealthDoc2(@RequestBody HealthDoc healthDoc,HttpServletRequest request,HttpServletResponse response){
//		log.info(healthDoc.getPersonalName());
//		String result=healthDocService.test2();
//		return result;
//	}
	
}
