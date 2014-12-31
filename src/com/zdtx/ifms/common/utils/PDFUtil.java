package com.zdtx.ifms.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @ClassName: PrintPDF
 * @Description: 打印数据抽象类
 * @author Leon Liu
 * @date 2012-5-25 下午04:05:13
 * @version V1.0
 */

public abstract class PDFUtil {

	/**
	 * 执行打印
	 */
	public final void doPrint(String title, Boolean hasSeq, List<?> pdfData, Integer[] colWidth, Integer pageSize, String paper) {
		try {
			String tempName = this.generatePDFContent(title, hasSeq, pdfData, colWidth, pageSize, paper);	//生成临时样式
			String pdfName = this.generatePDF(tempName);	//生成临时文件
			this.downloadFile(pdfName);						//下载临时文件流到浏览器
			this.deleteFile(tempName);						//删除临时样式文件
			this.deleteFile(pdfName);						//删除临时pdf文件
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 画出PDF样式抽象方法
	 * @param title 标题
	 * @param hasSeq 是否有序列
	 * @param pdfData 数据集
	 * @param colWidth 列宽百分比, 例：{20, 30, 40, 10} (和必须等于100, 无需考虑序列宽度)
	 * @param pageSize 每页显示条数
	 * @param paper 纸张类型
	 * @return PDF样式路径（包含文件名）
	 */
	protected abstract String generatePDFContent(String title, Boolean hasSeq, List<?> pdfData, Integer[] colWidth, Integer pageSize, String paper);

	/**
	 * 生成PDF文件
	 * @param tempName	模板名
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerException
	 * @return pdfDir 生成的pdf路径（包含文件名）
	 */
	private final String generatePDF(String tempName) {
		File source = new File(Constants.DATA_SOURCE_DIR_PATH);	//数据资源文件，这里暂时不用
		File specs = new File(tempName);
		String pdfPath = Constants.PDF_DESTINATION_DIR_PATH;	//pdf保存路径
		File path = new File(pdfPath);
		if(!path.exists()) {		//准备路径
			if(!path.mkdir()) {
				Struts2Util.renderText("{\"result\" : \"dirError\"}");
				return null;
			}
		}
		String pdfDir = pdfPath + "/print" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".pdf";	//临时文件名
		File target = new File(pdfDir);
		FopFactory fopFactory = FopFactory.newInstance();
		OutputStream out = null;
		try {
			fopFactory.setUserConfig(Constants.CONF_SOURCE_DIR_PATH);	// 读取自定义配置
			FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
//			foUserAgent.setCreationDate(new Date(1949, 4, 17, 10, 11, 12));
//			foUserAgent.setCreator("Liujun111");
			out = new FileOutputStream(target);
			out = new java.io.BufferedOutputStream(out);
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);	//MimeConstants.MIME_PDF = "application/pdf"
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(specs));
			Source src = new StreamSource(source);
			javax.xml.transform.Result res = null;
			//SAXResult基于事件驱动的xml处理方式
			res = new SAXResult(fop.getDefaultHandler());
			transformer.transform(src, res);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != out) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pdfDir;
	}

	/**
	 * 删除文件
	 *
	 * @param pdfDir 文件地址及文件名
	 */
	public final void deleteFile(String fileDir) {
		new File(fileDir).delete();
	}

	/**
	 * 数组排序(冒泡)
	 *
	 * @param strArr原数组
	 * @return String[] 新数组
	 */
	public final String[] getSortedColumns(String[] strArr) {
		String obj = "";
		int len = strArr.length;
		for (int i = 1; i < len; i++) {
			for (int j = 0; j < len - i; j++) {
				Long num_j = Long.valueOf(strArr[j].split("-")[1].trim());
				Long num_k = Long.valueOf(strArr[j + 1].split("-")[1].trim());
				if(num_j > num_k) {
					obj = strArr[j];
					strArr[j] = strArr[j + 1];
					strArr[j + 1] = obj;
				}
			}
		}
		return strArr;
	}

	/**
	 * 新建table表头单元格，并输入对应数据
	 * @param 处理类
	 * @param 父元素
	 * @param cellValue 单元格数据
	 */
	protected Element createTableHeaderCell(XMLUtil resolver, Element fatherElement, String cellValue) {
		Element headerCell = resolver.getDocument().createElement("fo:table-cell");
		headerCell.setAttribute("border-style", "solid");
		Element cellVal = resolver.getDocument().createElement("fo:block");
		cellVal.setAttribute("font-family", "SimSun");
		cellVal.setAttribute("font-weight", "bold");
		cellVal.setAttribute("font-size", "12pt");
		cellVal.setAttribute("text-align", "center");
		cellVal.setTextContent(cellValue);
		fatherElement.appendChild(headerCell);
		headerCell.appendChild(cellVal);
		return cellVal;
	}

	/**
	 * 新建table表体单元格，并输入对应数据
	 * @param 处理类
	 * @param 父元素
	 * @param cellValue 单元格数据
	 */
	protected Element createTableBodyCell(XMLUtil resolver, Element fatherElement, String cellValue) {
		Element bodyCell = resolver.getDocument().createElement("fo:table-cell");
		bodyCell.setAttribute("border-style", "solid");
		Element cellVal = resolver.getDocument().createElement("fo:block");
		cellVal.setAttribute("font-family", "SimSun");
		cellVal.setAttribute("font-size", "9pt");
		cellVal.setAttribute("text-align", "center");
		cellVal.setTextContent(cellValue);
		fatherElement.appendChild(bodyCell);
		bodyCell.appendChild(cellVal);
		return cellVal;
	}

	/**
	 * 下载文件到本地
	 * @throws IOException
	 */
	private void downloadFile(String sourceName) throws IOException {
		if (sourceName != null) {
            byte[] buffer = new byte[4 * 1024];
            BufferedInputStream bInput = null;
            BufferedOutputStream bOut = null;
			try {
				bInput = new BufferedInputStream(new FileInputStream(sourceName));
				bOut = new BufferedOutputStream(Struts2Util.getOutputStream(StringUtils.substringAfterLast(sourceName, "/")));
				while (bInput.read(buffer) != -1) {	//输出文件流
					bOut.write(buffer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				bOut.flush();
				bInput.close();
				bOut.close();
				Struts2Util.getResponse().flushBuffer();	//强制刷新response，输出完整文件
			}
        }
	}

	/**
	 * 选择纸张规格
	 * @param paperName 1:A3纵向、2:A3横向、3:A4横向、4:A4横向、5:A5横向、6:A5横向、7:241-1、8:381-1
	 */
	protected void changePaper(Document doc, String paperSpec) {
		Node node = doc.getElementsByTagName("fo:page-sequence").item(0);
		Element ele = (Element)node;
		String paperName = "";
		switch (Integer.valueOf(paperSpec)) {
		case 10:
			paperName = "A3-portrait";
			break;
		case 11:
			paperName = "A3-landscape";
			break;
		case 0:
			paperName = "A4-portrait";
			break;
		case 1:
			paperName = "A4-landscape";
			break;
		case 20:
			paperName = "A5-portrait";
			break;
		case 21:
			paperName = "A5-landscape";
			break;
		case 30:
			paperName = "241-1-portrait";
			break;
		case 31:
			paperName = "241-1-landscape";
			break;
		case 40:
			paperName = "381-1-portrait";
			break;
		case 41:
			paperName = "381-1-landscape";
			break;
		default:
			break;
		}
		ele.setAttribute("master-reference", paperName);
	}
}