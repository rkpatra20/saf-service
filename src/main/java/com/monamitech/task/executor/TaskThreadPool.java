package com.monamitech.task.executor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monamitech.task.dao.SAFModelDao;
import com.monamitech.task.model.SAFModel;

public class TaskThreadPool {

	private static Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);
	public static void execute(List<SAFModel> models, SAFModelDao safModelDao) {
		if (models != null && models.size() > 0) {
			ExecutorService executor = Executors.newFixedThreadPool(getThreadPoolSize(models));
			try {
				for (int i = 0; i < models.size(); i++) {
					Runnable runner = new TaskRunner(models.get(i), safModelDao);
					executor.execute(runner);
				}
			} finally {
				LOGGER.debug("calling shutdown on executor: "+executor.hashCode());
				executor.shutdown();
				LOGGER.debug("called shutdown on executor: "+executor.hashCode());
				int seconds = 3 * 60; // 3 minutes=180 seconds
				try {
					executor.awaitTermination(seconds, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LOGGER.debug("executor status: "+executor.hashCode()+" : "+executor.isShutdown());
			}

		}
	}

	public static int getThreadPoolSize(List<SAFModel> models) {
		int size = 1;
		if (models.size() > 1) {
			size = models.size() / 2;
		}
		return size;
	}
}
