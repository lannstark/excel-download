package com.lannstark.dto;

import com.lannstark.DefaultHeaderStyle;
import com.lannstark.ExcelColumn;
import com.lannstark.ExcelColumnStyle;
import com.lannstark.style.BlueHeaderStyle;
import com.lannstark.style.BlackHeaderStyle;

@DefaultHeaderStyle(style = @ExcelColumnStyle(excelCellStyleClass = BlueHeaderStyle.class))
public class ExcelDto {

    @ExcelColumn(headerName = "name")
    private String name;

    private String hideColumn;

    @ExcelColumn(headerName = "age",
            headerStyle = @ExcelColumnStyle(excelCellStyleClass = BlackHeaderStyle.class))
    private int age;

}
