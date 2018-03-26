package com.pubhealth.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregator;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregator;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.BaseField;
import com.pubhealth.entity.ES.BoolField;
import com.pubhealth.entity.ES.CollectionField;
import com.pubhealth.entity.ES.ESMetrics;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESConnector;
import com.pubhealth.entity.ES.MatchField;
import com.pubhealth.entity.ES.MutilMatchField;
import com.pubhealth.entity.ES.PartField;
import com.pubhealth.entity.ES.RangeField;

import static java.lang.String.format;

@Component("ESQueryWrapper")
public class ESQueryWrapper {
	
	private final RestHighLevelClient client = ESConnector.getClient();

	public ESQueryWrapper() {
	}

	public void wrapperBuilder(SearchSourceBuilder searchSourceBuilder, ESParam param) {
		int from = param.getFrom();
		int size = param.getSize();
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(size);
		// 传入查询字段
		if (param.searchedFields != null) {
			searchSourceBuilder.fetchSource(param.searchedFields, null);
		}
		// 排序
		if (param.sortKeys != null) {
			for (String key : param.sortKeys.keySet()) {
				FieldSortBuilder fieldSort = SortBuilders.fieldSort(key)
						.order(param.sortKeys.get(key) ? SortOrder.ASC : SortOrder.DESC);
				searchSourceBuilder.sort(fieldSort);
			}
		} else {
			searchSourceBuilder.sort(SortBuilders.scoreSort());
		}
	}

	public SearchResponse commonQuery(ESParam param, Index index) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 设置排序字段
		wrapperBuilder(searchSourceBuilder, param);
		// 装载查询参数

		List<BaseField> fieldList = param.getFieldList();

