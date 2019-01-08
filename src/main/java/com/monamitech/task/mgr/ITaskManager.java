package com.monamitech.task.mgr;

import org.springframework.scheduling.annotation.Async;

import com.monamitech.task.model.SAFModel;

public interface ITaskManager {

	@Async
	public void executeTask(SAFModel model);
}
