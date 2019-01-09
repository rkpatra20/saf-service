package com.monamitech.task.controller;

import java.util.Date;
import java.util.List;

import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping("/{safId")
	public SAFModel findSAFModel(@PathVariable("safId") Integer safId) {
		return safModelDao.findById(safId);
	}
	
	@GetMapping("/saf-list")
	public List<SAFModel> findAllSAFModel(@RequestParam("status") String status)
	{
		return safModelDao.selectAll(status);
	}

}
