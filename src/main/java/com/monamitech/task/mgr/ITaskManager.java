package com.monamitech.task.mgr;

import org.springframework.scheduling.annotation.Async;

import com.monamitech.model.SAFModel;

public interface ITaskManager {

	@Async
	public void executeTask(SAFModel model);
}
