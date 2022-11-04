package com.lannstark.resource;

import com.lannstark.CellDropdown;
import com.lannstark.DefaultBodyStyle;
import com.lannstark.DefaultHeaderStyle;
import com.lannstark.ExcelColumn;
import com.lannstark.ExcelColumnStyle;
import com.lannstark.exception.InvalidExcelCellStyleException;
import com.lannstark.exception.NoExcelColumnAnnotationsException;
import com.lannstark.resource.collection.PreCalculatedCellStyleMap;
import com.lannstark.style.ExcelCellStyle;
import com.lannstark.style.NoExcelCellStyle;
import com.lannstark.utils.DropdownOption;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.lannstark.utils.SuperClassReflectionUtils.getAllFields;
import static com.lannstark.utils.SuperClassReflectionUtils.getAnnotation;

/**
 * ExcelRenderResourceFactory
 *
 */
public final class ExcelRenderResourceFactory {

	static final Logger logger = LoggerFactory.getLogger(ExcelRenderResourceFactory.class);
	public static ExcelRenderResource prepareRenderResource(Class<?> type, Workbook wb,
															DataFormatDecider dataFormatDecider) {
		PreCalculatedCellStyleMap styleMap = new PreCalculatedCellStyleMap(dataFormatDecider);
		Map<String, String> headerNamesMap = new LinkedHashMap<>();
		List<String> fieldNames = new ArrayList<>();
		Map<Integer, DropdownOption> dropdownMap = new HashMap<>();

		final CellDropdown[] cellDropdowns = type.getAnnotationsByType(CellDropdown.class);
		for (CellDropdown cellDropdown : cellDropdowns) {

			final int columnIndex = cellDropdown.colIndex();
			final Class<? extends Enum> dropdownEnum = cellDropdown.type();
			final String qualifiedByName = cellDropdown.qualifiedByName();
			final boolean emptyCellAllowed = cellDropdown.emptyCellAllowed();
			final boolean showErrorBox = cellDropdown.showErrorBox();
			final boolean showPromptBox = cellDropdown.showPromptBox();
			final int errorStyle = cellDropdown.errorStyle();
			final String errorMessage = cellDropdown.errorMessage();

			final List<String> options = new LinkedList<>();

			if (dropdownEnum.isEnum()) {
				final Enum[] enumConstants = dropdownEnum.getEnumConstants();

				for (Enum enumConstant : enumConstants) {
					try {
						final Method method = dropdownEnum.getMethod(qualifiedByName);
						final String name = (String) method.invoke(enumConstant);
						options.add(name);

					} catch (Exception e) {
						logger.warn(String.format("Does not exists Method [%s] for Dropdown. Skip %s Enum", qualifiedByName ,dropdownEnum.getName()));
					}
				}
			}

			final DropdownOption dropdownOption = new DropdownOption(options, emptyCellAllowed, showPromptBox, showErrorBox, errorStyle, errorMessage);
			dropdownMap.put(columnIndex, dropdownOption);
		}

		ExcelColumnStyle classDefinedHeaderStyle = getHeaderExcelColumnStyle(type);
		ExcelColumnStyle classDefinedBodyStyle = getBodyExcelColumnStyle(type);

		for (Field field : getAllFields(type)) {
			if (field.isAnnotationPresent(ExcelColumn.class)) {
				ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
				styleMap.put(
						String.class,
						ExcelCellKey.of(field.getName(), ExcelRenderLocation.HEADER),
						getCellStyle(decideAppliedStyleAnnotation(classDefinedHeaderStyle, annotation.headerStyle())), wb);
				Class<?> fieldType = field.getType();
				styleMap.put(
						fieldType,
						ExcelCellKey.of(field.getName(), ExcelRenderLocation.BODY),
						getCellStyle(decideAppliedStyleAnnotation(classDefinedBodyStyle, annotation.bodyStyle())), wb);
				fieldNames.add(field.getName());
				headerNamesMap.put(field.getName(), annotation.headerName());
			}
		}

		if (styleMap.isEmpty()) {
			throw new NoExcelColumnAnnotationsException(String.format("Class %s has not @ExcelColumn at all", type));
		}
		return new ExcelRenderResource(styleMap, headerNamesMap, fieldNames, dropdownMap);
	}

	private static ExcelColumnStyle getHeaderExcelColumnStyle(Class<?> clazz) {
		Annotation annotation = getAnnotation(clazz, DefaultHeaderStyle.class);
		if (annotation == null) {
			return null;
		}
		return ((DefaultHeaderStyle) annotation).style();
	}

	private static ExcelColumnStyle getBodyExcelColumnStyle(Class<?> clazz) {
		Annotation annotation = getAnnotation(clazz, DefaultBodyStyle.class);
		if (annotation == null) {
			return null;
		}
		return ((DefaultBodyStyle) annotation).style();
	}

	private static ExcelColumnStyle decideAppliedStyleAnnotation(ExcelColumnStyle classAnnotation,
																 ExcelColumnStyle fieldAnnotation) {
		if (fieldAnnotation.excelCellStyleClass().equals(NoExcelCellStyle.class) && classAnnotation != null) {
			return classAnnotation;
		}
		return fieldAnnotation;
	}

	private static ExcelCellStyle getCellStyle(ExcelColumnStyle excelColumnStyle) {
		Class<? extends ExcelCellStyle> excelCellStyleClass = excelColumnStyle.excelCellStyleClass();
		// 1. Case of Enum
		if (excelCellStyleClass.isEnum()) {
			String enumName = excelColumnStyle.enumName();
			return findExcelCellStyle(excelCellStyleClass, enumName);
		}

		// 2. Case of Class
		try {
			return excelCellStyleClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new InvalidExcelCellStyleException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	private static ExcelCellStyle findExcelCellStyle(Class<?> excelCellStyles, String enumName) {
		try {
			return (ExcelCellStyle) Enum.valueOf((Class<Enum>) excelCellStyles, enumName);
		} catch (NullPointerException e) {
			throw new InvalidExcelCellStyleException("enumName must not be null", e);
		} catch (IllegalArgumentException e) {
			throw new InvalidExcelCellStyleException(
					String.format("Enum %s does not name %s", excelCellStyles.getName(), enumName), e);
		}
	}

}
