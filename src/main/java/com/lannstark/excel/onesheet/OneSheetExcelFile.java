package com.lannstark.excel.onesheet;

import com.lannstark.excel.SXSSFExcelFile;
import com.lannstark.resource.DataFormatDecider;

import java.util.List;

/**
 * OneSheetExcelFile
 *
 * - support Excel Version over 2007
 * - support one sheet rendering
 * - support Dffierent DataFormat by Class Type
 * - support Custom CellStyle according to (header or contents) and data field
 */
public final class OneSheetExcelFile<T> extends SXSSFExcelFile<T> {

	private static final int ROW_START_INDEX = 0;
	private static final int COLUMN_START_INDEX = 0;

	public OneSheetExcelFile(List<T> data, Class<T> type) {
		super(data, type);
		validateMaxRow(data);
	}

	public OneSheetExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider) {
		super(data, type, dataFormatDecider);
		validateMaxRow(data);
	}

	private void validateMaxRow(List<?> data) {
		int maxRows = supplyExcelVersion.getMaxRows();
		if (data.size() > maxRows) {
			throw new IllegalArgumentException(
					String.format("This concrete ExcelFile does not support over %s rows", maxRows));
		}
	}

	@Override
	public void renderExcel(List<T> data) {
		// 1. Create sheet and renderHeader
		sheet = wb.createSheet();
		renderHeadersWithNewSheet(sheet, ROW_START_INDEX, COLUMN_START_INDEX);

		if (data.isEmpty()) {
			return;
		}

		// 2. Render Contents
		int rowIndex = ROW_START_INDEX + 1;
		for (Object renderedData : data) {
			renderContent(renderedData, rowIndex++, COLUMN_START_INDEX);
		}
	}

}
