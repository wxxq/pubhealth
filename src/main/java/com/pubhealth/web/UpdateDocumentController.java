package com.pubhealth.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pubhealth.entity.Index;
import com.pubhealth.service.BaseService;

/*
 * @author melo
 * update elasticsearch document by mongo_id
*/

@Controller
@RequestMapping("/document")
public class UpdateDocumentController {

	@Autowired
	private BaseService baseMongoService;

	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public String updateDocumentById(HttpServletRequest request) {
		Map record = baseMongoService.parseDataByObjectId("5ae019e5178ca15442c0de55", "health_education");
		Index index = new Index("health_education", "health_education");
		String esId = baseMongoService.findESIdByMongoId("5ae019e5178ca15442c0de55", index);
		String result = baseMongoService.updateDocumentByESId(esId, record, index);
		if (result != null) {

		}
		return result;
	}
}
