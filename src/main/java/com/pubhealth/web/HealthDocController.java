package com.pubhealth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.HealthDoc;
import com.pubhealth.entity.Index;
import com.pubhealth.service.HealthDocService;
import com.pubhealth.util.ESConnector;


@Controller
@RequestMapping("/pub_service")
public class HealthDocController {
	
	Logger log=Logger.getLogger(HealthDocController.class);
	@Autowired
	private HealthDocService healthDocService;
	
	@RequestMapping("/health_doc_search")
	@ResponseBody
	public String searchHealthDoc(HealthDoc healthDoc,HttpServletRequest request,HttpServletResponse response){
		log.info(healthDoc.getPersonalName());
		String result=healthDocService.searchHealthDoc(healthDoc);
		return result;
	}
	
}
