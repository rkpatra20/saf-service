package com.monamitech.task.mgr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.monamitech.task.model.SAFModel;

@Component("defaultTaskMgrImpl")
public class DefaultTaskMgrImpl implements ITaskManager {

	private static Logger LOGGER = LoggerFactory.getLogger(DefaultTaskMgrImpl.class);

	@Override
	public void executeTask(SAFModel safModel) {
		LOGGER.warn("executing default task manager impl, SAF_ID"+safModel.getSafId());
		throw new RuntimeException("dummy exception");
	}

}
