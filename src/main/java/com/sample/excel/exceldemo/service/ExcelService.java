package com.sample.excel.exceldemo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ExcelService {

	private Map<String, Integer> columns = new HashMap<>();
	HashMap<String ,List<String>> responseData = new HashMap<>();
    private FileInputStream fis;
    private FileOutputStream fileOut;
    private Workbook wb;
    private Sheet sh;
    private Cell cell;
    private Row row;

    private String excelFilePath;

	public String updateExcelData() {

		populateResponseData();

		readExcelFile("/Users/premalathachaluvadhi/Downloads/Book.xlsx");

		return "Update Successful";
	}

	private void populateResponseData() {
		
		responseData.put("prema", Arrays.asList("100","200","300"));
		responseData.put("mouni", Arrays.asList("200","200","300"));

		responseData.put("devi", Arrays.asList("300","200","300"));

		responseData.put("lakshmi", Arrays.asList("400","200","300"));

		
	}

	private void readExcelFile(String excelPath) {

		try {
			File f = new File(excelPath);

			if (!f.exists()) {
				throw new Exception("File not exists");
			}

			 fis = new FileInputStream(f);
			 this.excelFilePath = excelPath;
			 wb = WorkbookFactory.create(fis);
			 sh = wb.getSheetAt(0);
			if (sh == null) {
				throw new Exception("Data not exists in the given excel");
			}


			// adding all the column header names to the map 'columns'
			sh.getRow(0).forEach(cell -> {
				columns.put(cell.getStringCellValue(), cell.getColumnIndex());
			});
			
			for(int i=1; i<=sh.getPhysicalNumberOfRows();i++) {
				
				String colData = getCellData(sh, "UserName", i);
				String text = StringUtils.join(responseData.getOrDefault(colData, Arrays.asList("NA")),',');
				updateCellData(sh,text,"Marks",i);
				
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
			return "";
		}
	}

	public String getCellData(Sheet sh,String columnName, int rownum) throws Exception {
		return getCellData(sh,rownum, columns.get(columnName));
	}
	
	
	public void updateCellData(Sheet sh,String text,String columnName, int rownum) throws Exception {
		 updateCellData(sh,text,rownum, columns.get(columnName));
	}
	
	
	public void updateCellData(Sheet sh,String text, int rownum, int colnum) throws Exception {
        try{
            Row row  = sh.getRow(rownum);
            if(row ==null)
            {
                row = sh.createRow(rownum);
            }
            Cell cell = row.getCell(colnum);

            if (cell == null) {
                cell = row.createCell(colnum);
            }
            cell.setCellValue(text);

            FileOutputStream fileOut = new FileOutputStream(excelFilePath);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        }catch(Exception e){
            throw (e);
        }
    }

}
