package com.lannstark.style.border;

import org.apache.poi.ss.usermodel.CellStyle;

public final class NoExcelBorders implements ExcelBorders {

	@Override
	public void apply(CellStyle cellStyle) {
		// Do nothing
	}

}
