package com.monamitech.task.importer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.monamitech.task.dao.SAFModelDao;
import com.monamitech.task.executor.TaskThreadPool;
import com.monamitech.task.model.SAFModel;
import com.monamitech.task.model.SAFStatus;

@Component
public class TaskImporter {

	@Autowired
	private SAFModelDao safModelDao;

	public void importTask(int retryCount, String status, String serviceBean) {
		SAFModel model = new SAFModel();
		model.setRetryCount(retryCount);
		model.setStatus(SAFStatus.valueOf(status).name());
		model.setServiceName(serviceBean);
		List<SAFModel> models = safModelDao.importSAFModel(model);
		models.add(new SAFModel("loanTaskMgrImpl"));
		models.add(new SAFModel("test"));
		TaskThreadPool.execute(models, safModelDao);
	}

}
