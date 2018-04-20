package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.ChildrenNewborn;
import com.pubhealth.entity.Hypertension;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class ChildrenNewbornService {
	Index index = new Index("children_newborn", "children_newborn");

	@Autowired
	private ESQueryWrapper dao;
	
	public String search(ChildrenNewborn childrenNewborn){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(childrenNewborn.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", childrenNewborn.getProvinceId(), ESSearchType.FILTER));
		}
		if(childrenNewborn.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", childrenNewborn.getCityId(), ESSearchType.FILTER));
		}
		if(childrenNewborn.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", childrenNewborn.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(childrenNewborn.getIdCard())){
			param.fieldList.add(new TermField("id_card.keyword", childrenNewborn.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(childrenNewborn.getPersonalName())){
			param.fieldList.add(new TermField("name.keyword", childrenNewborn.getPersonalName(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(childrenNewborn.getUniqueNumber())){
			param.fieldList.add(new TermField("unique_number.keyword", childrenNewborn.getUniqueNumber(),ESSearchType.FILTER));
		}
	
		if(StringUtils.isNotEmpty(childrenNewborn.getSort())) {
			param.setSortKeys(childrenNewborn.getSortKeys());
		}
		if(childrenNewborn.getFirstIndex()>0){
			param.setFrom(childrenNewborn.getFirstIndex());
		}
		param.setSize(childrenNewborn.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
