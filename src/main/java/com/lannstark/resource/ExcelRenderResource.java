package com.lannstark.resource;

import com.lannstark.resource.collection.PreCalculatedCellStyleMap;
import com.lannstark.utils.DropdownOption;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;
import java.util.Map;

public class ExcelRenderResource {

	private PreCalculatedCellStyleMap styleMap;

	// TODO dataFieldName -> excelHeaderName Map Abstraction
	private Map<String, String> excelHeaderNames;
	private List<String> dataFieldNames;

	private Map<Integer, DropdownOption> dropdownMap;

	public ExcelRenderResource(
			final PreCalculatedCellStyleMap styleMap,
			final Map<String, String> excelHeaderNames,
			final List<String> dataFieldNames,
			final Map<Integer, DropdownOption> dropdownMap
	) {
		this.styleMap = styleMap;
		this.excelHeaderNames = excelHeaderNames;
		this.dataFieldNames = dataFieldNames;
		this.dropdownMap = dropdownMap;
	}

	public CellStyle getCellStyle(final String dataFieldName, final ExcelRenderLocation excelRenderLocation) {
		return styleMap.get(ExcelCellKey.of(dataFieldName, excelRenderLocation));
	}

	public String getExcelHeaderName(final String dataFieldName) {
		return excelHeaderNames.get(dataFieldName);
	}

	public List<String> getDataFieldNames() {
		return dataFieldNames;
	}

	public Map<Integer, DropdownOption> getDropdownMap() {
		return this.dropdownMap;
	}

}
