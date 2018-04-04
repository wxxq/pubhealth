package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.SupervisionPatrol;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.RangeField;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class SupervisionPatrolService {
	private Index supervisionPatrolIndex = new Index("supervision_patrol", "supervision_patrol");
	
	@Autowired
	private ESQueryWrapper dao;
	
	public String search(SupervisionPatrol supervisionPatrol){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(supervisionPatrol.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", supervisionPatrol.getProvinceId(), ESSearchType.FILTER));
		}
		if(supervisionPatrol.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", supervisionPatrol.getCityId(), ESSearchType.FILTER));
		}
		if(supervisionPatrol.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", supervisionPatrol.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(supervisionPatrol.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", supervisionPatrol.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(supervisionPatrol.getPersonalName())){
			param.fieldList.add(new TermField("report_name.keyword", supervisionPatrol.getPersonalName(), ESSearchType.FILTER));
		}

		if(StringUtils.isNotEmpty(supervisionPatrol.getFromTime()) || StringUtils.isNotEmpty(supervisionPatrol.getToTime())) {
			param.fieldList.add(new RangeField("patrol_date", supervisionPatrol.getFromTime(), true, supervisionPatrol.getToTime(), true, ESSearchType.MUST));	

		}
		if(StringUtils.isNotEmpty(supervisionPatrol.getOrganization()) ) {
			param.fieldList.add(new TermField("organization.keyword", supervisionPatrol.getOrganization(), ESSearchType.FILTER));
		}
		if(supervisionPatrol.getId() != 0) {
			param.fieldList.add(new TermField("id", supervisionPatrol.getId(), ESSearchType.FILTER));
		}
		if(supervisionPatrol.getFirstIndex()>0){
			param.setFrom(supervisionPatrol.getFirstIndex());
		}
		param.setSize(supervisionPatrol.getPageSize());
		
		SearchResponse response = null;

		response = dao.commonQuery(param,supervisionPatrolIndex);
		
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
