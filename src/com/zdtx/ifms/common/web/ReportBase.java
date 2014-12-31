package com.zdtx.ifms.common.web;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 * For report
 *
 * @author Wiflg Goth
 * @since Mar 17, 2011
 */
@Results({
	@Result(name = "xls", type = "stream", params = { "contentType", "application/vnd.ms-excel", "inputName", "xlsStream",
			"contentDisposition", "attachment;filename=${xlsFileName}" }),
	@Result(name = "xlsx", type = "stream", params = { "contentType", "application/vnd.ms-excel", "inputName", "xlsStream",
			"contentDisposition", "attachment;filename=${xlsFileName}" })
})
public abstract class ReportBase<T> extends URLSupport<T> {

	private static final long serialVersionUID = 8647818504147408731L;

	// total
	protected Object[] total;
	// File name of Export excel
	protected String xlsFileName;
	protected InputStream xlsStream;

	private boolean sum;

	protected String disposeXlsName(String xlsName) {

		String disposedName = "report";
		try {
			disposedName = new String(xlsName.getBytes("GB2312"), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return disposedName + ".xls";
	}

	public Object[] getTotal() {
		return total;
	}

	public InputStream getXlsStream() {
		return xlsStream;
	}

	public String getXlsFileName() {
		return xlsFileName;
	}

	public boolean isSum() {
		return sum;
	}

	public void setSum(boolean sum) {
		this.sum = sum;
	}
}