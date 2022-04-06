package com.sample.excel.exceldemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.excel.exceldemo.service.ExcelDynamicService;

@RestController
public class ExcelController {

	@Autowired
	private ExcelDynamicService excelService;

	@GetMapping(path = "/service/health")
	public String updateExcelData() {

		return excelService.updateExcelData();
	}

}
