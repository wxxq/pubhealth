package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.PsychoFile;
import com.pubhealth.entity.VaccRecord;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class VaccRecordService {
	Index index = new Index("vaccinate_record", "vaccinate_record");
	
	@Autowired
	private ESQueryWrapper dao;
	public String search(VaccRecord vccRecord){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(vccRecord.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", vccRecord.getProvinceId(), ESSearchType.FILTER));
		}
		if(vccRecord.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", vccRecord.getCityId(), ESSearchType.FILTER));
		}
		if(vccRecord.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", vccRecord.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(vccRecord.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", vccRecord.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(vccRecord.getPersonalName())){
			param.fieldList.add(new TermField("vaccination_doctor.keyword", vccRecord.getPersonalName(), ESSearchType.FILTER));
		}
		
		if(StringUtils.isNotEmpty(vccRecord.getBornId())){
			param.fieldList.add(new TermField("born_id.keyword", vccRecord.getBornId(), ESSearchType.FILTER));
		}
		
		if(StringUtils.isNotEmpty(vccRecord.getSort())) {
			param.setSortKeys(vccRecord.getSortKeys());
		}
//		if(StringUtils.isNotEmpty(psychoFile.getPhone())){
//			param.fieldList.add(new TermField("contacts_phone.keyword", psychoFile.getPhone(),ESSearchType.FILTER));
//		}
		if(vccRecord.getFirstIndex()>0){
			param.setFrom(vccRecord.getFirstIndex());
		}
		param.setSize(vccRecord.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
