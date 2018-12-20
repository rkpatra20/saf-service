package com.monamitech.task.importer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.monamitech.model.SAFModel;
import com.monamitech.task.executor.TaskThreadPool;

@Component
public class TaskImporter {

	public void importTask(int retryCount, String status,String serviceBean) {
		List<SAFModel> models = new ArrayList<>();
		models.add(new SAFModel("loanTaskMgrImpl"));
		models.add(new SAFModel("test"));
		TaskThreadPool.execute(models);
	}

}
