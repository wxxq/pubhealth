package com.pubhealth.dao;

import java.util.List;
import java.util.Map;

/*
 * @author melo
*/
public interface BaseMongoDao {
	// 添加
	public void insert(Object object, String collectionName);

	// 查找所有
	public List<Object> findAll(Map<String, Object> params, String collectionName);

	// 修改
	public void update(Map<String, Object> params, String collectionName);

	// 创建集合
	public void createCollection(String collectionName);

	// 根据条件删除
	public void remove(Map<String, Object> params, String collectionName);

	public Object findOne(Map<String, Object> params, String collectionName, Class cls);

	Object findByObjectId(String id, String collectionName, Class cls);
}
