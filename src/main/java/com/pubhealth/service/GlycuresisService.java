package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.Glycuresis;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.InfectionDoc;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class GlycuresisService  {
	Index index = new Index("glycuresis", "glycuresis");

	@Autowired
	private ESQueryWrapper dao;
	
	public String search(Glycuresis glycuresis){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(glycuresis.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", glycuresis.getProvinceId(), ESSearchType.FILTER));
		}
		if(glycuresis.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", glycuresis.getCityId(), ESSearchType.FILTER));
		}
		if(glycuresis.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", glycuresis.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(glycuresis.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", glycuresis.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(glycuresis.getPersonalName())){
			param.fieldList.add(new TermField("doctor_signature.keyword", glycuresis.getPersonalName(), ESSearchType.FILTER));
		}
	
		if(StringUtils.isNotEmpty(glycuresis.getUniqueNumber())){
			param.fieldList.add(new TermField("unique_number.keyword", glycuresis.getUniqueNumber(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(glycuresis.getSort())) {
			param.setSortKeys(glycuresis.getSortKeys());
		}
		
		if(glycuresis.getFirstIndex()>0){
			param.setFrom(glycuresis.getFirstIndex());
		}
		param.setSize(glycuresis.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
