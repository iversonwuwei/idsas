package com.zdtx.ifms.common.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;

import com.opensymphony.xwork2.ActionSupport;
import com.zdtx.ifms.common.utils.Struts2Util;

/**
 * @ClassName: FileUploadResolver
 * @Description: 文件上传帮助类
 * @author Leon Liu
 * @date 2012-4-9 上午09:44:51
 * @version V1.0
 */
@ParentPackage("mainten-default")
@InterceptorRefs( {
		@InterceptorRef("mainLogin"),
		@InterceptorRef("restDefaultStack"),
		@InterceptorRef(value = "fileUpload", params = { "allowedTypes",
				"application/octet-stream, application/vnd.ms-excel, image/gif, image/jpeg, image/png",
				"maximumSize", "5000000" }) })
@Scope("prototype")
@Result(name = "img", type = "stream", params = { "contentType", "image/jpeg", "inputName", "imgCode" })
public class FileUploadController extends ActionSupport {

	private static final long serialVersionUID = -7107530871434953668L;
	private static final String IMAGE_DESTINATION_DIR_PATH = System.getProperty("catalina.home") + "/upload_imeges";
	private static final String EXCEL_DESTINATION_DIR_PATH = System.getProperty("catalina.home") + "/upload_excels";

	private File iefile;

	private ByteArrayInputStream imgCode;

	/**
	 * 	上传文件方法
	 * @return JSON
	 */
	public String upload() {
		InputStream is = null;
		FileOutputStream fos = null;
		String fileName = Struts2Util.getRequest().getParameter("qqfile");	//文件名
		String fileType = Struts2Util.getRequest().getParameter("fileType");	//文件类型
		String dirPath = "";
		File dirFile = null;
		if(fileType.equals("image")) {		//不同文件类型区分保存在各自路径下
			dirFile = new File(IMAGE_DESTINATION_DIR_PATH);
			dirPath = IMAGE_DESTINATION_DIR_PATH;
		} else if(fileType.equals("excel")) {
			dirFile = new File(EXCEL_DESTINATION_DIR_PATH);
			dirPath = EXCEL_DESTINATION_DIR_PATH;
		}
		if(!dirFile.isDirectory()) {			//验证路径
			if(!dirFile.mkdir()) {
				Struts2Util.renderText("{\"result\" : \"dirError\"}");
				return null;
			}
		}
		try {
			if(null != iefile) {	//ie浏览器特殊处理
				is = new FileInputStream(iefile);
			} else {
				is = Struts2Util.getRequest().getInputStream();
			}
			fileName = new String(fileName.getBytes("ISO-8859-1"),"UTF-8");      //url中文乱码处理
			//改文件名为统一标识，格式：IP_timestamp.xxx
			try {
				fileName = Struts2Util.getIpAddr(Struts2Util.getRequest()).replaceAll("\\.", "-") + "_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS").format(new Date()) + fileName.substring(fileName.indexOf("."), fileName.length());
			} catch (Exception e) {
			}
			fos = new FileOutputStream(new File(dirPath + "/" + fileName));
			IOUtils.copy(is, fos);
			if(fileType.equals("image")) {
				Struts2Util.getSession().setAttribute("upload_img", fileName);	//将照片临时保存到session中以在页面显示
			}
			Struts2Util.renderHtml("{success : \"" + fileName + "\"}");	//ajax返回文件名
			//String oldFile =  Struts2Utils.getRequest().getParameter("oldFile");		//删除旧文件
			//this.deleteFile(oldFile);
		} catch (FileNotFoundException ex) {
			Struts2Util.renderHtml("{error : notFound}");
		} catch (IOException ex) {
			Struts2Util.renderHtml("{error : io}");
		} finally {
			try {
				fos.close();
				is.close();
			} catch (IOException ignored) {
			}
		}
		return null;
	}

	/**
	 * 页面即时显示照片
	 * @return
	 */
	public String showImg() {
		String name = "";
		if(null != Struts2Util.getParameter("ul_path")) {
			name = Struts2Util.getParameter("ul_path");
		} else {
			name = (String)Struts2Util.getSession().getAttribute("upload_img");
		}
		InputStream is = null;
		try {
			is = new FileInputStream(new File(IMAGE_DESTINATION_DIR_PATH + "/" + name));
			byte[] bytes = new byte[is.available()];					//一次性读写
			is.read(bytes);
			imgCode = new ByteArrayInputStream(bytes);	//准备好的待返回数据流
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return "img";
	}

	/**
	 * 删除旧图片
	 * @param fileName	文件名
	 */
	public static void deleteFile(String fileName) {
		File img_file = new File(IMAGE_DESTINATION_DIR_PATH + "/" + fileName);
		if(img_file.exists()) {				//存在旧图片
			img_file.delete();
		}
		File exc_file = new File(EXCEL_DESTINATION_DIR_PATH + "/" + fileName);
		if(exc_file.exists()) {				//存在旧图片
			exc_file.delete();
		}
	}
	
	public void setIefile(File iefile) {
		this.iefile = iefile;
	}

	public File getIefile() {
		return iefile;
	}

	public ByteArrayInputStream getImgCode() {
		return imgCode;
	}

	public void setImgCode(ByteArrayInputStream imgCode) {
		this.imgCode = imgCode;
	}
}