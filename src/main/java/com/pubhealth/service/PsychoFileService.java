package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.PsychoFile;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class PsychoFileService {
	Index index = new Index("psychosis_file", "psychosis_file");
	
	@Autowired
	private ESQueryWrapper dao;
	public String search(PsychoFile psychoFile){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(psychoFile.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", psychoFile.getProvinceId(), ESSearchType.FILTER));
		}
		if(psychoFile.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", psychoFile.getCityId(), ESSearchType.FILTER));
		}
		if(psychoFile.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", psychoFile.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(psychoFile.getIdCard())){
			param.fieldList.add(new TermField("id_card.keyword", psychoFile.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(psychoFile.getPersonalName())){
			param.fieldList.add(new TermField("informed_consent_name.keyword", psychoFile.getPersonalName(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(psychoFile.getUniqueNumber())){
			param.fieldList.add(new TermField("unique_number.keyword", psychoFile.getUniqueNumber(),ESSearchType.FILTER));
		}
//		if(StringUtils.isNotEmpty(psychoFile.getPhone())){
//			param.fieldList.add(new TermField("contacts_phone.keyword", psychoFile.getPhone(),ESSearchType.FILTER));
//		}
		if(psychoFile.getFirstIndex()>0){
			param.setFrom(psychoFile.getFirstIndex());
		}
		param.setSize(psychoFile.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
