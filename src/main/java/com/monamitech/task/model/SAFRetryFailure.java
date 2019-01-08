package com.monamitech.task.model;

import java.util.Date;

public class SAFRetryFailure {

	private String failureResponse;

	private Date retryAt;

	private int currentRetryCount;

	public String getFailureResponse() {
		return failureResponse;
	}

	public void setFailureResponse(String failureResponse) {
		this.failureResponse = failureResponse;
	}

	public Date getRetryAt() {
		return retryAt;
	}

	public void setRetryAt(Date retryAt) {
		this.retryAt = retryAt;
	}

	public int getCurrentRetryCount() {
		return currentRetryCount;
	}

	public void setCurrentRetryCount(int currentRetryCount) {
		this.currentRetryCount = currentRetryCount;
	}

}
