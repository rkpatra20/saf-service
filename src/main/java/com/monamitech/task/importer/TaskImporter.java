package com.monamitech.task.importer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.monamitech.task.dao.SAFModelDao;
import com.monamitech.task.executor.TaskThreadPool;
import com.monamitech.task.model.SAFModel;

@Component
public class TaskImporter {

	@Autowired
	private SAFModelDao safModelDao;
	public void importTask(int retryCount, String status,String serviceBean) {
		List<SAFModel> models = new ArrayList<>();
		models.add(new SAFModel("loanTaskMgrImpl"));
		models.add(new SAFModel("test"));
		TaskThreadPool.execute(models,safModelDao);
	}

}
