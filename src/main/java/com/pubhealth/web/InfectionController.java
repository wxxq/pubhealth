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

import com.pubhealth.entity.InfectionDoc;
import com.pubhealth.service.InfectionService;

@Controller
@RequestMapping("/pub_service")
public class InfectionController {
	Logger log=Logger.getLogger(InfectionController.class);
	@Autowired
	private InfectionService infectionService;
	
	@RequestMapping(value="/infection_search",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public String searchHealthDoc(@RequestBody InfectionDoc infectionDoc,HttpServletRequest request,HttpServletResponse response){
		log.info(infectionDoc.getPersonalName());
		String result=infectionService.searchInfectionDoc(infectionDoc);
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
