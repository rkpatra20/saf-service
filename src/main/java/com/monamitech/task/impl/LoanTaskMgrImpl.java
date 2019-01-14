package com.monamitech.task.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.monamitech.task.importer.TaskImporter;
import com.monamitech.task.mgr.ITaskManager;
import com.monamitech.task.model.SAFModel;
import com.monamitech.task.model.SAFStatus;

@Component("loanTaskMgrImpl")
public class LoanTaskMgrImpl implements ITaskManager {

	private static Logger LOGGER = LoggerFactory.getLogger(LoanTaskMgrImpl.class);

	@Autowired
	private TaskImporter taskImporter;

	@Scheduled(cron = "0/2 * * * * ?")
	public void taskRunner() {
		
		taskImporter.importTask(10, SAFStatus.PENDING.name(),"loanTaskMgrImpl");
	}

	@Override
	public void executeTask(SAFModel safModel) {
		// TODO Auto-generated method stub

		LOGGER.info("LoanTaskMgrImpl");

	}

}
