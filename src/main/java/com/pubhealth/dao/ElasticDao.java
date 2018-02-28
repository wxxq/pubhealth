package com.pubhealth.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.pubhealth.entity.Document;
import com.pubhealth.entity.Index;

@Component
public class ElasticDao {
	private static Logger log = Logger.getLogger(ElasticDao.class);
	private final RestHighLevelClient client;
	private SearchSourceBuilder sourceBuilder;
	private Index index;
	private Gson gson;

	public ElasticDao(RestHighLevelClient client,SearchSourceBuilder searchSourceBuilder,Index index,Gson gson) {
		this.client = client;
		this.sourceBuilder = searchSourceBuilder;
		this.index = index;
		this.gson = gson;
	}
	
	public String createIndex(Index index,Document document){
		IndexRequest request = new IndexRequest(index.getName(), index.getType());
        request.source(gson.toJson(document), XContentType.JSON);
        IndexResponse response;
		try {
			response = client.index(request);
			return response.getId();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return "create index failed!";
	}
	
	public String updateDocument(Document document){
        try {
            UpdateRequest request = new UpdateRequest(index.getName(),
            		index.getType(), document.getId())
                    .doc(gson.toJson(document), XContentType.JSON);

            UpdateResponse response = client.update(request);
            return response.getId();
        } catch (Exception ex){
            log.error("The exception was thrown in updateDocument method. {} ", ex);
        }

        return null;
    }
	
	public List<Document> matchAllQuery() {

        List<Document> result = new ArrayList<Document>();
        try {
            flush();
            result = getDocuments(QueryBuilders.matchAllQuery());
        } catch (Exception ex){
            log.error("The exception was thrown in matchAllQuery method. {} ", ex);
        }

        return result;
    }

    /**
     *
     * @param query
     * @return
     */
    public List<Document> wildcardQuery(String query){

        List<Document> result = new ArrayList<Document>();

        try {
            result = getDocuments(QueryBuilders.wildcardQuery("_all", "*" + query.toLowerCase() + "*"));
        } catch (Exception ex){
            log.error("The exception was thrown in wildcardQuery method. {} ", ex);
        }

        return result;
    }

    /**
     *
     * @param id
     * @throws IOException
     */
    public void deleteDocument(String id){
        try {
            DeleteRequest deleteRequest = new DeleteRequest(index.getName(), index.getType(), id);
            client.delete(deleteRequest);
        } catch (Exception ex){
            log.error("The exception was thrown in deleteDocument method. {} ", ex);
        }
    }

    /**
     *
     * @return
     */
    private SearchRequest getSearchRequest(){
        SearchRequest searchRequest = new SearchRequest(index.getName());
        searchRequest.source(sourceBuilder);
        return searchRequest;
    }

    /**
     *
     * @param builder
     * @return
     * @throws IOException
     */
    private List<Document> getDocuments(AbstractQueryBuilder builder) throws IOException {
        List<Document> result = new ArrayList<Document>();

        sourceBuilder.query(builder);
        SearchRequest searchRequest = getSearchRequest();

        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            Document doc = gson.fromJson(hit.getSourceAsString(), Document.class);
            doc.setId(hit.getId());
            result.add(doc);
        }

        return result;
    }

    public void flush() throws IOException {
        String endPoint = String.join("/", index.getName(), "_flush");
        //client.getLowLevelClient().performRequest("POST", endPoint);
    }
    
	public void close(){
//		this.client.getLowLevelClient().close();
	}
}

