package com.monamitech.task.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.monamitech.task.mgr.ITaskManager;

@Component
public class SAFUtils implements ApplicationContextAware{

	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.context=applicationContext;
	}

	private static ApplicationContext context;

	public static ITaskManager getTaskManager(String serviceBeanName) {
		ITaskManager mgr = null;
		try {
			mgr = context.getBean(serviceBeanName, ITaskManager.class);
		} catch (NoSuchBeanDefinitionException e) {
			mgr = context.getBean("defaultTaskMgrImpl", ITaskManager.class);
		}
		return mgr;
	}
}
