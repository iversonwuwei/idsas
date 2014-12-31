/**
 * @File: IpCamManager.java
 * @path: idsas - com.zdtx.ifms.specific.service.monitor
 */
package com.zdtx.ifms.specific.service.monitor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.CipherUtil;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.ExportExcel;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.monitor.CamDevice;
import com.zdtx.ifms.specific.model.monitor.CamUser;
import com.zdtx.ifms.specific.model.monitor.Camera;
import com.zdtx.ifms.specific.model.system.CamModel;
import com.zdtx.ifms.specific.vo.monitor.CamUserVO;
import com.zdtx.ifms.specific.vo.monitor.IpCamVO;

/**
 * @ClassName: IpCamManager
 * @Description: monitor-Ip camera-manager
 * @author: Leon Liu
 * @date: 2013-7-8 下午3:01:49
 * @version V1.0
 */
@Service
@Transactional
public class IpCamManager {
	
	private Logger logger = Logger.getLogger(IpCamManager.class);

	@Autowired
	private BaseDao dao;
	
	public Page<Camera> getBatch(Page<Camera> page, IpCamVO vo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Camera.class);
		criteria.createAlias("modelID", "modelID");
		criteria.add(Restrictions.eq("isDelete", "F"));
		
		criteria.add(Restrictions.in("deptid", (Long[])Struts2Util.getSession().getAttribute("userDepartment")));
		if (null != vo.getDepartmentid() && -1L != vo.getDepartmentid()) {
			criteria.add(Restrictions.eq("deptid", vo.getDepartmentid()));
		}
		
		if (!Utils.isEmpty(vo.getModelID()) && vo.getModelID() != -1L) {
			criteria.add(Restrictions.eq("modelID.modelID", vo.getModelID()));
		}
		if (!Utils.isEmpty(vo.getCamName())) {
			criteria.add(Restrictions.ilike("cameraName", "%" + vo.getCamName() + "%"));
		}
		if (!Utils.isEmpty(vo.getCamCode())) {
			criteria.add(Restrictions.ilike("cameraCode", "%" + vo.getCamCode() + "%"));
		}
		if (!Utils.isEmpty(vo.getCamIP())) {
			criteria.add(Restrictions.ilike("ipAddress", "%" + vo.getCamIP() + "%"));
		}
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(Order.asc("cameraName"));
//		orders.add(Order.asc("modelID.modelID"));
		Page<Camera> pageResult=dao.getBatch(page,
				criteria.getExecutableCriteria(dao.getSession()),
				orderList);

