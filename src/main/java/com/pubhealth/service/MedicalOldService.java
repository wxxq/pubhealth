package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.MedicalOld;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class MedicalOldService {
	Index index = new Index("chinese_medical_oldman", "chinese_medical_oldman");
	
	@Autowired
	private ESQueryWrapper dao;
	public String search(MedicalOld medicalOld){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(medicalOld.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", medicalOld.getProvinceId(), ESSearchType.FILTER));
		}
		if(medicalOld.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", medicalOld.getCityId(), ESSearchType.FILTER));
		}
		if(medicalOld.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", medicalOld.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(medicalOld.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", medicalOld.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(medicalOld.getPersonalName())){
			param.fieldList.add(new TermField("doctor_signature.keyword", medicalOld.getPersonalName(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(medicalOld.getUniqueNumber())){
			param.fieldList.add(new TermField("unique_number.keyword", medicalOld.getUniqueNumber(),ESSearchType.FILTER));
		}
//		if(StringUtils.isNotEmpty(medicalOld.getPhone())){
//			param.fieldList.add(new TermField("contacts_phone.keyword", medicalOld.getPhone(),ESSearchType.FILTER));
//		}
		if(medicalOld.getFirstIndex()>0){
			param.setFrom(medicalOld.getFirstIndex());
		}
		param.setSize(medicalOld.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
