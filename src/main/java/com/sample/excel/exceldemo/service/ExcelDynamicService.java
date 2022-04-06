package com.sample.excel.exceldemo.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.excel.exceldemo.controller.ExcelConfig;
import com.sample.excel.exceldemo.model.ConfigObj;
import com.sample.excel.exceldemo.model.KeyObj;

@Service
public class ExcelDynamicService {

	private static final Logger logger = LoggerFactory.getLogger(ExcelDynamicService.class);

	@Autowired
	private ExcelConfig excelConfig;

	public String updateExcelData() {

		ObjectMapper mapper = new ObjectMapper();

		InputStream inputStream = ConfigObj.class.getResourceAsStream("/configdata.json");
		try {
			ConfigObj configData = mapper.readValue(inputStream, ConfigObj.class);

			Collection<KeyObj> totalKeys = configData.getSnapshot().getDynamicFolders().getkeyProperties().values();

			int count = 0;

			int max = 3;

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Java Books");
			int rowCount = 0;
			boolean lastRecordSuccess = false;
			for (KeyObj key : totalKeys) {

				if (count < max) {
					generateExcelRow(sheet, ++rowCount, key, "1.0");
					count++;
					lastRecordSuccess = false;


				} else {
					generateExcelRow(sheet, ++rowCount, key, "1.0");

					String excelFilePath = generateExcelFileName(0);

					logger.info("Reading the cell data and Update the cell data is completed");

					FileOutputStream fileOut = new FileOutputStream(excelFilePath);
					workbook.write(fileOut);
					fileOut.close();


					// clean up

					count = 0;
					rowCount = 0;
					workbook = new XSSFWorkbook();
					sheet = workbook.createSheet("Java Books");
					lastRecordSuccess = true;
				}

			}

			// Last

			if (!lastRecordSuccess) {

				String excelFilePath = generateExcelFileName(0);
				FileOutputStream fileOut = new FileOutputStream(excelFilePath);
				workbook.write(fileOut);
				fileOut.close();
			}

			logger.info("object is {}", configData);
		} catch (IOException e) {

			e.printStackTrace();
		}

		return "Update Successful";
	}

	private void generateExcelRow(XSSFSheet sheet, int rowCount, KeyObj key, String string) {

		Row row = sheet.createRow(rowCount);

		Cell cell = row.createCell(0);
		cell.setCellValue((String) excelConfig.getName());

		Cell cell1 = row.createCell(1);
		cell1.setCellValue((Integer) excelConfig.getStreetNo());

		Cell cell2 = row.createCell(2);
		cell2.setCellValue((String) key.getKey());

	}

	private XSSFSheet createWorkbookAndSheet() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Java Books");
		return sheet;
	}

	private void readAndUpdateExcelFile(String excelPath) {

		logger.info("Processing the Excel file is Started");
		FileInputStream fis = null;
		FileOutputStream fileOut = null;
		Workbook wb = null;
		Sheet sh = null;
		try {

			String excelFilePath = generateExcelFileName(0);

			logger.info("Reading the cell data and Update the cell data is completed");

			fileOut = new FileOutputStream(excelFilePath);
			wb.write(fileOut);

			logger.info("Update the cell data in the excel is completed");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error occured {}", e.getMessage());
		}

		finally {
			try {
				fis.close();
				fileOut.flush();
				fileOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private String generateExcelFileName(int n) {
		// TODO Auto-generated method stub
		return "Excel_" + n + "_" + RandomStringUtils.randomNumeric(8);
	}

	public String getCellData(Sheet sh, int rownum, int colnum) throws Exception {
		try {
			Cell cell = sh.getRow(rownum).getCell(colnum);
			String CellData = null;
			switch (cell.getCellType()) {
			case STRING:
				CellData = cell.getStringCellValue();
				break;
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					CellData = String.valueOf(cell.getDateCellValue());
				} else {
					CellData = String.valueOf((long) cell.getNumericCellValue());
				}
				break;
			case BOOLEAN:
				CellData = Boolean.toString(cell.getBooleanCellValue());
				break;
			case BLANK:
				CellData = "";
				break;
			}
			return CellData;
		} catch (Exception e) {

			logger.error("Reading the cell data for the row{} and Column {} is failed", rownum, colnum);
			return "";
		}
	}

//	public String getCellData(Sheet sh, String columnName, int rownum) throws Exception {
//		return getCellData(sh, rownum, columns.get(columnName));
//	}
//
//	public void updateCellData(Sheet sh, String text, String columnName, int rownum) throws Exception {
//		updateCellData(sh, text, rownum, columns.get(columnName));
//	}
//
//	public void updateCellData(Sheet sh, String text, int rownum, int colnum) throws Exception {
//		try {
//			Row row = sh.getRow(rownum);
//			if (row == null) {
//				row = sh.createRow(rownum);
//			}
//			Cell cell = row.getCell(colnum);
//
//			if (cell == null) {
//				cell = row.createCell(colnum);
//			}
//			cell.setCellValue(text);
//
//		} catch (Exception e) {
//			throw (e);
//		}
//	}

}
