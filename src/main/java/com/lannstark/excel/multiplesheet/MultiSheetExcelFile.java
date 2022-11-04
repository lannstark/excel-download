package com.lannstark.excel.multiplesheet;

import com.lannstark.excel.SXSSFExcelFile;
import com.lannstark.resource.DataFormatDecider;
import org.apache.commons.compress.archivers.zip.Zip64Mode;

import java.util.List;

/**
 * MultiSheetExcelFile
 *
 * - support Excel Version over 2007
 * - support multi sheet rendering
 * - support Dffierent DataFormat by Class Type
 * - support Custom CellStyle according to (header or body) and data field
 */
public class MultiSheetExcelFile<T> extends SXSSFExcelFile<T> {

	private static final int maxRowCanBeRendered = supplyExcelVersion.getMaxRows() - 1;
	private static final int ROW_START_INDEX = 0;
	private static final int COLUMN_START_INDEX = 0;
	private int currentRowIndex = ROW_START_INDEX;

	public MultiSheetExcelFile(Class<T> type) {
		super(type);
		wb.setZip64Mode(Zip64Mode.Always);
	}

	/*
	 * If you use SXSSF with hug data, you need to set zip mode
	 * see http://apache-poi.1045710.n5.nabble.com/Bug-62872-New-Writing-large-files-with-800k-rows-gives-java-io-IOException-This-archive-contains-unc-td5732006.html
	 */
	public MultiSheetExcelFile(List<T> data, Class<T> type) {
		super(data, type);
		wb.setZip64Mode(Zip64Mode.Always);
	}

	public MultiSheetExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider) {
		super(data, type, dataFormatDecider);
		wb.setZip64Mode(Zip64Mode.Always);
	}

	@Override
	protected void renderExcel(List<T> data) {
		// 1. Create header and return if data is empty
		if (data.isEmpty()) {
			createNewSheetWithHeader();
			return ;
		}

		// 2. generate Dropdown (be called Data Validation in Excel)
		generateDropdown(ROW_START_INDEX, data.size());

		// 3. Render body
		createNewSheetWithHeader();
		addRows(data);
	}

	@Override
	public void addRows(List<T> data) {
		for (Object renderedData : data) {
			renderBody(renderedData, currentRowIndex++, COLUMN_START_INDEX);
			if (currentRowIndex == maxRowCanBeRendered) {
				currentRowIndex = 1;
				createNewSheetWithHeader();
			}
		}
	}

	private void createNewSheetWithHeader() {
		sheet = wb.createSheet();
		renderHeadersWithNewSheet(sheet, ROW_START_INDEX, COLUMN_START_INDEX);
		currentRowIndex++;
	}

}
