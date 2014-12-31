package com.zdtx.ifms.common.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

/**
 *	web servlet 相关帮助类
 * @author Leon Liu
 * @since 2013-3-18 10:25:07
 */
public class Struts2Util {

	private static final String NOCACHE = "no-cache";
	private static final String UTF8 = "UTF-8";

	private static Logger logger = Logger.getLogger(Struts2Util.class);

	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	
	public static HttpServletResponse getResponse() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		return response;
	}
	
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static OutputStream getOutputStream(String filePath) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(filePath, "UTF-8"));	//inline表示在页面中打开。如果要以附件方式下载，则改为attachment。
//		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filePath, "UTF-8"));
		return response.getOutputStream();
	}

	/**
	 * Get output stream for download file from server to client
	 * @param filePath
	 * @return OutputStream
	 * @throws IOException
	 */
	public static OutputStream getOutputStreamDL(String filePath) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filePath, "UTF-8"));	//inline表示在页面中打开。如果要以附件方式下载，则改为attachment。
		return response.getOutputStream();
	}
	
	public static String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	public static Boolean hasParameter(String name) {
		return null != getParameter(name);
	}

	public static void render(final String textType, final String content,
			final String... params) {
		try {
			String charset = UTF8;	//编码
			boolean isNoCache = true;	//缓存
			for (String param : params) {		//读取每个参数的键值对
				String key = StringUtils.substringBefore(param, ":");
				String value = StringUtils.substringAfter(param, ":");

				if (StringUtils.equalsIgnoreCase(key, "encoding")) {	//encoding setting
					charset = value;
				} else if (StringUtils.equalsIgnoreCase(key, NOCACHE)) {
					isNoCache = Boolean.parseBoolean(value);
				} else {
					throw new IllegalArgumentException(key + "not true");
				}
			}

			HttpServletResponse response = ServletActionContext.getResponse();

			String contentType = textType + ";charset=" + charset;
			response.setContentType(contentType);
			if (isNoCache) {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", NOCACHE);
				response.setDateHeader("Expires", 0);
			}
			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void renderText(final String content, final String... params) {
		render("text/javascript", content, params);
	}

	public static void renderHtml(final String content, final String... params) {
		render("text/html", content, params);
	}

	public static void renderXml(final String content, final String... params) {
		render("text/xml", content, params);
	}

	public static void renderJson(final String content, final String... params) {
		render("application/json", content, params);
	}

	@SuppressWarnings("rawtypes")
	public static void renderJson(final Map map, final String... params) {
		String mapStr = JSONObject.fromObject(map).toString();
		renderJson(mapStr, params);
	}

	/**
	 * 处理JSON内循环（宽大策略）
	 * @param jsonObj
	 * @param params
	 */
	public static void renderJson(final Object jsonObj, final String... params) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		String jsonStr = JSONObject.fromObject(jsonObj, jsonConfig).toString();
		renderJson(jsonStr, params);
	}

	/**
	 * 向前台发送alert提示信息(多用于验证表单)
	 * @param msg 提示信息内容
	 */
	public static void sendMsg(String msg) {
		PrintWriter out = null;
		try {
			out = getResponse().getWriter();
			out.print("<script language='JavaScript'>alert('" + msg + "');window.history.back();</script>");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			out.close();
		}
	}
	
	/**
	 * 返回成功结果，跳转
	 * @param out	PrintWriter
	 * @param url	跳转URL（模块基本路径）
	 * @param hihetl	高亮显示ID
	 */
	public static void printSuccess(PrintWriter out, String url, Long hihetl) {
		if (null != out) {
			out.print(Constants.SUCCESS[0] + Struts2Util.getBasePath() + url + "?highlight=" + hihetl + Constants.SUCCESS[1]);
		}
	}
	
	/**
	 * 返回成功结果，跳转
	 * @param out	PrintWriter
	 * @param url	跳转URL（模块基本路径）
	 * @param hihetl	高亮显示ID
	 */
	public static void printSuccess(String url, Long hihetl) {
		PrintWriter out = null;
		try {
			out = getResponse().getWriter();
			out.print(Constants.SUCCESS[0] + Struts2Util.getBasePath() + url + "?editble=1&highlight=" + hihetl + Constants.SUCCESS[1]);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			out.close();
		}
	}

	/**
	 * 返回失败结果，跳转
	 * @param out	PrintWriter
	 */
	public static void printError(PrintWriter out) {
		if (null != out) {
			out.print(Constants.ERROR);
		}
	}

	/**
	 * 返回失败结果，跳转（打印异常内容）
	 * @param out	PrintWriter
	 * @param e	Exception
	 */
	public static void printError(PrintWriter out, Exception e) {
		logger.error(e.getMessage(), e);
		if (null != out) {
			out.print(Constants.ERROR);
		}
	}
	
	/**
	 * 返回失败结果，跳转（打印异常内容）
	 * @param out	PrintWriter
	 * @param e	Exception
	 */
	public static void printError(Exception e) {
		logger.error(e.getMessage(), e);
		PrintWriter out = null;
		try {
			out = getResponse().getWriter();
			out.print(Constants.ERROR);
		} catch (IOException ioe) {
			logger.error(ioe.getMessage(), ioe);
		} finally {
			out.close();
		}
	}
	
    /**
     * 通过HttpServletRequest返回IP客户端地址
     * @param request HttpServletRequest
     * @return ip String
     * @throws Exception
     */
    public static String getIpAddr(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

	/**
	 * 获得请求地址
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getBasePath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/";
	}

	/**
	 * 获取本项目url地址
	 * @param
	 * @return BasePath
	 */
	public static String getBasePath() {
		HttpServletRequest request = Struts2Util.getRequest();
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/";
	}
}