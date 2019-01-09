package com.monamitech.task.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SAFModel {

	private Integer safId;

	private String serviceName;

	private Integer retryCount;

	private String request;

	private String response;

	private List<SAFRetryFailure> retryFailures;

	private String fromSystem;

	private String toSystem;

	private Date insertedDt;

	private String status;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public SAFModel(String serviceName) {
		super();
		this.serviceName = serviceName;
	}

	public SAFModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getRetryCount() {
		if(retryCount==null)
			return 0;
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getFromSystem() {
		return fromSystem;
	}

	public void setFromSystem(String fromSystem) {
		this.fromSystem = fromSystem;
	}

	public String getToSystem() {
		return toSystem;
	}

	public void setToSystem(String toSystem) {
		this.toSystem = toSystem;
	}

	public Date getInsertedDt() {
		return insertedDt;
	}

	public void setInsertedDt(Date insertedDt) {
		this.insertedDt = insertedDt;
	}

	public List<SAFRetryFailure> getRetryFailures() {
		if(retryFailures==null)
		{
			retryFailures=new ArrayList<>();
		}
		return retryFailures;
	}

	public void setRetryFailures(List<SAFRetryFailure> retryFailures) {
		this.retryFailures = retryFailures;
	}

	public Integer getSafId() {
		if(safId==null)
			return 0;
		return safId;
	}

	public void setSafId(Integer safId) {
		this.safId = safId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
