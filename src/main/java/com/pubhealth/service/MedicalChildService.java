package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.MedicalChild;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class MedicalChildService {
	Index index = new Index("chinese_medical_children", "chinese_medical_children");
	
	@Autowired
	private ESQueryWrapper dao;
	public String search(MedicalChild medicalChild){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(medicalChild.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", medicalChild.getProvinceId(), ESSearchType.FILTER));
		}
		if(medicalChild.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", medicalChild.getCityId(), ESSearchType.FILTER));
		}
		if(medicalChild.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", medicalChild.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(medicalChild.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", medicalChild.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(medicalChild.getPersonalName())){
			param.fieldList.add(new TermField("doctor_signature.keyword", medicalChild.getPersonalName(), ESSearchType.FILTER));
		}
//		if(StringUtils.isNotEmpty(medicalChild.getPhone())){
//			param.fieldList.add(new TermField("contacts_phone.keyword", medicalChild.getPhone(),ESSearchType.FILTER));
//		}
		if(medicalChild.getFirstIndex()>0){
			param.setFrom(medicalChild.getFirstIndex());
		}
		param.setSize(medicalChild.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
