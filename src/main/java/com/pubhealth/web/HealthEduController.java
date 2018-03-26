package com.pubhealth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pubhealth.entity.HealthEdu;
import com.pubhealth.service.HealthEduService;

@Controller
@RequestMapping("/pub_service")
public class HealthEduController {

	Logger log=Logger.getLogger(HealthDocController.class);
	@Autowired
	private HealthEduService healthEduService;
	
	@RequestMapping(value="/health_edu_search",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public String searchHealthDoc(@RequestBody HealthEdu healthEdu,HttpServletRequest request,HttpServletResponse response){
		log.info(healthEdu.getPersonalName());
		String result=healthEduService.search(healthEdu);
		return result;
	}
	
//	@RequestMapping(value="/health_doc_search2",method = RequestMethod.POST,consumes = "application/json")
//	@ResponseBody
//	public String searchHealthDoc2(@RequestBody HealthEdu healthEdu,HttpServletRequest request,HttpServletResponse response){
//		log.info(healthEdu.getPersonalName());
//		String result=healthEduService.test2();
//		return result;
//	}
}
