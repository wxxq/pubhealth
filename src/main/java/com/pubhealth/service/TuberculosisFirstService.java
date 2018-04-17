package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.SupervisionPatrol;
import com.pubhealth.entity.TuberculosisFirst;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.RangeField;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class TuberculosisFirstService {
	private Index index = new Index("tuberculosis_first", "tuberculosis_first");
	@Autowired
	private ESQueryWrapper dao;
	
	public String search(TuberculosisFirst tuberculosisFirst){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(tuberculosisFirst.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", tuberculosisFirst.getProvinceId(), ESSearchType.FILTER));
		}
		if(tuberculosisFirst.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", tuberculosisFirst.getCityId(), ESSearchType.FILTER));
		}
		if(tuberculosisFirst.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", tuberculosisFirst.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(tuberculosisFirst.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", tuberculosisFirst.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(tuberculosisFirst.getPersonalName())){
			param.fieldList.add(new TermField("responsible.keyword", tuberculosisFirst.getPersonalName(), ESSearchType.FILTER));
		}
	
		
		if(StringUtils.isNotEmpty(tuberculosisFirst.getUniqueNumber())){
			param.fieldList.add(new TermField("unique_number.keyword", tuberculosisFirst.getUniqueNumber(),ESSearchType.FILTER));
		}

		
		if(tuberculosisFirst.getFirstIndex()>0){
			param.setFrom(tuberculosisFirst.getFirstIndex());
		}
		param.setSize(tuberculosisFirst.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
