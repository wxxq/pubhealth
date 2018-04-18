package com.pubhealth.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class Page {
	/**
	 * 默认页码
	 */
	public static final int DEFAULT_PAGE_NO = 1;
	/**
	 * 默认页面大小
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;

	private int pageNo = DEFAULT_PAGE_NO; // 页码
	private int pageSize = DEFAULT_PAGE_SIZE; // 页面大小
	private int totalCount; // 总的记录数
	
	private String sort;
	
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public Map<String, Boolean> getSortKeys(){
		Map<String, Boolean> sortKeys = new HashMap<String, Boolean>();
		if(StringUtils.isNotEmpty(sort)) {
			String[] keyValues = sort.split(";");
			for(String item: keyValues) {
				String[] kv = item.split(":");
				sortKeys.put(kv[0], kv[1].equals("asc") ? true:false);
			}
		}
		
		return sortKeys;
	}

	public Page() {
	}

	public Page(int pageNo, int pageSize) {
		setPageNo(pageNo);
		setPageSize(pageSize);
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		if (pageNo < 1) {
			pageNo = DEFAULT_PAGE_NO;
		}
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize < 1) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 获得总的页码数量
	 * 
	 * @return
	 */
	public int getTotalPage() {
		if (totalCount % pageSize > 0) {
			return totalCount / pageSize + 1;
		} else {
			return totalCount / pageSize;
		}
	}

	/**
	 * 获取从哪一条记录开始查询
	 * 
	 * @return
	 */
	public int getFirstIndex() {
		return (pageNo - 1) * pageSize;
	}

	/**
	 * 获取最后一条记录的下标数（不包含）
	 * 
	 * @return
	 */
	public int getLastIndex() {
		return getFirstIndex() + pageSize;
	}

	/**
	 * 判断是否还有下一页
	 * 
	 * @return
	 */
	public boolean isHasNextPage() {
		return (pageNo + 1) <= getTotalPage();
	}

	/**
	 * 获取下一个页码，在调用之前先调用<code>isHasNextPage()</code>方法进行判断
	 * 
	 * @return
	 */
	public int getNextPage() {
		return pageNo + 1;
	}

	/**
	 * 判断是否还有上一页
	 * 
	 * @return
	 */
	public boolean isHasPrePage() {
		return (pageNo - 1) > 0;
	}

	/**
	 * 获取上一个页码，在调用之前先调用<code>isHasPrePage()</code>方法进行判断
	 * 
	 * @return
	 */
	public int getPrePage() {
		return pageNo - 1;
	}
}
