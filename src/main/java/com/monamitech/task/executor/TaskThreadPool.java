package com.monamitech.task.executor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.monamitech.task.dao.SAFModelDao;
import com.monamitech.task.model.SAFModel;

public class TaskThreadPool {

	private static final int threadPoolSize = 10;

	public static void execute(List<SAFModel> models,SAFModelDao safModelDao) {
		if (models != null && models.size() > 0) {
			ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

			for (int i = 0; i < models.size(); i++) {
				Runnable runner = new TaskRunner(models.get(i),safModelDao);
				executor.execute(runner);
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
			}
		}
	}
}
