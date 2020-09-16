package com.lannstark.resource;

import org.apache.poi.ss.usermodel.DataFormat;

import java.util.Arrays;
import java.util.List;

public class DefaultDataFormatDecider implements DataFormatDecider {

	private static final String CURRENT_FORMAT = "#,##0";
	private static final String FLOAT_FORMAT_2_DECIMAL_PLACES = "#,##0.00";
	private static final String DEFAULT_FORMAT = "";

	@Override
	public short getDataFormat(DataFormat dataFormat, Class<?> type) {
		if (isFloatType(type)) {
			return dataFormat.getFormat(FLOAT_FORMAT_2_DECIMAL_PLACES);
		}

		if (isIntegerType(type)) {
			return dataFormat.getFormat(CURRENT_FORMAT);
		}
		return dataFormat.getFormat(DEFAULT_FORMAT);
	}

	private boolean isFloatType(Class<?> type) {
		List<Class<?>> floatTypes = Arrays.asList(
				Float.class, float.class,
				Double.class, double.class
		);
		return floatTypes.contains(type);
	}

	private boolean isIntegerType(Class<?> type) {
		List<Class<?>> integerTypes = Arrays.asList(
				Byte.class, byte.class,
				Short.class, short.class,
				Integer.class, int.class,
				Long.class, long.class
		);
		return integerTypes.contains(type);
	}

}