		// 传入 查询参数
		if (fieldList != null) {
			searchSourceBuilder.query(wrapperBoolQuery(fieldList));
		}
		// 传入聚合条件
		addAggregationQuery(searchSourceBuilder, param);
		return search(searchSourceBuilder, index);
	}

	public BoolQueryBuilder wrapperBoolQuery(List<BaseField> fieldList) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		for (BaseField eskv : fieldList) {
			QueryBuilder tempQueryBuilder = null;
			String key = eskv.getFieldName();
			switch (eskv.flag) {
			case TERM:
				tempQueryBuilder = QueryBuilders.termQuery(key, ((TermField) eskv).getFieldValue());
				break;
			case TERMS:
				tempQueryBuilder = QueryBuilders.termsQuery(key, ((CollectionField) eskv).getFieldCollection());
				break;
			case MUITL_MATCH:
				MutilMatchField mutilField = (MutilMatchField) eskv;
				MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders
						.multiMatchQuery(mutilField.getFieldValue(), mutilField.getMutilFields());
				multiMatchQueryBuilder.operator(mutilField.getOperator());
				multiMatchQueryBuilder.minimumShouldMatch(mutilField.getMinimum_should_match());
				multiMatchQueryBuilder.boost(mutilField.getBoost());
				multiMatchQueryBuilder.type(mutilField.getType());
				tempQueryBuilder = multiMatchQueryBuilder;
				break;
			case FUZZY:
				break;
			case EXIST:
				tempQueryBuilder = QueryBuilders.existsQuery(key);
				break;
			case MATCH:
				MatchField matchField = (MatchField) eskv;
				MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(key, matchField.getFieldValue());
				matchQueryBuilder.operator(matchField.getOperator());
				matchQueryBuilder.minimumShouldMatch(matchField.getMinimum_should_match());
				matchQueryBuilder.boost(matchField.getBoost());
				matchQueryBuilder.type(matchField.getType());
				tempQueryBuilder = matchQueryBuilder;
				break;
			case MATCH_PHRASE:
				tempQueryBuilder = QueryBuilders.matchPhraseQuery(eskv.getFieldName(), eskv.getFieldValue());
				break;
			case WILDCARD:
				PartField wildPartField = (PartField) eskv;
				WildcardQueryBuilder wildQueryBuilder = QueryBuilders.wildcardQuery(key, wildPartField.getValue());
				tempQueryBuilder = wildQueryBuilder;
				break;
			case PREFIX:
				PartField prefixPartField = (PartField) eskv;
				PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery(key, prefixPartField.getValue());
				tempQueryBuilder = prefixQueryBuilder;
				break;
			case REGEXP:
				PartField regexpPartField = (PartField) eskv;
				RegexpQueryBuilder regexpQueryBuilder = QueryBuilders.regexpQuery(key, regexpPartField.getValue());
				tempQueryBuilder = regexpQueryBuilder;
				break;
			case RANGE:
				RangeField rangeField = (RangeField) eskv;
				RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(key);
				rangeQueryBuilder.from(rangeField.getLowerValue(), rangeField.isIncludeLower());
				rangeQueryBuilder.to(rangeField.getUpperValue(), rangeField.isIncludeUpper());
				tempQueryBuilder = rangeQueryBuilder;
				break;
			case BOOL:
				List<BaseField> childFieldList = ((BoolField) eskv).getChildBool();
				QueryBuilder boolQueryBuilder = wrapperBoolQuery(childFieldList);
				tempQueryBuilder = boolQueryBuilder;
				break;
			default:
				break;
			}
			switch (eskv.searchType) {
			case MUST:
				boolQuery.must(tempQueryBuilder);
				break;
			case MUST_NOT:
				boolQuery.mustNot(tempQueryBuilder);
				break;
			case FILTER:
				boolQuery.filter(tempQueryBuilder);
				break;
			case SHOULD:
				boolQuery.should(tempQueryBuilder).minimumShouldMatch(1);
				break;
			}

		}
		return boolQuery;
	}

	public SearchResponse search(SearchSourceBuilder searchSourceBuilder, Index index) {
		SearchRequest searchRequest = new SearchRequest(index.getName());
		searchRequest.types(index.getType());
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = client.search(searchRequest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchResponse;
	}

	public void aggretationQuery1() {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		TermsAggregationBuilder aggregation = AggregationBuilders.terms("count_age").field("category");
		SumAggregationBuilder sumAggregation = AggregationBuilders.sum("age_sum").field("age");
		MinAggregationBuilder minAggregation = AggregationBuilders.min("age_min").field("age");
		AvgAggregationBuilder avgAggregation = AggregationBuilders.avg("age_avg").field("age");
		MaxAggregationBuilder maxAggregation = AggregationBuilders.max("age_max").field("age");
		aggregation.subAggregation(sumAggregation);
		aggregation.subAggregation(avgAggregation);
		aggregation.subAggregation(maxAggregation);
		aggregation.subAggregation(minAggregation);
		searchSourceBuilder.aggregation(aggregation);
		SearchResponse searchResponse = search(searchSourceBuilder, new Index(null, null));
		Aggregations aggregations = searchResponse.getAggregations();
		Terms terms = aggregations.get("count_age");
		List<? extends Bucket> buckets = terms.getBuckets();
		for (Bucket bucket : buckets) {
			Sum sum = bucket.getAggregations().get("age_sum");
			Avg avg = bucket.getAggregations().get("age_avg");
			double sum_value = sum.getValue();
			double avg_value = avg.getValue();
			String item_result = format("%s:%d,%f,%f", bucket.getKey(), bucket.getDocCount(), sum_value, avg_value);
			System.out.println(item_result);
		}
	}

	public void aggretationQuery() {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		TermsAggregationBuilder classAggregation = AggregationBuilders.terms("term_class").field("category");
		TermsAggregationBuilder ageAggregation = AggregationBuilders.terms("term_age").field("age");
		AvgAggregationBuilder avgHeightAggregation = AggregationBuilders.avg("avg_height").field("height");
		classAggregation.subAggregation(ageAggregation);
		ageAggregation.subAggregation(avgHeightAggregation);
		searchSourceBuilder.aggregation(classAggregation);
		SearchResponse searchResponse = search(searchSourceBuilder, new Index(null, null));
		Aggregations aggregations = searchResponse.getAggregations();
		Terms terms = aggregations.get("term_class");
		List<? extends Bucket> buckets = terms.getBuckets();
		for (Bucket bucket : buckets) {
			Terms ages = bucket.getAggregations().get("term_age");
			List<? extends Bucket> ageBuckets = ages.getBuckets();
			for (Bucket ageBucket : ageBuckets) {
				Avg avgHeight = ageBucket.getAggregations().get("avg_height");
				String item_result = format("%s-%s:%d,%f", bucket.getKey(), ageBucket.getKey(), ageBucket.getDocCount(),
						avgHeight.getValue());
				System.out.println(item_result);
			}

		}
	}

	// 添加聚合条件
	public void addAggregationQuery(SearchSourceBuilder searchSourceBuilder, ESParam param) {
		if (param.aggregationFields != null) {
			List<String> fields = param.aggregationFields;
			TermsAggregationBuilder bootAggregation = AggregationBuilders.terms("term_" + fields.get(0))
					.field(fields.get(0));
			TermsAggregationBuilder curAggregation = bootAggregation;
			for (int i = 1; i < fields.size(); i++) {
				TermsAggregationBuilder itemAggregation = AggregationBuilders.terms("term_" + fields.get(i))
						.field(fields.get(i));
				// itemAggregation.size(100);
				curAggregation.subAggregation(itemAggregation);
				curAggregation = itemAggregation;
			}
			if (param.aggregationMetrics != null) {
				Map<String, ESMetrics[]> metrics = param.aggregationMetrics;
				for (String f : metrics.keySet()) {
					ESMetrics[] metricArray = metrics.get(f);
					for (int i = 0; i < metricArray.length; i++) {
						switch (metricArray[i]) {
						case MAX:
							MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max("max_" + f).field(f);
							curAggregation.subAggregation(maxAggregationBuilder);
							break;
						case AVG:
							AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avg_" + f).field(f);
							curAggregation.subAggregation(avgAggregationBuilder);
							break;
						case SUM:
							SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sum_" + f).field(f);
							curAggregation.subAggregation(sumAggregationBuilder);
							break;
						case MIN:
							MinAggregationBuilder minAggregationBuilder = AggregationBuilders.min("min_" + f).field(f);
							curAggregation.subAggregation(minAggregationBuilder);
							break;
						case CARDINALITY:
							CardinalityAggregationBuilder cardinalityAggregationBuilder = AggregationBuilders
									.cardinality("cardinality_" + f).field(f);
							curAggregation.subAggregation(cardinalityAggregationBuilder);
							break;
						case STATS:
							StatsAggregationBuilder statsAggregationBuilder = AggregationBuilders.stats("stats_" + f)
									.field(f);
							curAggregation.subAggregation(statsAggregationBuilder);
						}
					}
				}
			}
			searchSourceBuilder.aggregation(bootAggregation);
		}
	}

}
