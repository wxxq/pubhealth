package com.pubhealth.entity.ES;

public enum ESQueryType {
	// 0 termQuery 1 matchQuery 2 rangeQuery
	TERM, TERMS, MATCH, RANGE, BOOL ,EXIST;
}
