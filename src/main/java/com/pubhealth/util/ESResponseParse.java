package com.pubhealth.util;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.JSONArray;
import org.json.JSONObject;

public class ESResponseParse {
	public static String parseJsonFromResponse(SearchResponse response) {
		SearchHits hits = response.getHits();
		long totalSize = hits.totalHits;
		JSONObject obj = new JSONObject();
		obj.put("count", totalSize);
		JSONArray arr = new JSONArray();
		for (SearchHit hit : hits) {
			String resultStr = hit.getSourceAsString();
			JSONObject item = new JSONObject(resultStr);
			arr.put(item);
		}
		obj.put("result", arr);
		obj.put("status", 1);
		return obj.toString();
	}

	public static String parseIdFromResponse(SearchResponse response) {
		String id = null;
		SearchHits hits = response.getHits();
		int length = (int) hits.totalHits;
		if (hits.totalHits > 0) {
			id = hits.getAt(length - 1).getId();
		}
		return id;
	}

}
