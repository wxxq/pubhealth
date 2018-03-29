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

import com.pubhealth.entity.Glycuresis;
import com.pubhealth.entity.InfectionDoc;
import com.pubhealth.service.GlycuresisService;
import com.pubhealth.service.InfectionService;

@Controller
@RequestMapping("/pub_service")
public class GlycuresisController {
	Logger log=Logger.getLogger(GlycuresisController.class);
	@Autowired
	private GlycuresisService glycuresisService;
	
	@RequestMapping(value="/glycuresis_search",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public String searchHealthDoc(@RequestBody Glycuresis glycuresis,HttpServletRequest request,HttpServletResponse response){
		log.info(glycuresis.getPersonalName());
		String result=glycuresisService.search(glycuresis);
		return result;
	}
}
