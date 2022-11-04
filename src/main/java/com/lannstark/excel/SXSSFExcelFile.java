package com.lannstark.excel;

import com.lannstark.exception.ExcelInternalException;
import com.lannstark.resource.DataFormatDecider;
import com.lannstark.resource.DefaultDataFormatDecider;
import com.lannstark.resource.ExcelRenderLocation;
import com.lannstark.resource.ExcelRenderResource;
import com.lannstark.resource.ExcelRenderResourceFactory;
import com.lannstark.utils.DropdownOption;
import java.util.Map;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static com.lannstark.utils.SuperClassReflectionUtils.getField;

public abstract class SXSSFExcelFile<T> implements ExcelFile<T> {

	protected static final SpreadsheetVersion supplyExcelVersion = SpreadsheetVersion.EXCEL2007;

	protected SXSSFWorkbook wb;
	protected Sheet sheet;
	protected ExcelRenderResource resource;

	/**
	 *SXSSFExcelFile
	 * @param type Class type to be rendered
	 */
	public SXSSFExcelFile(Class<T> type) {
		this(Collections.emptyList(), type, new DefaultDataFormatDecider());
	}

	/**
	 * SXSSFExcelFile
	 * @param data List Data to render excel file. data should have at least one @ExcelColumn on fields
	 * @param type Class type to be rendered
	 */
	public SXSSFExcelFile(List<T> data, Class<T> type) {
		this(data, type, new DefaultDataFormatDecider());
	}

	/**
	 * SXSSFExcelFile
	 * @param data List Data to render excel file. data should have at least one @ExcelColumn on fields
	 * @param type Class type to be rendered
	 * @param dataFormatDecider Custom DataFormatDecider
	 */
	public SXSSFExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider) {
		validateData(data);
		this.wb = new SXSSFWorkbook();
		this.resource = ExcelRenderResourceFactory.prepareRenderResource(type, wb, dataFormatDecider);
		renderExcel(data);
	}

	protected void validateData(List<T> data) { }

	protected abstract void renderExcel(List<T> data);

	/**
	 *
	 * @param rowIndex startRow
	 * @param dataSize total data size
	 */
	protected void generateDropdown(final int rowIndex, final int dataSize) {
		final Map<Integer, DropdownOption> dropdownMap = resource.getDropdownMap();
		for (Integer colIndex : dropdownMap.keySet()) {
			final DropdownOption dropdownOption = dropdownMap.get(colIndex);
			final List<String> options = dropdownOption.getOptionNames();

			final CellRangeAddressList cellRangeAddressList = new CellRangeAddressList();
			cellRangeAddressList.addCellRangeAddress(rowIndex, colIndex, rowIndex + dataSize, colIndex);

			final DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
			final DataValidationConstraint explicitListConstraint =
					dataValidationHelper.createExplicitListConstraint(options.toArray(new String[options.size()]));
			final DataValidation validation = dataValidationHelper.createValidation(explicitListConstraint, cellRangeAddressList);
			validation.setEmptyCellAllowed(dropdownOption.isEmptyCellAllowed());
			validation.setShowErrorBox(dropdownOption.isShowErrorBox());
			validation.setShowPromptBox(dropdownOption.isShowPromptBox());
			validation.setErrorStyle(dropdownOption.getErrorStyle());
			validation.createErrorBox("Error!", dropdownOption.getErrorMessage());
			sheet.addValidationData(validation);
		}
	}

	protected void renderHeadersWithNewSheet(Sheet sheet, int rowIndex, int columnStartIndex) {
		Row row = sheet.createRow(rowIndex);
		int columnIndex = columnStartIndex;
		for (String dataFieldName : resource.getDataFieldNames()) {
			Cell cell = row.createCell(columnIndex++);
			cell.setCellStyle(resource.getCellStyle(dataFieldName, ExcelRenderLocation.HEADER));
			cell.setCellValue(resource.getExcelHeaderName(dataFieldName));
		}
	}

	protected void renderBody(Object data, int rowIndex, int columnStartIndex) {
		Row row = sheet.createRow(rowIndex);
		int columnIndex = columnStartIndex;
		for (String dataFieldName : resource.getDataFieldNames()) {
			Cell cell = row.createCell(columnIndex++);
			try {
				Field field = getField(data.getClass(), (dataFieldName));
				field.setAccessible(true);
				cell.setCellStyle(resource.getCellStyle(dataFieldName, ExcelRenderLocation.BODY));
				Object cellValue = field.get(data);
				renderCellValue(cell, cellValue);
			} catch (Exception e) {
				throw new ExcelInternalException(e.getMessage(), e);
			}
		}
	}

	private void renderCellValue(Cell cell, Object cellValue) {
		if (cellValue instanceof Number) {
			Number numberValue = (Number) cellValue;
			cell.setCellValue(numberValue.doubleValue());
			return;
		}
		cell.setCellValue(cellValue == null ? "" : cellValue.toString());
	}

	public void write(OutputStream stream) throws IOException {
		wb.write(stream);
		wb.close();
		wb.dispose();
		stream.close();
	}

}
