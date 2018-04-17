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

import com.pubhealth.entity.SuperInfo;
import com.pubhealth.entity.TuberculosisFirst;
import com.pubhealth.service.SuperInfoService;
import com.pubhealth.service.TuberculosisFirstService;

@Controller
@RequestMapping("/pub_service")
public class TuberculosisFirstController {
	Logger log=Logger.getLogger(TuberculosisFirstController.class);
	@Autowired
	private TuberculosisFirstService tuberculosisFirstService;
	
	@RequestMapping(value="/tuberculosis_first_search",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public String searchBasicIndv(@RequestBody TuberculosisFirst tuberculosisFirst,HttpServletRequest request,HttpServletResponse response){
		log.info(tuberculosisFirst.getPersonalName());
		String result=tuberculosisFirstService.search(tuberculosisFirst);
		return result;
	}
}