		if (1 == pageResult.getCurrentPage()) {
			Utils.getSession().setAttribute("criteria_export", criteria);
			Utils.getSession().setAttribute("page_export", page);
			Utils.getSession().setAttribute("orderList_export", orderList);
		}
		return pageResult;
	}
	
	public List<Camera> getCamerByVehId(Long id){
		List<Camera> cams=new ArrayList<Camera>();
		String sql="select c.cameraid as key ,c.cameraid as value from T_CORE_CAMERA c where c.cameraid in (select b.cameraid from T_CORE_CAMERA_DEVICE b where b.deviceid in (select a.o_deviceno from t_core_unite a where a.o_busid="+id+"))";
		List<KeyAndValue> camids=dao.getKeyAndValueBySQL(sql);
		for(KeyAndValue k:camids){
			Camera tc=dao.get(Camera.class, Long.parseLong(k.getKey()));
			if(tc!=null){
				cams.add(tc);
			}
		}
		return cams;
	}
	public List<KeyAndValue> getCamips(Long id){
		String sql="select t.cctvip as key, t.cctvip as value from T_CORE_VEHICLE t where t.vehicleid=" + id;
		return dao.getKeyAndValueBySQL(sql);
	}
	public List<KeyAndValue> getCamipAndType(Long id){
		String sql="select a.cctvip as key,d.type as value from T_CORE_VEHICLE a,t_core_unite b,t_core_camera c,t_core_camera_model d where a.deviceid=b.o_deviceno and b.o_videono=c.cameraid and c.modelid=d.modelid and a.vehicleid=" + id;
		return dao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * Set ip camera's parameter
	 * @param ip									camera's ip
	 * @param paramName			param's name(full name-- group.subgroup.param)
	 * @param paramValue			param's value
	 * @param nameAndPass		username and password of authorized account ,necessary when isAuth = true
	 * @return	operate's result		true success; false: fail;
	 */
	public Boolean setIpParam(String ip, String paramName, String paramValue, String... nameAndPass) {
		if(ip == null || paramName == null || paramValue == null) {
			return Boolean.FALSE;
		}
		String url = "http://" + ip + "/operator/set_param.cgi?" + paramName + "=" + paramValue;
		logger.info(url);
		HttpClient httpClient = new HttpClient();
		HttpMethod method = new GetMethod(url);
		if(nameAndPass != null && nameAndPass.length == 2) {
			httpClient.getParams().setAuthenticationPreemptive(true);		//抢先认证，只发一次请求
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);	//连接超时时间
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(nameAndPass[0], nameAndPass[1]);	//账号密码证书
			httpClient.getState().setCredentials(AuthScope.ANY, credentials);
			method.setDoAuthentication(true);
		}
		try {
			httpClient.executeMethod(method);
			return !Utils.isEmpty(method.getResponseBodyAsString());
		} catch (ConnectTimeoutException cte) {
			logger.error("Set camera parameter: connect timed out! To:" + ip);
			return Boolean.FALSE;
		} catch (HttpException he) {
			logger.error("Set camera parameter: HttpException!");
			he.printStackTrace();
			return Boolean.FALSE;
		} catch (IOException ioe) {
			logger.error("Set camera parameter: IOException!");
			ioe.printStackTrace();
			return Boolean.FALSE;
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * 获得列表 通过时间和日期
	 */
	@SuppressWarnings("unused")
	public String getvideo(String ip, String date, String time, String name, String pwd) {
		if(ip == null || date == null || time == null) {
		//	return "false";
		}
		String url = "http://"+ip+"/operator/PostRecordingFileList.cgi?sdate="+DateUtil.toyyyyMMdd(date)+"&stime="+DateUtil.toHHmmssPlus(-100, date+" "+time)+"000&edate="+DateUtil.toyyyyMMdd(date)+"&etime="+DateUtil.toHHmmssPlus(0, date+" "+time)+"000";
//		String url="http://192.168.80.120/asp/listcontinuous.cgi?recordingid=19700101";
//		name="admin";
//		pwd="";
		logger.info(url);
		URL connect = null;
		BufferedReader reader = null;
		try {
			connect = new URL(url);
			URLConnection connection = connect.openConnection();
			connection.setRequestProperty("Authorization", "Basic " + CipherUtil.encodeBase64(name + ":" + pwd));		//增加权限设定，否则401无法访问
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			int c = 0;
			String str = "";
			String res = "";
			while((str = reader.readLine()) != null) {
				c ++;
				res += str;
			}
			return res;
		} catch (ConnectException conne) {
			logger.error("NullPointerException:Unable to connect the camera with IP:" + ip);
			conne.printStackTrace();
			return "false";
		} catch (IOException ioe) {
			logger.error("Set camera parameter: IOException");
			ioe.printStackTrace();
			return "false";
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error("Set camera parameter: IOException");
				e.printStackTrace();
				return "false";
			} catch (NullPointerException ne) {
				logger.error("NullPointerException:Unable to connect the camera with IP:" + ip);
				ne.printStackTrace();
				return "false";
			}
		}
	}
	
	
	
	/**
	 * 获得列表 通过时间和日期
	 */
	@SuppressWarnings("unused")
	public String getvideo3(String ip, String date, String time, String name, String pwd) {
		name="root";
		pwd="root";
		if(ip == null || date == null || time == null) {
		//	return "false";
		}
		String url="http://"+ip+"/cgi-bin/admin/lsctrl.cgi?cmd=search&triggerTime="+DateUtil.toHHmmssPlus3(-100, date+" "+time)+"+TO+"+DateUtil.toHHmmssPlus3(0, date+" "+time)+"";
		//		name="admin";
//		pwd="";
		logger.info(url);
		URL connect = null;
		BufferedReader reader = null;
		try {
			connect = new URL(url);
			URLConnection connection = connect.openConnection();
			connection.setRequestProperty("Authorization", "Basic " + CipherUtil.encodeBase64(name + ":" + pwd));		//增加权限设定，否则401无法访问
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			int c = 0;
			String str = "";
			String res = "";
			while((str = reader.readLine()) != null) {
				c ++;
				res += str;
			}
			return res;
		} catch (ConnectException conne) {
			logger.error("NullPointerException:Unable to connect the camera with IP:" + ip);
			conne.printStackTrace();
			return "false";
		} catch (IOException ioe) {
			logger.error("Set camera parameter: IOException");
			ioe.printStackTrace();
			return "false";
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error("Set camera parameter: IOException");
				e.printStackTrace();
				return "false";
			} catch (NullPointerException ne) {
				logger.error("NullPointerException:Unable to connect the camera with IP:" + ip);
				ne.printStackTrace();
				return "false";
			}
		}
	}
	
	
	/***
	 * 
	 * @param ip
	 * @param date
	 * @param time
	 * @param name
	 * @param pwd
	 * @return
	 */
	public String exeUrl(String url, String name, String pwd) {
		//		name="admin";
//		pwd="";
		logger.info(url);
		URL connect = null;
		BufferedReader reader = null;
		try {
			connect = new URL(url);
			URLConnection connection = connect.openConnection();
			connection.setRequestProperty("Authorization", "Basic " + CipherUtil.encodeBase64(name + ":" + pwd));		//增加权限设定，否则401无法访问
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String str = "";
			String res = "";
			while((str = reader.readLine()) != null) {
				res += str;
			}
			return res;
		} catch (ConnectException conne) {
			logger.error("NullPointerException:Unable to connect the camera with IP:");
			conne.printStackTrace();
			return "false";
		} catch (IOException ioe) {
			logger.error("Set camera parameter: IOException");
			ioe.printStackTrace();
			return "false";
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error("Set camera parameter: IOException");
				e.printStackTrace();
				return "false";
			} catch (NullPointerException ne) {
				logger.error("NullPointerException:Unable to connect the camera with IP:" );
				ne.printStackTrace();
				return "false";
			}
		}
	}
	
	/**
	 * Set ip camera's parameters
	 * @param ip									camera's ip
	 * @param paramsStr				params string 	"param1=value1&param2=value2..."
	 * @param nameAndPass		username and password of authorized account ,necessary when isAuth = true
	 * @return	operate's result		success:params string;	fail:null;
	 */
	public String setIpParams(String ip, String paramsStr, String... nameAndPass) {
		if(ip == null || paramsStr == null) {
			return null;
		}
		String url = "http://" + ip + "/operator/set_param.cgi?" + paramsStr;
		logger.info(url);
		HttpClient httpClient = new HttpClient();
		HttpMethod method = new GetMethod(url);
		if(nameAndPass != null && nameAndPass.length == 2) {
			httpClient.getParams().setAuthenticationPreemptive(true);		//抢先认证，只发一次请求
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);	//连接超时时间
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(nameAndPass[0], nameAndPass[1]);	//账号密码证书
			httpClient.getState().setCredentials(AuthScope.ANY, credentials);
			method.setDoAuthentication(true);
		}
		try {
			httpClient.executeMethod(method);
			return method.getResponseBodyAsString();
		} catch (ConnectTimeoutException cte) {
			logger.error("Set camera parameters: connect timed out! To:" + ip);
			return "TIMEOUT";
		} catch (HttpException he) {
			logger.error("Set camera parameters: HttpException");
			he.printStackTrace();
			return null;
		} catch (IOException ioe) {
			logger.error("Set camera parameters: IOException");
			ioe.printStackTrace();
			return "REFUSED";
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * Get ip camera's parameter
	 * @param ip									camera's ip
	 * @param paramName			param's name(full name-- group.subgroup.param)
	 * @param username				username of authorized account
	 * @param password					password of authorized account
	 * @return	param's value;
	 */
	public String getIpParam(String ip, String paramName, String... nameAndPass) {
		if(ip == null || paramName == null) {
			return null;
		}
		String url = "http://" + ip + "/operator/get_param.cgi?" + paramName;
		logger.info(url);
		HttpClient httpClient = new HttpClient();
		HttpMethod method = new GetMethod(url);
		if(nameAndPass != null && nameAndPass.length == 2) {
			httpClient.getParams().setAuthenticationPreemptive(true);		//抢先认证，只发一次请求
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);	//连接超时时间
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(nameAndPass[0], nameAndPass[1]);	//账号密码证书
			httpClient.getState().setCredentials(AuthScope.ANY, credentials);
			method.setDoAuthentication(true);
		}
		try {
			httpClient.executeMethod(method);
			return method.getResponseBodyAsString();
		} catch (ConnectTimeoutException cte) {
			logger.error("Get camera parameter: Connect timed out! To:" + ip);
			return "TIMEOUT";
		} catch (HttpException he) {
			logger.error("Get camera parameter: HttpException");
			he.printStackTrace();
			return null;
		} catch (IOException ioe) {
			logger.error("Get camera parameter: IOException");
			return "REFUSED";
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * get all camera's model
	 * @return	List<KeyAndValue>
	 */
	public List<KeyAndValue> getCamModels() {
		String sql = "SELECT MODELID AS KEY, MODEL AS VALUE FROM T_CORE_CAMERA_MODEL WHERE ISDELETE = 'F'";
		return dao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * 验证IP或名称是否存在
	 * @param newIP	新IP
	 * @param newCode	新名称
	 * @return	IP：IP重复；CODE：编号重复；PASS：都不重复；
	 */
	public String checkDuplicate(String camID, String newIP, String newCode) {
		if(Utils.isEmpty(camID)) {
			String sql = "SELECT 1 AS KEY, 1 AS VALUE FROM T_CORE_CAMERA WHERE ISDELETE = 'F' AND IPADDRESS = '" + newIP + "'";
			List<KeyAndValue> list = dao.getKeyAndValueBySQL(sql);
			if(list != null && list.size() != 0) {
				return "IP";
			} else {
				sql = "SELECT 1 AS KEY, 1 AS VALUE FROM T_CORE_CAMERA WHERE ISDELETE = 'F' AND CAMERACODE = '" + newCode + "'";
				list = dao.getKeyAndValueBySQL(sql);
				if(list != null && list.size() != 0) {
					return "CODE";
				} else {
					return "PASS";
				}
			}
		} else {
			String sql = "SELECT 1 AS KEY, 1 AS VALUE FROM T_CORE_CAMERA WHERE ISDELETE = 'F' AND CAMERAID <> " + camID + " AND IPADDRESS = '" + newIP + "'";
			List<KeyAndValue> list = dao.getKeyAndValueBySQL(sql);
			if(list != null && list.size() != 0) {
				return "IP";
			} else {
				sql = "SELECT 1 AS KEY, 1 AS VALUE FROM T_CORE_CAMERA WHERE ISDELETE = 'F' AND CAMERAID <> " + camID + " AND CAMERACODE = '" + newCode + "'";
				list = dao.getKeyAndValueBySQL(sql);
				if(list != null && list.size() != 0) {
					return "CODE";
				} else {
					return "PASS";
				}
			}
		}
	}
	
	/**
	 * Get IP Camera's users
	 * @return	CamUserVO
	 */
	@SuppressWarnings("unchecked")
	public CamUserVO getCamUsers() {
		String sql = "SELECT A.USERNAME AS ADMINNAME, A.USERPASS AS ADMINPASS, " +
								"B.USERNAME AS OPERATORNAME, B.USERPASS AS OPERATORPASS, " +
								"C.USERNAME AS VIEWERNAME, C.USERPASS AS VIEWERPASS " +
								"FROM T_CORE_CAM_USER A, T_CORE_CAM_USER B, T_CORE_CAM_USER C " +
								"WHERE A.AUTHLEVEL = 0 AND B.AUTHLEVEL = 1 AND C.AUTHLEVEL = 2";
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		query.addScalar("adminName", StringType.INSTANCE);
		query.addScalar("adminPass", StringType.INSTANCE);
		query.addScalar("operatorName", StringType.INSTANCE);
		query.addScalar("operatorPass", StringType.INSTANCE);
		query.addScalar("viewerName", StringType.INSTANCE);
		query.addScalar("viewerPass", StringType.INSTANCE);
		query.setResultTransformer(Transformers.aliasToBean(CamUserVO.class));
		query.setCacheable(true);
		CamUserVO camUserVO = ((List<CamUserVO>) query.list()).get(0);
		return camUserVO;
	}
	
	/**
	 * Set IP Camera's users
	 * @param CamUserVO username and password value object
	 */
	public void setCamUsers(CamUserVO vo, String oldAdminPass) {
		CamUser camUser = new CamUser();
		String hql = "From CamUser c Where c.authLevel = ";
		camUser = (CamUser)dao.getSession().createQuery(hql + 0).list().get(0);	//save administrator
		camUser.setUserPass(vo.getAdminPass());
		dao.save(camUser);
		camUser = (CamUser)dao.getSession().createQuery(hql + 1).list().get(0);	//save operator
		camUser.setUserName(vo.getOperatorName());
		camUser.setUserPass(vo.getOperatorPass());
		dao.save(camUser);
		camUser = (CamUser)dao.getSession().createQuery(hql + 2).list().get(0);	//save viewer
		camUser.setUserName(vo.getViewerName());
		camUser.setUserPass(vo.getViewerPass());
		dao.save(camUser);
//		List<KeyAndValue> ipList = getAllCamIP();
//		String paramStr = "system.account.password0=" + vo.getAdminPass() + 
//											  "&system.account.name1=" + vo.getOperatorName() + 
//											  "&system.account.password1=" + vo.getOperatorPass() + 
//											  "&system.account.name2=" + vo.getViewerName() + 
//											  "&system.account.password2=" + vo.getViewerPass();
//		for (int i = 0,j = ipList.size(); i < j; i++) {	//set all camera's authority
//			if(null != setIpParams(ipList.get(i).getKey(), paramStr, "Admin", oldAdminPass)) {	//success
//				setSingleCamUsers(ipList.get(i).getKey(), vo);
//			};
//		}
	}
	
	/**
	 * Get Camera's User By authority level
	 * @param level	authority level	0:admin;	1:operator;	2:viewer
	 * @return	CamUser
	 */
	@SuppressWarnings("unchecked")
	public CamUser getCamUserByAuthLevel(Long level) {
		if(level == null) {
			return null;
		}
		String hql = "From CamUser c Where c.authLevel = " + level;
		List<CamUser> list = (List<CamUser>)dao.getSession().createQuery(hql).list();
		if(list != null && list.size() != 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Get Camera's User By authority level
	 * @param level	authority level	0:admin;	1:operator;	2:viewer
	 * @return	CamUser
	 */
	public KeyAndValue getCamAdminByIP(String camIP) {
		if(camIP == null) {
			return null;
		}
		String sql = "SELECT ADMINNAME AS KEY, ADMINPASS AS VALUE FROM T_CORE_CAMERA WHERE IPADDRESS = '" + camIP + "'";
		List<KeyAndValue> list = dao.getKeyAndValueBySQL(sql);
		if(list != null && list.size() != 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 *  Initial IP Camera
	 * @return
	 */
	public Boolean initIpCam(String camIP, String plateNum, String... nameAndPass) {
		if(Utils.isEmpty(camIP) || Utils.isEmpty(plateNum)) {
			return false;
		}
		String url = "http://" + camIP + "/operator/set_param.cgi?";
		url += "video.osd.control=1&";
		url += "video.osd.dt_control=1&";
		url += "video.osd.text=" + plateNum + "&";
		url += "system.datetime.format=0&";
		url += "system.datetime.timezone=4&";
		url += "system.datetime.method=2&";
		url += "system.datetime.ntpinterval=2&";
		url += "video.channel0.videocodec=0&";
		url += "video.channel1.videocodec=0&";
		url += "video.channel0.fps=10&";
		url += "video.channel1.fps=5&";
		url += "video.channel0.resolution=12&";
		url += "video.channel1.resolution=13&";
		url += "video.image.env=1&";
		url += "video.image.flip=0";	//0 or 2	垂直翻转
//		if(nameAndPass != null && nameAndPass.length == 2) {
//			url += "";
//		}
		logger.info(url);
		HttpClient httpClient = new HttpClient();
		HttpMethod method = new GetMethod(url);
		if(nameAndPass != null && nameAndPass.length == 2) {
			httpClient.getParams().setAuthenticationPreemptive(true);		//抢先认证，只发一次请求
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);	//连接超时时间
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(nameAndPass[0], nameAndPass[1]);	//账号密码证书
			httpClient.getState().setCredentials(AuthScope.ANY, credentials);
			method.setDoAuthentication(true);
		}
		try {
			httpClient.executeMethod(method);
			return !Utils.isEmpty(method.getResponseBodyAsString());
		} catch (ConnectTimeoutException cte) {
			logger.error("Initial camera: Connect timed out! To:" + camIP);
			return false;
		} catch (HttpException he) {
			logger.error("Initial camera: HttpException");
			he.printStackTrace();
			return false;
		} catch (IOException ioe) {
			logger.error("Initial camera: IOException");
			ioe.printStackTrace();
			return false;
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * get all camera's IP which is used
	 * @return IP list
	 */
	public List<KeyAndValue> getAllCamIP() {
		String sql = "SELECT IPADDRESS AS KEY, 1 AS VALUE FROM T_CORE_CAMERA WHERE ISDELETE = 'F'";
		return dao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * set one camera's users
	 */
	public void setSingleCamUsers(String ip, CamUserVO vo) {
		String sql = "UPDATE T_CORE_CAMERA SET ADMINPASS = " + vo.getAdminPass()
				+" AND OPERATORNAME = " + vo.getOperatorName()
				+" AND OPERATORPASS = " + vo.getOperatorPass()
				+" AND VIEWERNAME = " + vo.getViewerName()
				+" AND VIEWERPASS = " + vo.getViewerPass()
				+" WHERE IPADDRESS = " + ip;
		dao.executeUpdate(sql);
	}
	
	/**
	 * Get camera's IP by vehicle's ID
	 * @param vehID		vehicle's ID
	 * @return	IP camera's IP
	 */
	public String getIPByVehID(Long vehID) {
		String sql = "SELECT 1 AS KEY, IPADDRESS AS VALUE FROM T_CORE_CAMERA " +
				"WHERE CAMERAID = (SELECT CAMERAID " +
				"FROM T_CORE_CAMERA_DEVICE " +
				"WHERE CHANNEL = 1 " +
				"AND DEVICEID = (SELECT O_DEVICENO FROM T_CORE_UNITE WHERE O_BUSID = " + vehID + "))";
		List<KeyAndValue> dataList = dao.getKeyAndValueBySQL(sql);
		if(dataList == null || dataList.size() == 0) {
			return null;
		} else {
			return dataList.get(0).getValue();
		}
	}
	
	/**
	 * Upload the video file which saved in IP camera's sd card to the server by video file name
	 * @param camIP		camera's IP
	 * @param videoName		video file's name
	 * @return	action result:	0:	ip is null or video name is null
	 * 												1:	succeed
	 * 												2:	ConnectException
	 * 												3:	IOException
	 */
	public int uploadVideoToServer(String camIP, String date, String time, String second, String... nameAndPass) {
		if(Utils.isEmpty(camIP) || Utils.isEmpty(date) || Utils.isEmpty(time)) {
			return 0;
		}
		second = Utils.isEmpty(second) ? "20" : second;		//default 20 sec
//		String dateStr = videoName.substring(0, 8);
//		String url = "http://" + camIP + "/sd/" + dateStr + "/" + videoName + "_S.avi";
		String url = "http://" + camIP + "/operator/sd_download.cgi?date=" + date + "&time=" + time + "&second=" + second;
		logger.info(url);
		URL connect = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		int len;
		try {
			connect = new URL(url);
			URLConnection connection = connect.openConnection();
			if(nameAndPass != null && nameAndPass.length == 2) {
				connection.setRequestProperty("Authorization", "Basic " + CipherUtil.encodeBase64(nameAndPass[0] + ":" + nameAndPass[1]));	//增加权限设定，否则401无法访问
			}
			connection.connect();
			fos = new FileOutputStream(new File("D://" + date + time + ".avi"));
			bos = new BufferedOutputStream(fos);
			bis = new BufferedInputStream(connection.getInputStream());
			byte[] b = new byte[4096];
			while((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			return 1;
		} catch (ConnectException conne) {
			conne.printStackTrace();
			return 2;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return 3;
		} finally {
			try {
				fos.close();
				bos.close();
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
				return 3;
			}
		}
	}
	
	/**
	 * Get plate number by camera's ID
	 * @return
	 */
	public String getPlateNumByCamID(Long camID) {
		if(Utils.isEmpty(camID)) {
			return null;
		}
		String sql = "SELECT A.LICENSEPLATE AS KEY, 1 AS VALUE FROM T_CORE_VEHICLE A WHERE A.DEVICEID = " +
					"(SELECT B.DEVICEID FROM T_CORE_CAMERA_DEVICE B WHERE B.CAMERAID = " + camID + ")";
		List<KeyAndValue> list = dao.getKeyAndValueBySQL(sql);
		if(list != null && list.size() != 0) {
			return list.get(0).getKey();
		} else {
			return null;
		}
	}
	
	/**
	 * Get IP Camera's info by vehicle's id or device's id
	 * @param vehicleID
	 * @param deviceID
	 * @return	String[]{ip, username, password}
	 */
	public String[] getCamInfoByVehOrDevice(String vehicleID, String deviceID) {
		if(Utils.isEmpty(vehicleID) && Utils.isEmpty(deviceID)) {
			return null;
		}
		String[] resStr = new String[3];
		String sql = "";
		if(!Utils.isEmpty(vehicleID)) {	//车辆条件
			sql = "SELECT IPADDRESS AS KEY, ADMINNAME||'|'||ADMINPASS AS VALUE" +
						" FROM T_CORE_CAMERA WHERE IPADDRESS =" +
						" (SELECT O_LOGINHOST FROM T_CORE_UNITE WHERE O_BUSID = " + vehicleID + ")";
		} else {
			sql = "SELECT IPADDRESS AS KEY, ADMINNAME||'|'||ADMINPASS AS VALUE" +
					" FROM T_CORE_CAMERA WHERE IPADDRESS =" +
					" (SELECT O_LOGINHOST FROM T_CORE_UNITE WHERE O_DEVICENO = " + deviceID + ")";
		}
		List<KeyAndValue> list = dao.getKeyAndValueBySQL(sql);
		if(list != null && list.size() != 0) {
			KeyAndValue keyAndValue = list.get(0);
			resStr[0] = keyAndValue.getKey();
			resStr[1] = keyAndValue.getValue().split("\\|")[0];
			resStr[2] = keyAndValue.getValue().split("\\|")[1];
		}
		return resStr;
	}
	
	/**
	 * 根据车辆ID或者设备ID或者摄像头IP获取车辆名称
	 * @param vehicleID	车辆ID
	 * @param deviceID	设备ID
	 * @param camIP		摄像头IP
	 * @return	String 车辆名称
	 */
	public String getVehNameByVehIDOrDeviceIDOrCamIP(String vehicleID, String deviceID, String camIP) {
		if(Utils.isEmpty(vehicleID) && Utils.isEmpty(deviceID) && Utils.isEmpty(camIP)) {
			return null;
		}
		String sql = "SELECT O_BUSNAME AS KEY, 1 AS VALUE FROM T_CORE_UNITE WHERE ";
		if(!Utils.isEmpty(vehicleID)) {
			sql += "O_BUSID = " + vehicleID;
		} else if(!Utils.isEmpty(deviceID)) {
			sql += "O_DEVICENO = " + deviceID;
		} else if(!Utils.isEmpty(camIP)) {
			sql += "O_LOGINHOST = '" + camIP+"'";
		}
		List<KeyAndValue> vehNameList = dao.getKeyAndValueBySQL(sql);
		if(vehNameList != null && vehNameList.size() != 0) {
			KeyAndValue keyAndValue = vehNameList.get(0);
			return keyAndValue.getKey();
		} else {
			return null;
		}
	}
	
	/**
	 * 获取摄像头时间
	 * @param camIP	摄像头IP
	 * @param nameAndPass	身份认证需要的账号密码
	 * @return	日期(换行)时间(system.datetime.date=2013/9/10
	 * 										   		 system.datetime.time=19:44:39)
	 */
	public String getCamTime(String camIP, String... nameAndPass) {
		String res = getIpParam(camIP, "system.datetime.date&system.datetime.time", nameAndPass);
		if(res == null) {
			return "Connection error!";
		} else if(res.equals("TIMEOUT")) {
			return "Connection timeout!";
		} else if(res.equals("REFUSED")) {
			return "Connection refused!";
		} else if(res.contains("401 Unauthorized")) {
			return "Unauthorized!";
		}
		String resDate = Utils.getMatcher("system.datetime.date=(.*)", res);
		String resTime = Utils.getMatcher("system.datetime.time=(.*)", res);
		String[] yyyymmdd = resDate.split("/");	//日期补0
		for (int i = 1,j = yyyymmdd.length; i < j; i++) {
			if(yyyymmdd[i].length() < 2) {
				yyyymmdd[i] = "0" + yyyymmdd[i];
			}
		}
		resDate = yyyymmdd[0] + "/" + yyyymmdd[1] + "/" + yyyymmdd[2];
		String[] hhmmss = resTime.split(":");		//时间补0
		for (int i = 0,j = hhmmss.length; i < j; i++) {
			if(hhmmss[i].length() < 2) {
				hhmmss[i] = "0" + hhmmss[i];
			}
		}
		resTime = hhmmss[0] + ":" + hhmmss[1] + ":" + hhmmss[2];
		return resDate + " " + resTime;
	}
	
	/**
	 * 检查摄像头时间与服务器时间差是否合法
	 * @param camTime	摄像头时间
	 * @return true：合法；false：不合法
	 */
	public boolean checkCamTime(String camTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		try {
			if(Math.abs(calendar.getTimeInMillis() - format.parse(camTime).getTime()) > 120000) {
				return false;
			} else {
				return true;
			}
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * 获得列表 通过时间和日期
	 */
	public String getvideolist2(String ip, String date, String time, String name, String pwd,String port) {
		String rstr="false";
		String url="http://"+ip+":"+port+"/cgi-bin/storage.cgi?xml&trigger_type=0&from="+date+"%20"+DateUtil.toHHmmssPlus2(-300, date+" "+time)+"&to="+date+"%20"+DateUtil.toHHmmssPlus2(0, date+" "+time)+"&file_type=0";
		try {
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(name, pwd);	//账号密码证书
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setAuthenticationPreemptive(true);		//抢先认证，只发一次请求
			httpClient.getState().setCredentials(AuthScope.ANY, credentials);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);	//连接超时时间
			HttpMethod method = new GetMethod(url);
			method.setDoAuthentication(true);
			httpClient.executeMethod(method);
			rstr=method.getResponseBodyAsString();
			method.releaseConnection();
		} catch (Exception e) {
			return rstr;
		}
		
		return rstr;
	}

	public List<CamDevice> getCD(Long id) {
		Criteria criteria=dao.getSession().createCriteria(CamDevice.class);
		criteria.add(Restrictions.eq("camID", id));
		List<CamDevice> cds=dao.getAll(criteria);
		return cds;
	}

	@SuppressWarnings("unchecked")
	public InputStream getExcel(String title) {
		List<Camera> data = new ArrayList<Camera>();
		Page<Camera> page_export = (Page<Camera>) Utils.getSession().getAttribute("page_export");
		DetachedCriteria criteria_export = (DetachedCriteria) Utils.getSession().getAttribute("criteria_export");
		List<Order> orderList_export = (List<Order>) Utils.getSession().getAttribute("orderList_export");
		page_export.setPageSize(page_export.getTotalCount());
		Page<Camera> pageResult = dao.getBatch(page_export,
				criteria_export.getExecutableCriteria(dao.getSession()),
				orderList_export);
		if (null != pageResult) {
			if (0 != pageResult.getResult().size()) {
				data = pageResult.getResult();

			}
		}
		ExportExcel ee = new ExportExcel() {
			
			@Override
			protected HSSFWorkbook disposeData(HSSFWorkbook wb, Object[] total,
					List<?> data) throws IOException {
				HSSFSheet sheet = wb.getSheetAt(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
				HSSFRow rowss = sheet.createRow(0);
				rowss.setHeightInPoints(20);
				HSSFCell hssfCell = rowss.createCell(0);
				hssfCell = this.createCell(wb, hssfCell, total[0].toString());
				HSSFCellStyle style = this.createStyle(wb);
				HSSFRow row2 = sheet.createRow(1);
				HSSFCell cellrow01 = row2.createCell(0);
				cellrow01.setCellStyle(style);
				HSSFCell cellrow02 = row2.createCell(1);
				cellrow02.setCellStyle(style);
				HSSFCell cellrow03 = row2.createCell(2);
				cellrow03.setCellStyle(style);
				HSSFCell cellrow04 = row2.createCell(3);
				cellrow04.setCellStyle(style);
				HSSFCell cellrow05 = row2.createCell(4);
				cellrow05.setCellStyle(style);
				HSSFCell cellrow06 = row2.createCell(5);
				cellrow06.setCellStyle(style);
				HSSFCell cellrow07 = row2.createCell(6);
				cellrow07.setCellStyle(style);
				
				cellrow01.setCellValue("No.");
				cellrow02.setCellValue("Camera Name");
				cellrow03.setCellValue("Department");
				cellrow04.setCellValue("Camera Model");
				cellrow05.setCellValue("IP");
				cellrow06.setCellValue("Login Username");
				cellrow07.setCellValue("Login Password");
				
				
				if (null != data && 0 != data.size()) {
					
					for (int i = 0; i < data.size(); i++) {
						Object[] o=(Object[])data.get(i);
						CamModel cm=(CamModel)o[0];
						Camera a=(Camera)o[1];
						
						
						HSSFRow row = sheet.createRow(i + 2);
						HSSFCell cell001 = row.createCell(0);
						cell001.setCellStyle(style);
						cell001.setCellValue(i + 1);
						HSSFCell cell002 = row.createCell(1);
						cell002.setCellStyle(style);
						cell002.setCellValue(a.getCameraName());
						HSSFCell cell003 = row.createCell(2);
						cell003.setCellStyle(style);
						cell003.setCellValue(a.getDeptname());
						HSSFCell cell004 = row.createCell(3);
						cell004.setCellStyle(style);
						cell004.setCellValue(cm.getModelName());
						
						HSSFCell cell005 = row.createCell(4);
						cell005.setCellStyle(style);
						cell005.setCellValue(a.getIpAddress());
						
						HSSFCell cell006 = row.createCell(5);
						cell006.setCellStyle(style);
						cell006.setCellValue(a.getAdminName());
						HSSFCell cell007 = row.createCell(6);
						cell007.setCellStyle(style);
						cell007.setCellValue(a.getAdminPass());
						
						
					}
				}
				
				
				return wb;
			}
		};
		
		
		Object[] total = new Object[1];
		total[0] = title;
		String str = title;
		return ee.export(total, data, str);
		}
	
	/**
	 * check if camera name is duplicate 
	 * @return true duplicate; false not duplicate
	 */
	@SuppressWarnings("rawtypes")
	public Boolean isDuplicate(Long deptID, String camID, String camName) {
		String sql = "";
		if(Utils.isEmpty(camID)) {
			sql = "SELECT 1 FROM T_CORE_CAMERA WHERE ISDELETE='F' AND DEPTID = " + deptID + " AND CAMERANAME='" + camName + "'";
		} else {
			sql = "SELECT 1 FROM T_CORE_CAMERA WHERE ISDELETE='F' AND CAMERAID <> " + camID + " AND DEPTID = " + deptID + " AND CAMERANAME = '" + camName + "'";
		}
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		List list = query.list();
		if(list == null || list.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
}