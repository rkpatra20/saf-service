package com.monamitech.task.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monamitech.task.dao.SAFModelDao;
import com.monamitech.task.model.SAFModel;

@RestController
@RequestMapping("/")
public class SAFController {

	private SAFModelDao safModelDao;

	@GetMapping("/active")
	public String active() {
		return "ACTIVE: " + new Date();
	}

	@PostMapping("/")
	public SAFModel addSAFModel(@RequestBody SAFModel model) {
		return safModelDao.insertSAFModel(model);
	}

}
