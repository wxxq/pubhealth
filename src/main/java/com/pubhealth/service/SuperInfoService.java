package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.SuperInfo;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class SuperInfoService {
	Index index = new Index("supervision_info", "supervision_info");
	
	@Autowired
	private ESQueryWrapper dao;
	public String search(SuperInfo superInfo){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(superInfo.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", superInfo.getProvinceId(), ESSearchType.FILTER));
		}
		if(superInfo.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", superInfo.getCityId(), ESSearchType.FILTER));
		}
		if(superInfo.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", superInfo.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(superInfo.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", superInfo.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(superInfo.getPersonalName())){
			param.fieldList.add(new TermField("report_name.keyword", superInfo.getPersonalName(), ESSearchType.FILTER));
		}
//		if(StringUtils.isNotEmpty(superInfo.getPhone())){
//			param.fieldList.add(new TermField("contacts_phone.keyword", superInfo.getPhone(),ESSearchType.FILTER));
//		}
		if(superInfo.getFirstIndex()>0){
			param.setFrom(superInfo.getFirstIndex());
		}
		param.setSize(superInfo.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}