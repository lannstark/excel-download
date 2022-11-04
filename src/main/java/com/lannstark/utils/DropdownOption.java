package com.lannstark.utils;

import java.util.List;

public class DropdownOption {

	private List<String> options;

	private boolean emptyCellAllowed;

	private boolean showPromptBox;

	private boolean showErrorBox;

	private int errorStyle;

	private String errorMessage;

	public DropdownOption(
			final List<String> options,
			final boolean emptyCellAllowed,
			final boolean showPromptBox,
			final boolean showErrorBox,
			final int errorStyle,
			final String errorMessage)
	{
		this.options = options;
		this.emptyCellAllowed = emptyCellAllowed;
		this.showPromptBox = showPromptBox;
		this.showErrorBox = showErrorBox;
		this.errorStyle = errorStyle;
		this.errorMessage = errorMessage;
	}

	public List<String> getOptionNames() {
		return options;
	}

	public boolean isEmptyCellAllowed() {
		return emptyCellAllowed;
	}

	public boolean isShowPromptBox() {
		return showPromptBox;
	}

	public boolean isShowErrorBox() {
		return showErrorBox;
	}

	public int getErrorStyle() {
		return errorStyle;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
