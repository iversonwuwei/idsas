package com.zdtx.ifms.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Criteria;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.CriteriaImpl.Subcriteria;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Utils extends Struts2Util {
	private static Logger logger = Logger.getLogger(Utils.class);
	private static DecimalFormat format = new DecimalFormat("#################.##");
	
	
	/** 建立路径 */
	public static void creatDir(String path) {
		File pathf = new File(path);
		if (!pathf.exists()) { // 准备路径
			pathf.mkdir();
		}
	}
	
	/***下载文件**/
	public static void downloadFile(String sURL,String sPath,String sName){
		Utils.creatDir(sPath);
		int nStartPos = 0;
		int nRead = 0;
		RandomAccessFile oSavedFile = null;
		try {
			URL url = new URL(sURL);
			// 打开连接
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			// 获得文件长度
			long nEndPos = getFileSize(sURL);
			oSavedFile = new RandomAccessFile(sPath + "\\"+ sName, "rw");
			httpConnection.setRequestProperty("User-Agent", "Internet Explorer");
			String sProperty = "bytes=" + nStartPos + "-";
			// 告诉服务器book.rar这个文件从nStartPos字节开始传
			httpConnection.setRequestProperty("RANGE", sProperty);
			InputStream input = httpConnection.getInputStream();
			byte[] b = new byte[1024];
			// 读取网络文件,写入指定的文件中
			while ((nRead = input.read(b, 0, 1024)) > 0&& nStartPos < nEndPos) {
				oSavedFile.write(b, 0, nRead);
				nStartPos += nRead;
			}
			httpConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(oSavedFile != null) {
					oSavedFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	// 获得文件长度

	public static long getFileSize(String sURL) {
		int nFileLength = -1;
		try {
			URL url = new URL(sURL);
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestProperty("User-Agent", "Internet Explorer");
			int responseCode = httpConnection.getResponseCode();
			if (responseCode >= 400) {
				System.err.println("Error Code : " + responseCode);
				return -2; // -2 represent access is error
			}
			String sHeader;
			for (int i = 1;; i++) {
				sHeader = httpConnection.getHeaderFieldKey(i);
				if (sHeader != null) {
					if (sHeader.equals("Content-Length")) {
						nFileLength = Integer.parseInt(httpConnection.getHeaderField(sHeader));
						break;
					}
				} else
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nFileLength;

	}
	
	
	/**
	 * @param 字符转译
	 *            （cht to HTML）
	 * @param str
	 * @return
	 */
	public static String revert(String str) {
		if (null == str) {
			return "";
		}
		if ("".equals(str.trim())) {
			return "";
		}
		return str.trim().replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
	}

	/**
	 * @param 字符逆转译
	 *            （HTML to cht）
	 * @param str
	 * @return
	 */
	public static String to_revert(String str) {
		if (null == str) {
			return "";
		}
		if ("".equals(str.trim())) {
			return "";
		}
		return str.trim().replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"");
	}

	/**
	 * 保留小数位数
	 * 
	 * @param val
	 *            原字符串
	 * @param scale
	 *            保留位数，0为取整
	 * @return 处理后字符串, 出现异常则返回null
	 */
	public static String decimalFormat(String str, Integer scale) {
		str = str.trim();
		if (null == str || "".equals(str)) {
			return null;
		}
		StringBuilder pattern = new StringBuilder("#");
		if (scale != 0) {
			pattern.append(".");
		}
		for (int i = 0; i < scale; i++) {
			pattern.append("0");
		}
		DecimalFormat format = new DecimalFormat(pattern.toString());
		try {
			str = format.format(Double.parseDouble(str));
		} catch (Exception e) {
			return null;
		}
		return str;
	}

	/**
	 * 处理数值型数据,浮点型则最多保留两位小数
	 * 
	 * @param str
	 *            String型数据
	 * @return Object 处理后数值(Long or Double),出现异常则返回0
	 */
	public static Object decimalFormat(String str) {
		str = str.trim();
		if (null == str || "".equals(str)) {
			return 0L;
		} else {
			try {
				if (str.contains(".")) { // 含小数点
					DecimalFormat format = new DecimalFormat("#.00");
					// format.applyPattern(".00");
					return Double.parseDouble(format.format(Double.parseDouble(str))); // 处理保留两位小数
				} else {
					return Long.parseLong(str);
				}
			} catch (Exception e) {
				return 0L;
			}
		}
	}

	/**
	 * 判断对象是否为空或0：String/Long/Integer/Double/Float
	 * 
	 * @param object
	 * @return true:空; false:非空
	 */
	public static boolean isEmpty(Object object) {
		if (null == object) {
			return true;
		} else if (object instanceof String && "".equals((object + "").trim())) { // String型
			return true;
		} else if (object instanceof Long && 0L == (Long) object) { // Long型
			return true;
		} else if (object instanceof Integer && 0 == (Integer) object) { // Integer型
			return true;
		} else if (object instanceof Double && 0.0 == (Double) object) { // Double型
			return true;
		} else if (object instanceof Float && 0F == (Float) object) { // Float型
			return true;
		}
		return false;
	}

	/**
	 * 为criteria查询增加关联对象别名，验证别名是否已存在
	 * 
	 * @param criteriaImpl
	 *            criteria实例
	 * @param path
	 *            别名
	 * @param name
	 *            关联对象
	 * @return Criteria
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Criteria addAlias(CriteriaImpl criteriaImpl, String path, String name) {
		if (path == null) {
			return criteriaImpl;
		}
		for (Iterator iter = criteriaImpl.iterateSubcriteria(); iter.hasNext();) {
			Subcriteria subCriteria = (Subcriteria) iter.next();
			if (path.equals(subCriteria.getPath())) {
				return criteriaImpl;
			}
		}
		return criteriaImpl.createAlias(path, name, Criteria.LEFT_JOIN);
	}

	/**
	 * 取出List中的key, 生成Long数组, 用于增加权限过滤
	 * 
	 * @return Long[] keys[key1, key2, key3, ...]
	 */
	public static Long[] keysToArray(List<KeyAndValue> list) {
		if (0 == list.size()) {
			return null;
		}
		Long[] keys = new Long[list.size()];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = Long.valueOf(list.get(i).getKey());
		}
		return keys;
	}
	
	/**
	 * 取出List中的key, 逗号分隔生成字符串, 用于增加权限过滤
	 * @return String keys: key1,key2,key3,...
	 */
	public static String keysToString(List<KeyAndValue> list) {
		if (0 == list.size()) {
			return null;
		}
		String keyString = "";
		String key = null;
		for (int i = 0; i < list.size(); i++) {
			key = list.get(i).getKey();
			if(!Utils.isEmpty(key)) {
				keyString += key + ",";
			}
		}
		if(!keyString.equals("")) {
			return StringUtils.removeEnd(keyString, ",");
		} else {
			return null;
		}
	}
	
	/**
	 * 取出List<KeyAndValue>中的key和value, 转换成map<String, String>
	 * @return map<String, String>
	 */
	public static Map<String, String> listToMap(List<KeyAndValue> list) {
		if (0 == list.size()) {
			return null;
		}
		Map<String, String> dataMap = new HashMap<String, String>();
		String key = null;
		String value = null;
		for (int i = 0; i < list.size(); i++) {
			key = list.get(i).getKey();
			value = list.get(i).getValue();
			if(!Utils.isEmpty(key)) {
				dataMap.put(value, key);
			}
		}
		if(dataMap.isEmpty()) {
			return null;
		} else {
			return dataMap;
		}
	}

	/**
	 * @param 更改名字去掉名字的说明部分
	 * @param name
	 * @return
	 */
	public static String getName(String name) {
		String _name = name;
		if (null == name || "".equals(name)) {
			return "";
		}
		String[] $ = { "-", "\\(", "\\)", "\\（", "\\）" };
		for (int i = 0; i < $.length; i++) {
			if (1 != _name.split($[i]).length) {
				_name = _name.split($[i])[0];
			}
		}
		return _name;
	}

	/**
	 * 导出excel通用方法（无特殊样式）
	 * 
	 * @param <E>
	 * @param list
	 *            数据
	 * @param excelName
	 *            文件名
	 * @param headerArray
	 *            需导列头名,需要转译的字段在列头名后加[cv],需要计算合计的字段在列头名后加[ttli]（整数）/[ttld]（浮点数）,
	 *            例:"序号,公司,车队,行驶公里[ttli],保养级别[cv]"
	 * @param fieldsArray
	 *            列头名对应属性名
	 * @param convert
	 *            需要转译的字库，convert.add("原值1","期望值1");例：列头例中保养级别需转译，convert.add(
	 *            "100","一保项目");convert.add("101","二保项目");...
	 * @return InputStream
	 */
	public static <E> InputStream exportExcel(final List<?> dataList, final String excelName, final String headerArray, final String fieldsArray, final Map<String, String> convert) {
		final String[] columns = headerArray.split(",");// 获得字段名称
		final String[] fields = fieldsArray.split(","); // 获得属性名称
		// 生成EXCEL表
		ExportExcel ee = new ExportExcel() {
			/**
			 * 实现抽象方法生成数据
			 * 
			 * @param workbook
			 *            excel表
			 * @param total
			 *            合计
			 * @param excelData
			 *            数据
			 */
			@Override
			public HSSFWorkbook disposeData(HSSFWorkbook workbook, Object[] total, List<?> excelData) throws IOException {
				boolean hasTotal = false;
				Integer[] ttli = new Integer[columns.length];
				Double[] ttld = new Double[columns.length];
				// 生成表里一个SHEET
				HSSFSheet sheet = workbook.getSheetAt(0);
				// 生成第一行
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, fields.length)); // 合并第一行全部格
				HSSFRow rowOne = sheet.createRow(0);
				rowOne.setHeightInPoints(20);
				HSSFCell hssfCell = rowOne.createCell(0);
				hssfCell = this.createCell(workbook, hssfCell, excelName); // 创建表头
				HSSFCellStyle style = this.createStyle(workbook);
				// 循环处理每条数据
				for (int i = 0; i < excelData.size(); i++) {
					HSSFRow row = sheet.createRow(i + 2); // 生成行
					// 序号列
					HSSFCell cellSeq = row.createCell(0); // 生成格
					cellSeq.setCellStyle(style); // 样式
					cellSeq.setCellValue(String.valueOf(i + 1));
					// 循环处理单条数据的每个字段
					Object obj = null;
					if (!excelData.get(i).getClass().isArray()) { // 数据对像无关联关系
						obj = (Object) excelData.get(i);
					} else { // 有关联关系
						Object[] objArr = (Object[]) excelData.get(i);
						obj = objArr[objArr.length - 1];
					}
					for (int j = 1; j < fields.length + 1; j++) {
						try {
							HSSFCell cell = row.createCell(j); // 生成格
							cell.setCellStyle(style); // 样式
							if (columns[j].contains("[cv]")) { // 需要转译
								cell.setCellValue(String.valueOf(convert.get(String.valueOf(EntityUtil.getValueByField(obj, fields[j-1].trim())))));
							} else {
								cell.setCellValue(String.valueOf(EntityUtil.getValueByField(obj, fields[j-1].trim())));
							}
							if (columns[j].contains("[ttld]")) { // 合计
								hasTotal = true;
								ttld[j] = (ttld[j] == null ? 0 : ttld[j]);
								ttld[j] += Double.valueOf(cell.getStringCellValue());
							} else if (columns[j].contains("[ttli]")) {
								hasTotal = true;
								ttli[j] = (ttli[j] == null ? 0 : ttli[j]);
								ttli[j] += Integer.valueOf(cell.getStringCellValue());
							}
						} catch (IllegalArgumentException iarge) {
							logger.error("导出excel---非法参数！");
							iarge.printStackTrace();
						} catch (IllegalAccessException iacce) {
							logger.error("导出excel---非法访问！");
							iacce.printStackTrace();
						} catch (NoSuchFieldException nsfe) {
							logger.error("导出excel---无属性名！");
							nsfe.printStackTrace();
						}
					}
				}
				if (hasTotal) { // 处理合计行
					HSSFRow totalRow = sheet.createRow(sheet.getLastRowNum() + 1); // 生成行
					HSSFCell totalCell = totalRow.createCell(0);
					totalCell.setCellStyle(this.createFontBigRed(workbook)); // 样式
					totalRow.createCell(1).setCellStyle(this.createFontBigRed(workbook));
					sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), 0, 1));
					totalCell.setCellValue("Total");
					for (int i = 2; i < fields.length + 1; i++) {
						totalCell = totalRow.createCell(i); // 生成格
						totalCell.setCellStyle(this.createFontBigRed(workbook)); // 样式
						if (null != ttli[i]) {
							totalCell.setCellValue(String.valueOf(ttli[i])); // 整数合计
						} else if (null != ttld[i]) {
							totalCell.setCellValue(new DecimalFormat("#.00").format(ttld[i])); // 浮点数合计保留两位小数
						} else {
							totalCell.setCellValue(""); // 不合计为空串
						}
					}
				}
				return workbook;
			}
		};
		Object[] total = null;
		return ee.export(total, dataList, excelName, columns);
	}

	/**
	 * 生成并打印PDF通用方法（无特殊样式）
	 * 
	 * @param <E>
	 * @param title
	 *            标题
	 * @param hasSeq
	 *            是否打印序号列
	 * @param dataList
	 *            数据
	 * @param headerArray
	 *            需导列头名,需要转译的字段在列头名后加[cv],需要计算合计的字段在列头名后加[ttli]（整数）/[ttld]（浮点数）,
	 *            例:"序号,公司,车队,行驶公里[ttli],保养级别[cv]"
	 * @param fieldsArray
	 *            列头名对应属性名
	 * @param convert
	 *            需要转译的字库，convert.add("原值1","期望值1");例：列头例中保养级别需转译，convert.add(
	 *            "100","一保项目");convert.add("101","二保项目");...
	 * @param colWidth
	 *            列宽百分比, 例：{20, 30, 40, 10} (和必须等于100, 无需考虑序列宽度)
	 * @param pageSize
	 *            每页显示条数
	 * @param paper
	 *            纸张规格：不填默认"00":A4纵向、"10":A3纵向、"11":A3横向、"01":A4横向、"00":A4横向(默认)
	 *            、"20":A5纵向、"21":A5横向、"30":241-1纵向、"31":241-1横向、"40":381-1纵向、
	 *            "41":381-1横向
	 * @return InputStream
	 */
	public static <E> void printPDF(final String title, final Boolean hasSeq, final List<?> dataList, final String headerArray, final String fieldsArray, final Map<String, String> convert,
			final Integer[] colWidth, final Integer pageSize, final String... paper) {
		final String[] columns = headerArray.split(",");// 获得字段名称
		final String[] fields = fieldsArray.split(","); // 获得属性名称
		// 生成EXCEL表
		PDFUtil pp = new PDFUtil() {

			/**
			 * 实现生成PDF样式方法
			 * 
			 * @param title
			 *            标题
			 * @param hasSeq
			 *            是否有序列
			 * @param pdfData
			 *            数据集
			 * @param colWidth
			 *            列宽百分比, 例：{20, 30, 40, 10} (和必须等于100, 无需考虑序列宽度)
			 * @param pageSize
			 *            每页显示条数
			 * @param paper
			 *            纸张规格
			 * @return PDF样式路径（包含文件名）
			 */
			@Override
			public String generatePDFContent(String title, Boolean hasSeq, List<?> pdfData, Integer[] colWidth, Integer pageSize, String paper) {
				boolean hasTotal = false;
				Integer[] ttli = new Integer[columns.length];
				Double[] ttld = new Double[columns.length];
				String tempName = "";
				try {
					// 读取主模版方法
					XMLUtil resolver = new XMLUtil(Constants.TEM_SOURCE_DIR_PATH);
					if (resolver.loadXml() != null) {
						Document doc = resolver.getDocument();
						// 纸张规格
						if (null != paper && !"00".equals(paper)) {
							changePaper(doc, paper);
						}
						// 标题
						Node titleNode = doc.getElementsByTagName("fo:block").item(1);
						titleNode.setTextContent(title);
						// 日期
						Node dateNode = doc.getElementsByTagName("fo:block").item(2);
						dateNode.setTextContent("Print Date：" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
						// 获得table标签
						Node tableNode = doc.getElementsByTagName("fo:table").item(0);
						// 初始化列数
						Element columnEle = null;
						if (hasSeq) {
							columnEle = doc.createElement("fo:table-column");
							columnEle.setAttribute("column-width", "proportional-column-width(5)");
							tableNode.appendChild(columnEle);
						}
						for (int i = 0; i < columns.length; i++) {
							columnEle = doc.createElement("fo:table-column");
							columnEle.setAttribute("column-width", "proportional-column-width(" + colWidth[i] + ")");
							tableNode.appendChild(columnEle);
						}
						// 表头
						Element tableHeader = doc.createElement("fo:table-header");
						tableNode.appendChild(tableHeader);
						if (hasSeq) {
							this.createTableHeaderCell(resolver, tableHeader, "No.");
						}
						for (int i = 0; i < columns.length; i++) {
							this.createTableHeaderCell(resolver, tableHeader, columns[i].replace("[cv]", "").replace("[ttli]", "").replace("[ttld]", ""));
						}
						// 表体
						Element tableBody = doc.createElement("fo:table-body");
						tableNode.appendChild(tableBody);
						// 行循环
						Element tableRow = null;
						Element bodyCell = null;
						for (int i = 0; i < pdfData.size(); i++) {
							tableRow = resolver.getDocument().createElement("fo:table-row");
							tableBody.appendChild(tableRow);
							// 循环处理单条数据的每个字段
							if (hasSeq) {
								// 序号列
								bodyCell = this.createTableBodyCell(resolver, tableRow, String.valueOf(i + 1));
								bodyCell.setAttribute("column-number", String.valueOf(1));
								if (i != 0 && (i + 1) % pageSize == 0) {
									bodyCell.setAttribute("break-after", "page");
								}
							}
							Object obj = null;
							if (!pdfData.get(i).getClass().isArray()) { // 数据对像无关联关系
								obj = (Object) pdfData.get(i);
							} else { // 有关联关系
								Object[] objArr = (Object[]) pdfData.get(i);
								obj = objArr[objArr.length - 1];
							}
							for (int j = 0, k = fields.length; j < k; j++) {
								try {
									if (columns[j].contains("[cv]")) { // 需要转译
										bodyCell = this.createTableBodyCell(resolver, tableRow, String.valueOf(convert.get(EntityUtil.getValueByField(obj, fields[j].trim()))));
									} else {
										bodyCell = this.createTableBodyCell(resolver, tableRow, EntityUtil.getValueByField(obj, fields[j].trim()));
									}
									if (columns[j].contains("[ttld]")) { // 合计浮点型
										hasTotal = true;
										ttld[j] = (ttld[j] == null ? 0 : ttld[j]);
										ttld[j] += Double.valueOf(bodyCell.getTextContent());
									} else if (columns[j].contains("[ttli]")) { // 合计整形
										hasTotal = true;
										ttli[j] = (ttli[j] == null ? 0 : ttli[j]);
										ttli[j] += Integer.valueOf(bodyCell.getTextContent());
									}
									bodyCell.setAttribute("column-number", String.valueOf(hasSeq ? (j + 2) : (j + 1)));
									if (i != 0 && (i + 1) % pageSize == 0) {
										bodyCell.setAttribute("break-after", "page"); // 分页
									}
								} catch (IllegalArgumentException iarge) {
									logger.error("pdf打印---非法参数！");
									iarge.printStackTrace();
								} catch (IllegalAccessException iacce) {
									logger.error("pdf打印---非法访问！");
									iacce.printStackTrace();
								} catch (NoSuchFieldException nsfe) {
									logger.error("pdf打印---无属性名！");
									nsfe.printStackTrace();
								}
							}
						}
						if (hasTotal) { // 处理合计行
							tableRow = resolver.getDocument().createElement("fo:table-row"); // 生成行
							tableBody.appendChild(tableRow);
							bodyCell = this.createTableBodyCell(resolver, tableRow, "Total");
							for (int i = 1; i < fields.length + 1; i++) {
								if (null != ttli[i]) {
									this.createTableBodyCell(resolver, tableRow, String.valueOf(ttli[i])); // 整数合计
								} else if (null != ttld[i]) {
									this.createTableBodyCell(resolver, tableRow, new DecimalFormat("#.00").format(ttld[i])); // 浮点数合计保留两位小数
								} else {
									this.createTableBodyCell(resolver, tableRow, ""); // 不合计为空串
								}
							}
						}
					}
					tempName = resolver.xml2File(new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()));
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}
				return tempName;
			}
		};
		try {
			if (0 == paper.length) {
				pp.doPrint(title, true, dataList, colWidth, pageSize, "00");
			} else {
				pp.doPrint(title, true, dataList, colWidth, pageSize, paper[0]);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 格式化Double
	 * 
	 * @param obj
	 * @return
	 */
	public static String doubleFmt(Object obj) {
		return null == obj ? "" : format.format(Double.valueOf(String.valueOf(obj)));
	}

	public static String doubleFmt(Double obj) {
		return null == obj ? "" : format.format(obj);
	}

	public static String doubleFmt(String obj) {
		return null == obj ? "" : format.format(obj);
	}

	/**
	 * 自定义格式化Double
	 * 
	 * @param obj
	 * @param fmt
	 * @return
	 */
	public static String doubleFmt(Object obj, String fmt) {
		DecimalFormat _format = new DecimalFormat(fmt);
		return null == obj ? "" : _format.format(Double.valueOf(String.valueOf(obj)));
	}

	public static String doubleFmt(Double obj, String fmt) {
		DecimalFormat _format = new DecimalFormat(fmt);
		return null == obj ? "" : _format.format(obj);
	}

	public static String doubleFmt(String obj, String fmt) {
		DecimalFormat _format = new DecimalFormat(fmt);
		return null == obj ? "" : _format.format(obj);
	}
	
	/***
	 * 从xml中解析<path></path> 中间的字符串
	 * @param str
	 * @return
	 */
	public static List<String> getpath(String ip,String str){
		List<String> strs=new ArrayList<String>();
		String pstr= "<path>";
		String epstr= "</path>";
		
		int b= str.indexOf(pstr);
		int c;
		while(b>0){
			str=str.substring(b+pstr.length());
			c=str.indexOf(epstr);
			if(c<0){
				break;
			}
			strs.add("http://"+ip+"/sd/"+str.substring(0, c));
			str=str.substring(c+epstr.length());
			b= str.indexOf(pstr);
		}
		if(strs!=null && strs.size()!=0){
			List<String> strs2=new ArrayList<String>();
			strs2.add(strs.get(0));
			return strs2;
		}
		return null;
	}
	/***
	 * 从xml中解析<path></path> 中间的字符串
	 * @param str
	 * @return
	 */
	public static List<String> getpath3(String ip,String str){
		List<String> strs=new ArrayList<String>();
		String pstr= "<destPath>";
		String epstr= "</destPath>";
		
		int b= str.indexOf(pstr);
		int c;
		while(b>0){
			str=str.substring(b+pstr.length());
			c=str.indexOf(epstr);
			if(c<0){
				break;
			}
			strs.add("http://"+ip+"/cgi-bin/admin/downloadMedias.cgi?"+str.substring(0, c));
			str=str.substring(c+epstr.length());
			b= str.indexOf(pstr);
		}
		if(strs!=null && strs.size()!=0){
			List<String> strs2=new ArrayList<String>();
			strs2.add(strs.get(strs.size()-1));
			return strs2;
		}
		return null;
	}

	/**
	 * 根据正则表达式提取指定字符串
	 * @param regex
	 * @param source
	 * @return
	 */
	public static String getMatcher(String regex, String source) {  
        String result = "";  
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);  
        Matcher matcher = pattern.matcher(source);  
        while (matcher.find()) {  
            result = matcher.group(1);//只取第一组  
        }  
        return result;  
    }
	
	/***
	 * 从xml中解析<path></path> 中间的字符串
	 * @param str
	 * @return
	 */
	public static List<String> getpath2(String ip,String str){
		List<String> strs=new ArrayList<String>();
		String pstr= "/video\\/";
		String epstr= "\",";
		
		int b= str.indexOf(pstr);
		int c;
		while(b>0){
			str=str.substring(b+pstr.length());
			c=str.indexOf(epstr);
			if(c<0){
				break;
			}
			strs.add(ip+","+str.substring(0, c));
			str=str.substring(c+epstr.length());
			b= str.indexOf(pstr);
		}
		if(strs!=null && strs.size()!=0){
			List<String> strs2=new ArrayList<String>();
			strs2.add(strs.get(0));
			return strs2;
		}
		return null;
	}
	
	/***
	 * 转换avi名字为字符串 ipcam 2.x下载用
	 * @param name
	 * @return
	 */
	public static String toSaveStr(String name){
		String savestr="";
		if(name.startsWith("EVENT_")){
			savestr=name.substring("EVENT_".length(),name.indexOf("."));
		}else{
			savestr=name.substring(0, name.indexOf("_S"));
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
		try {
			savestr=sdf2.format(sdf.parse(savestr));
		} catch (ParseException e) {
		}
		return savestr;
	}
	/**
	 * 将数据以文件形式保存到服务器
	 * 
	 * @param path保存路径
	 * @param type
	 *            扩展名
	 * @param fname
	 *            文件名
	 * @param text
	 *            内容
	 */
	public static String saveFile(String path, String fname, byte[] text) {
		Utils.creatDir(path);
		OutputStream out = null;
		try {
			File pathf = new File(path);
			if (!pathf.exists()) { // 准备路径
				pathf.mkdir();
			}
			String pathFlie = path + "/" + fname;
			File file = new File(pathFlie);

			out = new FileOutputStream(file);
			out = new java.io.BufferedOutputStream(out);
			out.write(text);
			return pathFlie;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	
	

}