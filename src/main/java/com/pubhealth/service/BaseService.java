package com.pubhealth.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.pubhealth.dao.BaseMongoDao;
import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.LogEntity;
import com.pubhealth.entity.Operator;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

import static java.lang.String.format;

/*
 * @author melo
*/
@Service
public class BaseService {
	
	Logger log = LoggerFactory.getLogger(BaseService.class);

	@Autowired
	private BaseMongoDao baseMongoDao;

	@Autowired
	private ESQueryWrapper dao;

	/**
	 * 根据MongoId从ES 索引中查询对应的ES索引的documentID
	 * 
	 * @param mongoId
	 * @param index
	 * @return 返回documentID
	 */
	public String findESIdByMongoId(String mongoId, Index index) {
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		param.fieldList.add(new TermField("uid.keyword", mongoId, ESSearchType.FILTER));
		SearchResponse response = dao.commonQuery(param, index);
		String id = ESResponseParse.parseIdFromResponse(response);
		return id;
	}

	/**
	 * 更新elasticsearch的文档信息
	 * 
	 * @param ESId
	 *            文档id
	 * @param content
	 *            更新的内容
	 * @param index
	 * @return
	 */
	public JSONObject updateDocumentByESId(String ESId, Map m, Index index) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", ESId);
		map.put("content", m);
		String result = dao.updateDocument(map, index);
		JSONObject obj = new JSONObject();
		obj.put("es_id", ESId);
		if (result != null) {
			obj.put("status", "success");
		} else {
			obj.put("status", "error");
		}
		return obj;
	}

	/**
	 * 
	 * @param obj
	 * @param collectionName
	 */
	public void insertLogRecord(Map<String, Object> map, String collectionName) {
		String mongoId = (String) map.get("mongoId");
		String esId = (String) map.get("ESId");
		Operator operator = (Operator) map.get("operator");
		String result = (String) map.get("result");
		LogEntity logEntity = new LogEntity();
		logEntity.setESId(esId);
		logEntity.setCreateDate(new Date());
		logEntity.setMongoId(mongoId);
		logEntity.setOperator(operator);
		logEntity.setResult(result);
		baseMongoDao.insert(logEntity, collectionName);
	}

	public String findOne(Map<String, Object> map, String collectionName) {
		Map record = (Map) baseMongoDao.findOne(map, collectionName, Map.class);
		return record.toString();
	}

	/**
	 * 根据mongoId从mongoDB中查询数据，并格式化日期字段
	 * 
	 * @param id
	 * @param collectionName
	 * @return 转化为Map结构
	 */
	public Map parseDataByObjectId(String id, String collectionName) {
		Map map = (Map) baseMongoDao.findByObjectId(id, collectionName, Map.class);
		map.remove("_id");
		map.put("uid", id);
		try{
			String createDate = (String) map.get("create_date");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createDateStr = sdf.format(new Date(Long.parseLong(createDate) * 1000));
			map.put("create_date", createDateStr);
			String update_date = (String) map.get("update_date");
			String updateDateStr = sdf.format(new Date(Long.parseLong(update_date) * 1000));
			map.put("update_date", updateDateStr);
		}catch (NumberFormatException e) {
			String record = format("create_date and update_date format exception,mongoid=%s",id);
			log.info(record);
		}
		return map;
	}

}
