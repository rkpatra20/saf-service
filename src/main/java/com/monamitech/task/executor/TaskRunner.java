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
		String lockKey = this.model.getSafId() + this.model.getServiceName();
		synchronized (lockKey.intern()) {
			forwardToTaskImpl();
		}
	}

	public void forwardToTaskImpl() {
		ITaskManager taskManager = null;
		if (model == null || model.getServiceName() == null) {
			LOGGER.warn("unable to process model");
			return;
		}
		taskManager = SAFUtils.getTaskManager(model.getServiceName());
		Integer safId = new Integer(model.getSafId());
		Integer retryCount = new Integer(model.getRetryCount());
		boolean isTaskExecutedSuccessfully = false;
		try {
			taskManager.executeTask(model);
			isTaskExecutedSuccessfully = true;
		} catch (Exception e) {
			LOGGER.error("FAILURE_SAF_ID: " + safId);
			LOGGER.error(e.toString(), e);
			String errorJson = e.getClass().getName();
			int count = safModelDao.updateSAFModelOnError(safId, errorJson, retryCount+1);
			LOGGER.warn("task failed!! rows updated:{} for SAF_ID: {}", count, safId);
		} finally {
			if (isTaskExecutedSuccessfully) {
				int count = safModelDao.updateSAFModelOnSuccess(safId, model.getResponse(), retryCount+1);
				LOGGER.error("SUCCESS_SAF_ID: " + safId);
				LOGGER.info("task success!! rows updated:{} for SAF_ID: {}", count, safId);
			}
		}
	}

}
