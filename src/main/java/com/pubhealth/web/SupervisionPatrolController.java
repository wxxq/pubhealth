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

import com.pubhealth.entity.SupervisionPatrol;
import com.pubhealth.service.SupervisionPatrolService;

@Controller
@RequestMapping("/pub_service")
public class SupervisionPatrolController {
	Logger log=Logger.getLogger(SupervisionPatrolController.class);
	@Autowired
	private SupervisionPatrolService supervisonPatrolService;
	
	@RequestMapping(value="/supervision_patrol_search",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public String searchBasicIndv(@RequestBody SupervisionPatrol supervisionPatrol,HttpServletRequest request,HttpServletResponse response){
		log.info(supervisionPatrol.getPersonalName());
		String result=supervisonPatrolService.search(supervisionPatrol);
		return result;
	}
}
