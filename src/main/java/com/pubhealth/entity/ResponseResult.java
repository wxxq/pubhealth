package com.pubhealth.entity;

import java.util.List;

/*
 * @author melo
*/
public class ResponseResult<T> {
	private int status;
	private int count;
	private List<T> result;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
}


