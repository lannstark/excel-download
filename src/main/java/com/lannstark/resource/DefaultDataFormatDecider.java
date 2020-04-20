package com.lannstark.resource;

import org.apache.poi.ss.usermodel.DataFormat;

import java.util.Arrays;
import java.util.List;

public class DefaultDataFormatDecider implements DataFormatDecider {

	private static final String CURRENT_FORMAT = "#,##0";
	private static final String DEFAULT_FORMAT = "";

	@Override
	public short getDataFormat(DataFormat dataFormat, Class<?> type) {
		if (isNumericType(type)) {
			return dataFormat.getFormat(CURRENT_FORMAT);
		}
		return dataFormat.getFormat(DEFAULT_FORMAT);
	}

	private boolean isNumericType(Class<?> type) {
		List<Class<?>> numericTypes = Arrays.asList(
				Byte.class, byte.class,
				Short.class, short.class,
				Integer.class, int.class,
				Long.class, long.class,
				Float.class, float.class,
				Double.class, double.class
		);
		return numericTypes.contains(type);
	}

}
