package com.lannstark.resource;

import com.lannstark.dto.ExcelDto;
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
