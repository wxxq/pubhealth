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

import com.pubhealth.entity.Hypertension;
import com.pubhealth.entity.InfectionDoc;
import com.pubhealth.service.HypertensionService;
import com.pubhealth.service.InfectionService;

@Controller
@RequestMapping("/pub_service")
public class HypertensionController {

	Logger log=Logger.getLogger(HypertensionController.class);
	@Autowired
	private HypertensionService hypertensionService;
	
	@RequestMapping(value="/hypertension_search",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public String searchHealthDoc(@RequestBody Hypertension hypertension,HttpServletRequest request,HttpServletResponse response){
		log.info(hypertension.getPersonalName());
		String result=hypertensionService.search(hypertension);
		return result;
	}
}
