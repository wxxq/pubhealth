package com.pubhealth.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pubhealth.entity.Index;
import com.pubhealth.entity.LogEntity;
import com.pubhealth.entity.Operator;
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

	@RequestMapping(value = "/update_by_id", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public String updateDocumentById(@RequestBody Map<String,String> params,HttpServletRequest request) {
		String mongoId=params.get("mongo_id");
		String collectionName=params.get("collection_name");
		JSONObject result=null;
		if(StringUtils.isNotEmpty(mongoId)&&StringUtils.isNotEmpty(collectionName)){
			Index index = new Index(collectionName, collectionName);
			Map record = baseMongoService.parseDataByObjectId(mongoId, collectionName);
			String esId = baseMongoService.findESIdByMongoId(mongoId, index);
			result = baseMongoService.updateDocumentByESId(esId, record, index);
			Map<String,Object> map = new HashMap<>();
			map.put("mongoId", mongoId);
			map.put("ESId", esId);
			map.put("operator", Operator.UPDATE);
			map.put("result", result.get("status"));		
			baseMongoService.insertLogRecord(map, "es_crud_records");
		}else{
			result=new JSONObject();
			result.put("status", "failed");
			result.put("result", "mongo_id and collection_name must not be null or blank!");
		}
		return result.toString();
	}
	
}
