package com.lannstark.style.align;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * DefaultExcelAlign
 *
 * Can be used with {@link com.lannstark.style.CustomExcelCellStyle}
 * see {@link org.apache.poi.ss.usermodel.VerticalAlignment} and
 * {@link org.apache.poi.ss.usermodel.HorizontalAlignment} for detail explanation
 */
public enum DefaultExcelAlign implements ExcelAlign {

	GENERAL_TOP(HorizontalAlignment.GENERAL, VerticalAlignment.TOP),
	GENERAL_CENTER(HorizontalAlignment.GENERAL, VerticalAlignment.CENTER),
	GENERAL_BOTTOM(HorizontalAlignment.GENERAL, VerticalAlignment.BOTTOM),
	GENERAL_JUSTIFY(HorizontalAlignment.GENERAL, VerticalAlignment.JUSTIFY),
	GENERAL_DISTRIBUTED(HorizontalAlignment.GENERAL, VerticalAlignment.DISTRIBUTED),
	LEFT_TOP(HorizontalAlignment.LEFT, VerticalAlignment.TOP),
	LEFT_CENTER(HorizontalAlignment.LEFT, VerticalAlignment.CENTER),
	LEFT_BOTTOM(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM),
	LEFT_JUSTIFY(HorizontalAlignment.LEFT, VerticalAlignment.JUSTIFY),
	LEFT_DISTRIBUTED(HorizontalAlignment.LEFT, VerticalAlignment.DISTRIBUTED),
	CENTER_TOP(HorizontalAlignment.CENTER, VerticalAlignment.TOP),
	CENTER_CENTER(HorizontalAlignment.CENTER, VerticalAlignment.CENTER),
	CENTER_BOTTOM(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM),
	CENTER_JUSTIFY(HorizontalAlignment.CENTER, VerticalAlignment.JUSTIFY),
	CENTER_DISTRIBUTED(HorizontalAlignment.CENTER, VerticalAlignment.DISTRIBUTED),
	RIGHT_TOP(HorizontalAlignment.RIGHT, VerticalAlignment.TOP),
	RIGHT_CENTER(HorizontalAlignment.RIGHT, VerticalAlignment.CENTER),
	RIGHT_BOTTOM(HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM),
	RIGHT_JUSTIFY(HorizontalAlignment.RIGHT, VerticalAlignment.JUSTIFY),
	RIGHT_DISTRIBUTED(HorizontalAlignment.RIGHT, VerticalAlignment.DISTRIBUTED),
	FILL_TOP(HorizontalAlignment.FILL, VerticalAlignment.TOP),
	FILL_CENTER(HorizontalAlignment.FILL, VerticalAlignment.CENTER),
	FILL_BOTTOM(HorizontalAlignment.FILL, VerticalAlignment.BOTTOM),
	FILL_JUSTIFY(HorizontalAlignment.FILL, VerticalAlignment.JUSTIFY),
	FILL_DISTRIBUTED(HorizontalAlignment.FILL, VerticalAlignment.DISTRIBUTED),
	JUSTIFY_TOP(HorizontalAlignment.JUSTIFY, VerticalAlignment.TOP),
	JUSTIFY_CENTER(HorizontalAlignment.JUSTIFY, VerticalAlignment.CENTER),
	JUSTIFY_BOTTOM(HorizontalAlignment.JUSTIFY, VerticalAlignment.BOTTOM),
	JUSTIFY_JUSTIFY(HorizontalAlignment.JUSTIFY, VerticalAlignment.JUSTIFY),
	JUSTIFY_DISTRIBUTED(HorizontalAlignment.JUSTIFY, VerticalAlignment.DISTRIBUTED),
	CENTER_SELECTION_TOP(HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.TOP),
	CENTER_SELECTION_CENTER(HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER),
	CENTER_SELECTION_BOTTOM(HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.BOTTOM),
	CENTER_SELECTION_JUSTIFY(HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.JUSTIFY),
	CENTER_SELECTION_DISTRIBUTED(HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.DISTRIBUTED),
	DISTRIBUTED_TOP(HorizontalAlignment.DISTRIBUTED, VerticalAlignment.TOP),
	DISTRIBUTED_CENTER(HorizontalAlignment.DISTRIBUTED, VerticalAlignment.CENTER),
	DISTRIBUTED_BOTTOM(HorizontalAlignment.DISTRIBUTED, VerticalAlignment.BOTTOM),
	DISTRIBUTED_JUSTIFY(HorizontalAlignment.DISTRIBUTED, VerticalAlignment.JUSTIFY),
	DISTRIBUTED_DISTRIBUTED(HorizontalAlignment.DISTRIBUTED, VerticalAlignment.DISTRIBUTED);

	private final HorizontalAlignment horizontalAlignment;
	private final VerticalAlignment verticalAlignment;

	DefaultExcelAlign(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
		this.verticalAlignment = verticalAlignment;
	}

	@Override
	public void apply(CellStyle cellStyle) {
		cellStyle.setAlignment(horizontalAlignment);
		cellStyle.setVerticalAlignment(verticalAlignment);
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

}
