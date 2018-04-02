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

import com.pubhealth.entity.ChildOne;
import com.pubhealth.entity.ChildrenNewborn;
import com.pubhealth.service.ChildOneService;
import com.pubhealth.service.ChildrenNewbornService;

@Controller
@RequestMapping("/pub_service")
public class ChildrenNewbornController {
	Logger log=Logger.getLogger(ChildOneController.class);
	@Autowired
	private ChildrenNewbornService childrenNewbornService;
	
	@RequestMapping(value="/child_newborn_search",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public String searchBasicIndv(@RequestBody ChildrenNewborn childrenNewborn,HttpServletRequest request,HttpServletResponse response){
		log.info(childrenNewborn.getPersonalName());
		String result=childrenNewbornService.search(childrenNewborn);
		return result;
	}
}
