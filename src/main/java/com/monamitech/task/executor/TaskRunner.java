package com.monamitech.task.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monamitech.task.dao.SAFModelDao;
import com.monamitech.task.mgr.ITaskManager;
import com.monamitech.task.model.SAFModel;
import com.monamitech.task.util.SAFUtils;

public class TaskRunner implements Runnable {

	private static Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);
	private SAFModel model;
	private SAFModelDao safModelDao;

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

	public TaskRunner(SAFModel model, SAFModelDao safModelDao) {
		super();
		this.model = model;
		this.safModelDao = safModelDao;
	}

	@Override
	public void run() {
		ITaskManager taskManager = null;
		if (model == null || model.getServiceName() == null) {
			LOGGER.warn("unable to process model");
			return;
		}
		taskManager = SAFUtils.getTaskManager(model.getServiceName());
		Integer safId = new Integer(model.getSafId());
		Integer retryCount=new Integer(model.getRetryCount());
		try {
			taskManager.executeTask(model);
			int count=safModelDao.updateSAFModelOnSuccess(safId, model.getResponse(),retryCount);
			LOGGER.info("rows updated:{} for SAF_ID: {}",count,safId);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			String errorJson = e.getClass().getName();
			int count=safModelDao.updateSAFModelOnError(safId, errorJson,retryCount);
			LOGGER.warn("rows updated:{} for SAF_ID: {}",count,safId);
		}

	}

}
