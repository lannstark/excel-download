package com.lannstark.resource;

import com.lannstark.DefaultContentsStyle;
import com.lannstark.DefaultHeaderStyle;
import com.lannstark.ExcelColumn;
import com.lannstark.ExcelColumnStyle;
import com.lannstark.exception.InvalidExcelCellStyleException;
import com.lannstark.exception.NoExcelColumnAnnotationsException;
import com.lannstark.resource.collection.PreCalculatedCellStyleMap;
import com.lannstark.style.ExcelCellStyle;
import com.lannstark.style.NoExcelCellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.lannstark.utils.SuperClassReflectionUtils.getAllFields;
import static com.lannstark.utils.SuperClassReflectionUtils.getAnnotation;

/**
 * ExcelRenderResourceFactory
 *
 */
public final class ExcelRenderResourceFactory {

	public static ExcelRenderResource prepareRenderResource(Class<?> type, Workbook wb,
															DataFormatDecider dataFormatDecider) {
		PreCalculatedCellStyleMap styleMap = new PreCalculatedCellStyleMap(dataFormatDecider);
		Map<String, String> headerNamesMap = new LinkedHashMap<>();
		List<String> fieldNames = new ArrayList<>();

		ExcelColumnStyle classDefinedHeaderStyle = getHeaderExcelColumnStyle(type);
		ExcelColumnStyle classDefinedContentsStyle = getContentsExcelColumnStyle(type);

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
						ExcelCellKey.of(field.getName(), ExcelRenderLocation.CONTENTS),
						getCellStyle(decideAppliedStyleAnnotation(classDefinedContentsStyle, annotation.contentsStyle())), wb);
				fieldNames.add(field.getName());
				headerNamesMap.put(field.getName(), annotation.headerName());
			}
		}

		if (styleMap.isEmpty()) {
			throw new NoExcelColumnAnnotationsException(String.format("Class %s has not @ExcelColumn at all", type));
		}
		return new ExcelRenderResource(styleMap, headerNamesMap, fieldNames);
	}

	private static ExcelColumnStyle getHeaderExcelColumnStyle(Class<?> clazz) {
		Annotation annotation = getAnnotation(clazz, DefaultHeaderStyle.class);
		if (annotation == null) {
			return null;
		}
		return ((DefaultHeaderStyle) annotation).style();
	}

	private static ExcelColumnStyle getContentsExcelColumnStyle(Class<?> clazz) {
		Annotation annotation = getAnnotation(clazz, DefaultContentsStyle.class);
		if (annotation == null) {
			return null;
		}
		return ((DefaultContentsStyle) annotation).style();
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
	private static ExcelCellStyle findExcelCellStyle(Class excelCellStyles, String enumName) {
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
