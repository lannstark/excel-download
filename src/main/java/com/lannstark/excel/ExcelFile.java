package com.lannstark.excel;

import java.io.IOException;
import java.io.OutputStream;

public interface ExcelFile {

	void write(OutputStream stream) throws IOException;

}
