package com.lannstark;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.poi.ss.usermodel.DataValidation.ErrorStyle;

@Repeatable(value = CellDropdowns.class)
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CellDropdown {

	/**
	 * column index
	 * start from 0
	 */
	int colIndex();


	// Enum of Dropdown list
	Class<? extends Enum> type();

	// get Method for Enum Value
	String qualifiedByName() default "name";

	/**
	 * @see {@link org.apache.poi.ss.usermodel.DataValidation} setEmptyCellAllowed
	 */
	boolean emptyCellAllowed() default false;

	/**
	 * @see {@link org.apache.poi.ss.usermodel.DataValidation} setShowPromptBox
	 */
	boolean showPromptBox() default true;

	/**
	 * @see {@link org.apache.poi.ss.usermodel.DataValidation} setShowErrorBox
	 */
	boolean showErrorBox() default true;

	/**
	 * @see {@link org.apache.poi.ss.usermodel.DataValidation} setErrorStyle
	 */
	int errorStyle() default ErrorStyle.WARNING;

	String errorMessage() default "Invalid Value!";
}
