package com.sample.excel.exceldemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.excel.exceldemo.model.NotesModel;
import com.sample.excel.exceldemo.service.ApiService;
import com.sample.excel.exceldemo.service.ExcelService;

@RestController
public class ExcelController {

	@Autowired
	private ExcelService excelService;
	
	@Autowired
	private ApiService apiService;
	
	@GetMapping(path = "/service/health")
	public String updateExcelData() {
		
		
		return excelService.updateExcelData();
	}
	
	@GetMapping(path = "/service/posts")
	public NotesModel[] apiData() {
		
		return apiService.getApiResults();
	}
	
}
