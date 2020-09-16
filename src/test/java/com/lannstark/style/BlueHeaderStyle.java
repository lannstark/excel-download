package com.lannstark.style;

import com.lannstark.style.align.DefaultExcelAlign;
import com.lannstark.style.border.DefaultExcelBorders;
import com.lannstark.style.border.ExcelBorderStyle;
import com.lannstark.style.configurer.ExcelCellStyleConfigurer;

public class BlueHeaderStyle extends CustomExcelCellStyle {

    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.foregroundColor(223, 235, 246)
                .excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER);
    }

}