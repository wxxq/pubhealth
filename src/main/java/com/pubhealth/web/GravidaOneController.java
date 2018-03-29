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
import com.pubhealth.entity.GravidaOne;
import com.pubhealth.entity.Index;
import com.pubhealth.service.GravidaOneService;
import com.pubhealth.util.ESConnector;


@Controller
@RequestMapping("/pub_service")
public class GravidaOneController {
	
	Logger log=Logger.getLogger(GravidaOneController.class);
	@Autowired
	private GravidaOneService gravidaOneService;
	
	@RequestMapping(value="/gravida_one_search",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public String searchBasicIndv(@RequestBody GravidaOne gravidaOne,HttpServletRequest request,HttpServletResponse response){
		log.info(gravidaOne.getPersonalName());
		String result=gravidaOneService.search(gravidaOne);
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