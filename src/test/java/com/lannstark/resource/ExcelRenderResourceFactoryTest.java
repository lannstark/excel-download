package com.lannstark.resource;

import com.lannstark.dto.ExcelDto;
import com.lannstark.dto.ExcelWithDropdownDto;
import com.lannstark.excel.ExcelFile;
import com.lannstark.excel.onesheet.OneSheetExcelFile;
import com.lannstark.type.Job;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ExcelRenderResourceFactoryTest {

    @Test
    public void excelRenderResourceCreationTest() {
        // given & when
        ExcelRenderResource resource
                = ExcelRenderResourceFactory.prepareRenderResource(ExcelDto.class, new SXSSFWorkbook(), new DefaultDataFormatDecider());

        // then
        assertThat(resource.getDataFieldNames()).isEqualTo(Arrays.asList("name", "age"));

        assertCenterThinCellStyle(resource.getCellStyle("name", ExcelRenderLocation.HEADER), (byte) 223, (byte) 235, (byte) 246);
        assertCenterThinCellStyle(resource.getCellStyle("age", ExcelRenderLocation.HEADER), (byte) 0, (byte) 0, (byte) 0);
    }

    @Test
    void createExcelWithDropdownTest() throws IOException {
        List<ExcelWithDropdownDto> data = new ArrayList<>();
        data.add(new ExcelWithDropdownDto("Alexander", 28, Job.BACKEND_DEVELOPER));
        data.add(new ExcelWithDropdownDto("Foo", 20, Job.DATA_SCIENTIST));
        data.add(new ExcelWithDropdownDto("Bar", 30, Job.PM));

        final ExcelFile<ExcelWithDropdownDto> excelFile = new OneSheetExcelFile<>(data, ExcelWithDropdownDto.class);

        final File file = new File("./test.xlsx");
        final FileOutputStream fileOutputStream = new FileOutputStream(file);
        excelFile.write(fileOutputStream);
        fileOutputStream.close();;
    }


    private void assertCenterThinCellStyle(CellStyle cellStyle,
                                 byte red, byte green, byte blue) {
        assertThat(cellStyle.getAlignment()).isEqualTo(HorizontalAlignment.CENTER);
        assertThat(cellStyle.getVerticalAlignment()).isEqualTo(VerticalAlignment.CENTER);
        assertThat(cellStyle.getBorderTop()).isEqualTo(BorderStyle.THIN);
        assertThat(cellStyle.getBorderRight()).isEqualTo(BorderStyle.THIN);
        assertThat(cellStyle.getBorderLeft()).isEqualTo(BorderStyle.THIN);
        assertThat(cellStyle.getBorderBottom()).isEqualTo(BorderStyle.THIN);
        XSSFColor nameHeaderCellColor = (XSSFColor) cellStyle.getFillForegroundColorColor();
        assertThat(nameHeaderCellColor.getRGB()).isEqualTo(new Byte[]{red, green, blue});
    }

}
