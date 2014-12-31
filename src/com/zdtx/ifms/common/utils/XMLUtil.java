package com.zdtx.ifms.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @ClassName: PrintManager
 * @Description: 打印模块-XML处理帮助类
 * @author Leon Liu
 * @date 2012-5-21 下午03:49:37
 * @version V1.0
 */


public class XMLUtil {

	private Document document;
	private String filename;

	/**
	 * 构造方法，接收文件路径和名称，并读取转换为Document
	 * @param filename	文件路径和名称
	 * @throws ParserConfigurationException
	 */
	public XMLUtil(String filename) throws ParserConfigurationException {
		this.filename = filename;
		this.document = loadXml();
	}

	public Document getDocument() {
		return this.document;
	}

	/**
	 * 得到xml文档
	 *
	 * @param filename 文件路径和名称
	 * @return	文件转换后的Document对象
	 * @throws ParserConfigurationException
	 */
	public Document loadXml() throws ParserConfigurationException {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		File file = new File(filename);
		if (file.exists()) {
			try {
				document = builder.parse(filename);
				document.normalize();	//document结构标准化
			} catch (SAXException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return document;
	}

	/**
	 * 修改某个节点的内容,将 nodeName 的值修改为newNodeValue
	 *
	 * @param nodeName
	 * @param newNodeValue
	 * @throws ParserConfigurationException
	 */
	public void xmlUpdate(String nodeName, String newNodeValue)
			throws ParserConfigurationException {
		Node root = document.getDocumentElement();
		// 如果root有子元素
		if (root.hasChildNodes()) {
			//ftpnodes
			NodeList ftpnodes = root.getChildNodes();
			//循环取得ftp所有节点
			for (int i = 0; i < ftpnodes.getLength(); i++) {
				NodeList ftplist = ftpnodes.item(i).getChildNodes();
				for (int k = 0; k < ftplist.getLength(); k++) {
					Node subnode = ftplist.item(k);
					if (subnode.getNodeType() == Node.ELEMENT_NODE
							&& subnode.getNodeName() == nodeName) {
						subnode.getFirstChild().setNodeValue(newNodeValue);
					}
				}
			}
		}
		xml2File("");
	}

	/**
	 * 读取某个节点的内容
	 *
	 * @param nodename 节点name属性值
	 * @return 节点内容
	 */
	public String readNode(String nodename) {
		Node node = document.getElementsByTagName(nodename).item(0);
		return node.getTextContent();
	}

	/**
	 * 读取某个节点的属性
	 *
	 * @param nodename 节点name属性值
	 * @param itemname	节点属性名
	 * @return 节点属性值
	 */
	public String readAttribute(String nodename, String itemname) {
		Node node = document.getElementsByTagName(nodename).item(0);
		return node.getAttributes().getNamedItem(itemname).getTextContent();
	}

	/**
	 * 获取XML标准格式内容的String中的某节点的某属性值
	 * @param str	XML标准格式内容的String
	 * @param nodename	节点名
	 * @param itemname	节点属性名
	 * @return	查询对应的属性值
	 */
	public String parseString(String str, String nodename, String itemname) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dber;
		try {
			dber = factory.newDocumentBuilder();
			str = "<xroot>" + str + "</xroot>";
			ByteArrayInputStream bais = null;
			try {
				bais = new ByteArrayInputStream(str.getBytes("UTF-8"));
				Document docf;
				try {
					docf = dber.parse(bais);
					Node node = docf.getElementsByTagName(nodename).item(0);
					return node.getAttributes().getNamedItem(itemname)
							.getTextContent();
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (SAXException ex) {
					ex.printStackTrace();
				}
			} catch (UnsupportedEncodingException uee) {
				uee.printStackTrace();
			}
		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * 读取的document转成文件
	 * @param name 新文件名
	 * @return	新旧文件组合名
	 */
	public String xml2File(String name) {
		String newFileName = "";
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			newFileName = filename.split("\\.")[0] + "_" + name + "." + filename.split("\\.")[1];
			PrintWriter pw = new PrintWriter(newFileName, "UTF-8");
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (TransformerException mye) {
			mye.printStackTrace();
		} catch (IOException exp) {
			exp.printStackTrace();
		}
		return newFileName;
	}
}