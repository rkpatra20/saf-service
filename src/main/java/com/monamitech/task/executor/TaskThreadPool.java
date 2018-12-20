package com.monamitech.task.executor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.monamitech.model.SAFModel;

public class TaskThreadPool {

	private static final int threadPoolSize = 20;

	public static void execute(List<SAFModel> models) {
		if (models != null && models.size() > 0) {
			ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

			for (int i = 0; i < models.size(); i++) {
				Runnable runner = new TaskRunner(models.get(i));
				executor.execute(runner);
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
			}
		}
	}
}
