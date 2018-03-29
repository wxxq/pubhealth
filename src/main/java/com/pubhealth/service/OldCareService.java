package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.OldCare;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class OldCareService {
	Index index = new Index("oldman_care", "oldman_care");
	
	@Autowired
	private ESQueryWrapper dao;
	public String search(OldCare oldCare){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(oldCare.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", oldCare.getProvinceId(), ESSearchType.FILTER));
		}
		if(oldCare.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", oldCare.getCityId(), ESSearchType.FILTER));
		}
		if(oldCare.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", oldCare.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(oldCare.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", oldCare.getIdCard(),ESSearchType.FILTER));
		}
// 表里面没有名字字段
//		if(StringUtils.isNotEmpty(oldCare.getPersonalName())){
//			param.fieldList.add(new TermField("collection_name.keyword", oldCare.getPersonalName(), ESSearchType.FILTER));
//		}
//		if(StringUtils.isNotEmpty(oldCare.getPhone())){
//			param.fieldList.add(new TermField("contacts_phone.keyword", oldCare.getPhone(),ESSearchType.FILTER));
//		}
		if(oldCare.getFirstIndex()>0){
			param.setFrom(oldCare.getFirstIndex());
		}
		param.setSize(oldCare.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
