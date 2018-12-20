package com.monamitech.task.executor;

import com.monamitech.model.SAFModel;
import com.monamitech.task.util.SAFUtils;

public class TaskRunner implements Runnable {

	private SAFModel model;

	public TaskRunner() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SAFModel getModel() {
		return model;
	}

	public void setModel(SAFModel model) {
		this.model = model;
	}

	public TaskRunner(SAFModel model) {
		super();
		this.model = model;
	}

	@Override
	public void run() {
       SAFUtils.getTaskManager(model.getServiceName()).executeTask(model);
	}

}
