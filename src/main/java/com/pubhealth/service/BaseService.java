package com.pubhealth.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.pubhealth.dao.BaseMongoDao;
import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

/*
 * @author melo
*/
@Service
public class BaseService {

	@Autowired
	private BaseMongoDao baseMongoDao;

	@Autowired
	private ESQueryWrapper dao;

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
	 * @param ESId 文档id
	 * @param content 更新的内容
	 * @param index
	 * @return
	 */
	public String updateDocumentByESId(String ESId, Map m, Index index) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", ESId);
		map.put("content", m);
		String result = dao.updateDocument(map, index);
		return result;
	}

	public void insert(Object obj, String collectionName) {
		baseMongoDao.insert(obj, collectionName);
	}

	public String findOne(Map<String, Object> map, String collectionName) {
		Map record = (Map) baseMongoDao.findOne(map, collectionName, Map.class);
		return record.toString();
	}

	public Map parseDataByObjectId(String id, String collectionName) {
		Map map = (Map) baseMongoDao.findByObjectId(id, collectionName, Map.class);
		map.remove("_id");
		map.put("uid", id);
		String createDate=(String) map.get("create_date");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createDateStr = sdf.format(new Date(Long.parseLong(createDate)*1000));   
		map.put("create_date", createDateStr);
		String update_date=(String) map.get("update_date");
		String updateDateStr = sdf.format(new Date(Long.parseLong(update_date)*1000));   
		map.put("update_date", updateDateStr);
		return map;
	}

}
