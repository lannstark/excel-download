package com.lannstark.dto;

import com.lannstark.CellDropdown;
import com.lannstark.DefaultHeaderStyle;
import com.lannstark.ExcelColumn;
import com.lannstark.ExcelColumnStyle;
import com.lannstark.style.BlackHeaderStyle;
import com.lannstark.style.BlueHeaderStyle;
import com.lannstark.type.Job;

@CellDropdown(colIndex = 2, type = Job.class, qualifiedByName = "getDescription")
@DefaultHeaderStyle(style = @ExcelColumnStyle(excelCellStyleClass = BlueHeaderStyle.class))
public class ExcelWithDropdownDto {

    @ExcelColumn(headerName = "name")
    private String name;

    private String hideColumn;

    @ExcelColumn(headerName = "age",
            headerStyle = @ExcelColumnStyle(excelCellStyleClass = BlackHeaderStyle.class))
    private int age;

    @ExcelColumn(headerName = "job")
    private Job job;

    public ExcelWithDropdownDto(final String name, final int age, final Job job) {
        this.name = name;
        this.age = age;
        this.job = job;
    }
}
