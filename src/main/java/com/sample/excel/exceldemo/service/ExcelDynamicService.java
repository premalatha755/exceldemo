package com.sample.excel.exceldemo.service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.excel.exceldemo.config.ExcelConfig;
import com.sample.excel.exceldemo.model.ConfigObj;
import com.sample.excel.exceldemo.model.KeyObj;
import com.sample.excel.exceldemo.util.AppUtil;

@Service
public class ExcelDynamicService {

	private static final Logger logger = LoggerFactory.getLogger(ExcelDynamicService.class);

	@Autowired
	private ExcelConfig excelConfig;

	@Value("${excelpage.count}")
	private Integer max;

	public String updateExcelData() {

		logger.info("Updating the Excel Process Started");
		ObjectMapper mapper = new ObjectMapper();

		try {

			// Retrieve data from Config data file
			InputStream inputStream = ConfigObj.class.getResourceAsStream("/configdata.json");

			ConfigObj configData = mapper.readValue(inputStream, ConfigObj.class);

			Optional<Collection<KeyObj>> result = AppUtil
					.resolve(() -> configData.getSnapshot().getDynamicFolders().getkeyProperties().values());

			if (result.isEmpty()) {
				return "Key Data is not present";
			}

			Collection<KeyObj> totalKeys = result.get();
			logger.info("Reading data from file is completed");

			int pageCount = 0;

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("config");
			int rowCount = 0;
			boolean lastRecordSuccess = false;

			for (KeyObj key : totalKeys) {

				// Fetching rows based on the page size
				if (pageCount < max) {

					// Creating the Excel row for the each key
					generateExcelRow(sheet, ++rowCount, key, configData.getConfigVersion());
					pageCount++;
					lastRecordSuccess = false;

				} else {
					generateExcelRow(sheet, ++rowCount, key, configData.getConfigVersion());

					String excelFilePath = generateExcelFileName();

					generateExcelHeader(sheet);

					// Updating the excel with the page count of data

					FileOutputStream fileOut = new FileOutputStream(excelFilePath);
					workbook.write(fileOut);
					fileOut.close();

					// clean up

					pageCount = 0;
					rowCount = 0;

					workbook = new XSSFWorkbook();
					sheet = workbook.createSheet("config");
					lastRecordSuccess = true;
				}

			}

			// Last Record Less than page size

			if (!lastRecordSuccess) {

				String excelFilePath = generateExcelFileName();
				generateExcelHeader(sheet);
				FileOutputStream fileOut = new FileOutputStream(excelFilePath);
				workbook.write(fileOut);
				fileOut.close();
			}

		} catch (Exception e) {

			logger.error("Error while updating the data in the excel {}", e.getMessage());
			e.printStackTrace();
		}

		logger.info("Updating the Excel Process completed");

		return "Update Successful";
	}

	private void generateExcelHeader(XSSFSheet sheet) {

		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("Name");

		Cell cell1 = row.createCell(1);
		cell1.setCellValue("StreetNo");

		Cell cell2 = row.createCell(2);
		cell2.setCellValue("Key");

		Cell cell3 = row.createCell(3);
		cell3.setCellValue("Version");

	}

	private void generateExcelRow(XSSFSheet sheet, int rowCount, KeyObj key, String version) {

		Row row = sheet.createRow(rowCount);

		Cell cell = row.createCell(0);
		cell.setCellValue((String) excelConfig.getName());

		Cell cell1 = row.createCell(1);
		cell1.setCellValue((Integer) excelConfig.getStreetNo());

		Cell cell2 = row.createCell(2);
		cell2.setCellValue((String) key.getKey());

		Cell cell3 = row.createCell(3);
		cell3.setCellValue((String) version);

	}

	private String generateExcelFileName() {
		return "Excel_" + RandomStringUtils.randomNumeric(8) + ".xlsx";
	}

}
