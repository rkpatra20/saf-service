package com.monamitech;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.monamitech.task.dao.SAFModelDao;
import com.monamitech.task.model.SAFModel;

@SpringBootApplication
public class Starter {

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "local");
		SpringApplication.run(Starter.class, args);
	}
}

@Profile("local")
@Component
class SAFModelLoaderOnStartUp
{
	@Autowired
	private SAFModelDao safModelDao;
	@PostConstruct
	public void init()
	{
		loadSAFModel();
	}
	private void loadSAFModel() {
		
		for(int i=0;i<10;i++)
		{
			SAFModel model=new SAFModel();
			model.setServiceName("loanTaskMgrImpl");
			model.setRequest("https://google.co.in");
			model.setFromSystem("system1");
			model.setToSystem("system2");
			model.setInsertedDt(new Date());
		    safModelDao.insertSAFModel(model);
		}
		
		//System.out.println(safModelDao.selectAll(null));
	}
	
}
