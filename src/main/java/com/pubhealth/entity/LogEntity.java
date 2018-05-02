package com.pubhealth.entity;

import java.util.Date;

/*
 * @author melo
*/
public class LogEntity {
	private String mongoId;
	private String ESId;
	private Operator operator;
	private String result;
	private Date createDate;

	public String getMongoId() {
		return mongoId;
	}

	public void setMongoId(String mongoId) {
		this.mongoId = mongoId;
	}

	public String getESId() {
		return ESId;
	}

	public void setESId(String eSId) {
		ESId = eSId;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}

