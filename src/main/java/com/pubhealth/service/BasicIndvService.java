package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.BasicIndv;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class BasicIndvService {
	Index index = new Index("basicindividual", "basicindividual");
	
	@Autowired
	private ESQueryWrapper dao;
	public String search(BasicIndv basicIndv){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(basicIndv.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", basicIndv.getProvinceId(), ESSearchType.FILTER));
		}
		if(basicIndv.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", basicIndv.getCityId(), ESSearchType.FILTER));
		}
		if(basicIndv.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", basicIndv.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(basicIndv.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", basicIndv.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(basicIndv.getPersonalName())){
			param.fieldList.add(new TermField("personal_name.keyword", basicIndv.getPersonalName(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(basicIndv.getPhone())){
			param.fieldList.add(new TermField("contacts_phone.keyword", basicIndv.getPhone(),ESSearchType.FILTER));
		}
		if(basicIndv.getFirstIndex()>0){
			param.setFrom(basicIndv.getFirstIndex());
		}
		param.setSize(basicIndv.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
