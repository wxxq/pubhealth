package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.InfectionDoc;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.RangeField;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class InfectionService {
	Index index = new Index("infections", "infections");

	@Autowired
	private ESQueryWrapper dao;
	
	public String searchInfectionDoc(InfectionDoc infectionDoc){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(infectionDoc.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", infectionDoc.getProvinceId(), ESSearchType.FILTER));
		}
		if(infectionDoc.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", infectionDoc.getCityId(), ESSearchType.FILTER));
		}
		if(infectionDoc.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", infectionDoc.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(infectionDoc.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", infectionDoc.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(infectionDoc.getPersonalName())){
			param.fieldList.add(new TermField("responsible.keyword", infectionDoc.getPersonalName(), ESSearchType.FILTER));
		}
	
		if(StringUtils.isNotEmpty(infectionDoc.getFromTime()) || StringUtils.isNotEmpty(infectionDoc.getToTime())) {
			param.fieldList.add(new RangeField("date", infectionDoc.getFromTime(), true, infectionDoc.getToTime(), true, ESSearchType.MUST));
		}
		
		if(StringUtils.isNotEmpty(infectionDoc.getUniqueNumber())){
			param.fieldList.add(new TermField("unique_number.keyword", infectionDoc.getUniqueNumber(),ESSearchType.FILTER));
		}
		
		if(infectionDoc.getFirstIndex()>0){
			param.setFrom(infectionDoc.getFirstIndex());
		}
		param.setSize(infectionDoc.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
