package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.ChildOne;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class ChildOneService {
	Index index = new Index("children_oneyears", "children_oneyears");
	
	@Autowired
	private ESQueryWrapper dao;
	public String search(ChildOne childOne){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(childOne.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", childOne.getProvinceId(), ESSearchType.FILTER));
		}
		if(childOne.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", childOne.getCityId(), ESSearchType.FILTER));
		}
		if(childOne.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", childOne.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(childOne.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", childOne.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(childOne.getPersonalName())){
			param.fieldList.add(new TermField("doctor_signature.keyword", childOne.getPersonalName(), ESSearchType.FILTER));
		}
//		if(StringUtils.isNotEmpty(childOne.getPhone())){
//			param.fieldList.add(new TermField("contacts_phone.keyword", childOne.getPhone(),ESSearchType.FILTER));
//		}
		if(childOne.getFirstIndex()>0){
			param.setFrom(childOne.getFirstIndex());
		}
		param.setSize(childOne.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
