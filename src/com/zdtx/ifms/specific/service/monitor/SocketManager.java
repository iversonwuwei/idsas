/**
 * @File: SocketManager.java
 * @path: idsas - com.zdtx.ifms.specific.service.monitor
 */
package com.zdtx.ifms.specific.service.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;

/**
 * @ClassName: SocketManager
 * @Description: 远程录像相关业务帮助类
 * @author: Leon Liu
 * @date: 2013-5-27 下午2:02:54
 * @version V1.0
 */
@Service
@Transactional
public class SocketManager {
	
	@Autowired
	private BaseDao dao;
	
	private Socket socket;
	private OutputStream os;
	private InputStream is;
	private String timestamp;
	private String userName = "";
	private String password = "";
	
	/**
	 * 验证车辆是否存在及是否在线
	 * @param 		userName
	 * @param 		videoID
	 * @return 		0：车辆在线；
	 * 						1：车辆不存在；
	 * 						2：车辆不在线；
	 * 						3：车辆响应超时；
	 * 						8：无录像文件；
	 * 						100：IO异常；
	 * 						101：未知端口；
	 * 						103：身份验证失败
	 */
	public int checkVehIsOnLine(String userName, String password, String videoID) {
		try {
			prepareSocket(userName, password);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			closeSocket();
			return 101;
		} catch (IOException e) {
			e.printStackTrace();
			closeSocket();
			return 100;
		}
		String checkRes = "";
		try {
			if(getLogin()) {																				//身份认证
				checkRes = searchVideo(userName, videoID, "000000", "000000", "000000", "1");	//查询车辆是否存在以及是否在线
				if(!checkRes.contains(";")) {
					Integer resNum = Integer.valueOf(checkRes);
					if(resNum <= 3 && resNum > 0) {	//车辆不存在、不在线、超时均关闭连接
						closeSocket();
					}
					return Integer.valueOf(checkRes);
				} else {
					return 0;
				}
			} else {
				closeSocket();
				return 103;																				//身份认证失败返回3
			}
		} catch (IOException e) {
			closeSocket();
			e.printStackTrace();
			return 100;																					//IO错误返回10
		}
	}
	
	/**
	 * 验证视频是否存在
	 * @param 		userName	用户登录名
	 * @param 		videoID		视频设备编号
	 * @param 		videoDate 视频日期
	 * @param 		beginTime	开始时间
	 * @param 		endTime		结束时间
	 * @return 		video list		视频文件列表
	 */
	public List<KeyAndValue> checkVideoExist(String userName, String videoID, String channelID, String videoDate, String beginTime, String endTime) {
		List<KeyAndValue> resList = new ArrayList<KeyAndValue>();
		String checkRes = "";
		try {
			checkRes = searchVideo(userName, videoID, videoDate, beginTime, endTime, channelID);	//查询车辆是否存在以及是否在线
			if(!checkRes.contains(";")) {
				Integer errorCode = Integer.valueOf(checkRes);
				if(errorCode == 1) {
					resList.add(new KeyAndValue("error", "The vehicle you selected does not exist!"));
				} else if(errorCode == 2) {
					resList.add(new KeyAndValue("error", "The vehicle you selected is not online!"));
				} else if(errorCode == 3) {
					resList.add(new KeyAndValue("error", "Vehicle response has timed out!"));
				} else if(errorCode == 8) {
					resList.add(new KeyAndValue("error", "The vehicle you selected does not have any video file!"));
				} else if(errorCode == 65561) {
					resList.add(new KeyAndValue("error", "Channel number error!"));
				} else if(errorCode == 131091) {
					resList.add(new KeyAndValue("error", "Can not search for video files!"));
				}
				closeSocket();
			} else {
				String[] videoArr = checkRes.split(";")[1].split(":");	//video list
				for (int i = 0; i < videoArr.length; i++) {
					resList.add(new KeyAndValue(i+1 + "", videoArr[i]));
				}
			}
		} catch (IOException e) {
			closeSocket();
			return null;																					//IO错误返回null
		}
		return resList;
	}
	
	/**
	 * 获得应答结果
	 * @return 应答结果字符串
	 * @throws IOException
	 */
	private String getSocketRes() throws IOException {
		int i = 0;
		while(true) {														//轮询应答
			i = is.available();
			if(i > 0) {															//有应答跳出
				break;
			}
		}
		byte[] b = new byte[is.available()];
		is.read(b);															//读取内容
		return new String(b, "GB2312");				//转编码返回
	}
	
	/**
	 * 准备socket对象，建立连接，初始化输入输出流对象
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private void prepareSocket(String userName, String password) throws UnknownHostException, IOException {
		this.userName = userName;
		this.password = password;
		socket = new Socket(InetAddress.getByName("192.168.18.206"), 16006);
		os = socket.getOutputStream();
		is = socket.getInputStream();
	}
	
	public void closeSocket() {
		try {
			if(socket != null) {
				os.close();
				is.close();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 身份认证
	 * @return	true：通过；false：不通过
	 * @throws IOException
	 */
	private boolean getLogin() throws IOException {
		String loginStr = "$$|A0|" + userName + "|" + password + "|##";
		os.write(loginStr.getBytes());
		os.flush();
		String resStr = getSocketRes();								//获取身份认证应答，通过： $$|B0|||1|##；不通过：$$|B0|ERROR|身份验证失败|0|##
		return resStr.split("\\|")[4].equals("1");				//根据固定位置值返回结果
	}
	
	/**
	 * 搜索录像状态信息
	 * @param userName 用户ID
	 * @param videoID 	视频设备编号
	 * @param date 	视频日期	yyMMdd
	 * @param beginTime 	视频开始时间	HHmmss
	 * @param endTime 	视频结束时间		HHmmss
	 * @return success 文件总个数;文件1名称:文件2名称:文件n名称:
	 * 					error	1	车辆不存在
										2	车辆不在线
										3	车辆响应超时
										8	无录像文件
										65561	通道号错误
										131091	不能搜索录像文件
	 * @throws IOException
	 */
	public String searchVideo(String userName, String videoID, String date, String beginTime, String endTime, String channel) throws IOException {
		timestamp = new SimpleDateFormat("yyyyMMdd").format(new Date());	//时间戳
		String mess = "$$|66|" + timestamp + "|" + userName + "|110|" + videoID + "," + date + " " + beginTime + "," + endTime + ",3," + channel + ",|##";
		os.write(mess.getBytes());	//发送查询串
		os.flush();
		String resStr = getSocketRes();				//$$|66|1369642315|lh|110|65535,2,|##
		String res = resStr.split("\\|")[5];			//65535,2,
		String flag = res.split(",")[1];					//2
		if(!flag.equals("0")) {									//错误状态
			return flag;													//返回错误代码：1	车辆不存在；2	车辆不在线；3	车辆响应超时；8	无录像文件；65561	通道号错误；131091	不能搜索录像文件；
		} else {
			return res.split(",")[2];							//成功状态，返回：文件总个数;文件1名称:文件2名称:文件n名称:
		}
	}
	
	/**
	 * 下载录像到服务器
	 * @return success 文件存储路径
	 * 					error	4	车辆不在线
	 * 								5	车辆不存在
	 * 								6	车辆响应超时
										7	文件不存在
										131096	下载任务上限
										131090	文件为正在录像文件
										131095	下载文件任务号获取失败
	 */
	public String downloadServer(String userName, String videoID, String fileName) {
		timestamp = new SimpleDateFormat("yyyymmdd").format(new Date());	//时间戳
		String resStr = "";
		String dl_status = "";
		String mess = "$$|66|" + timestamp + "|" + userName + "|114|" + videoID + ",0,,,,," + fileName + "|##";
		try {
			os.write(mess.getBytes());
			os.flush();
			while(true) {
				resStr = getSocketRes();
				dl_status = resStr.split("\\|")[5].split(",")[2];
				if(!dl_status.equals("1") && !dl_status.equals("2")) {
					break;
				}
			}
			if(!dl_status.equals("3")) {									//错误状态
				return dl_status;													//返回错误代码：1	车辆不存在；2	车辆不在线；3	车辆响应超时；8	无录像文件；65561	通道号错误；131091	不能搜索录像文件；
			} else {
				return resStr.split("\\|")[5].split(",")[3];							//成功状态，返回：文件总个数;文件1名称:文件2名称:文件n名称:
			}
		} catch (IOException e) {
			closeSocket();
			return "4";
		}
	}
	
	/**
	 * 保存socket接收的超速数据
	 * @param dataArr 超速数据	$$|C9|[车辆所属]|[车辆名称]|[GPS数据产生日期]
	 * 													|[GPS数据产生时间]|[纬度]|[经度]|[速度]|[方位角]
														|[车辆运行标识]|[标准速度]|[标识]|[超速时长]|[前一站点序号]|##
	 * @return Integer 0:成功；1:存储过程执行失败；2:被条件过滤未执行存储过程；null:程序出现异常
	 */
	public Integer saveOverSpeed(String[] dataArr) {
		Connection conn = null;
		CallableStatement statement = null;
		try {
//			conn = SessionFactoryUtils.getDataSource(dao.getSessionFactory()).getConnection();
//			statement = conn.prepareCall("{CALL UP_JK.P_JK_OVERSPEED(?,?,?,?,?, ?,?,?,?,?, ?)}");
//			statement.setLong(1, Utils.isEmpty(dataArr[12]) ? 0L : Long.valueOf(dataArr[12]));	//IN_FLAG
//			statement.setLong(2, 0L);	//IN_HISTORYDATA(暂时都设0)
//			statement.setLong(3, Utils.isEmpty(dataArr[13]) ? 0L : Long.valueOf(dataArr[13]));	//IN_TIMETAKEN
//			statement.setString(4, Utils.isEmpty(dataArr[3]) ? "" : dataArr[3]);	//IN_BUSNAME
//			statement.setString(5, Utils.isEmpty(dataArr[4]) ? "" : dataArr[4]);	//IN_DATE
//			statement.setString(6, Utils.isEmpty(dataArr[5]) ? "" : dataArr[5]);	//IN_TIME
//			statement.setDouble(7, Utils.isEmpty(dataArr[7]) ? 0D : Double.valueOf(dataArr[7]));	//IN_LONGITUDE
//			statement.setDouble(8, Utils.isEmpty(dataArr[6]) ? 0D : Double.valueOf(dataArr[6]));	//IN_LATITUDE
//			statement.setLong(9, Utils.isEmpty(dataArr[14]) ? 0L : Long.valueOf(dataArr[14]));	//IN_MAXSPEED(暂时用前一站点序号)
//			statement.setLong(10, Utils.isEmpty(dataArr[11]) ? 0L : Long.valueOf(dataArr[11]));	//IN_NORMALSPEED
//			statement.registerOutParameter(11, Types.INTEGER);	//OUT_RET
//			statement.execute();
//			Integer res = statement.getInt(11);
//			return res;
			//存储过程中的过滤条件
			int flag = Utils.isEmpty(dataArr[12]) ? 0 : Integer.valueOf(dataArr[12]);
			int timeTaken = Utils.isEmpty(dataArr[13]) ? 0 : Integer.valueOf(dataArr[13]);
			if(flag == 0 && timeTaken >= 15) {		//超速操过15秒才统计
				conn = SessionFactoryUtils.getDataSource(dao.getSessionFactory()).getConnection();
				statement = conn.prepareCall("{CALL UP_VEH_BEH_NEW.P_BEH_OVERSPEED(?,?,?,?,?, ?,?,?,?,?)}");
				statement.setString(1, Utils.isEmpty(dataArr[3]) ? "" : dataArr[3]);	//IN_BUSNAME
				statement.setString(2, Utils.isEmpty(dataArr[4]) ? "" : dataArr[4]);	//IN_DATE
				statement.setString(3, Utils.isEmpty(dataArr[5]) ? "" : dataArr[5]);	//IN_TIME
				statement.setDouble(4, Utils.isEmpty(dataArr[7]) ? 0D : Double.valueOf(dataArr[7]));	//IN_LONGITUDE
				statement.setDouble(5, Utils.isEmpty(dataArr[6]) ? 0D : Double.valueOf(dataArr[6]));	//IN_LATITUDE
				statement.setInt(6, flag);	//IN_FLAG
				statement.setInt(7, timeTaken);	//IN_TIMETAKEN
				statement.setLong(8, Utils.isEmpty(dataArr[14]) ? 0L : Long.valueOf(dataArr[14]));	//IN_MAXSPEED(暂时用前一站点序号)
				statement.setLong(9, Utils.isEmpty(dataArr[11]) ? 0L : Long.valueOf(dataArr[11]));	//IN_NORMALSPEED
				statement.registerOutParameter(10, Types.INTEGER);	//OUT_RET
				statement.execute();
				Integer res = statement.getInt(10);
				return res;
			} else {
				return 2;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(statement != null) {
					statement.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * 保存socket接收的急加速急减速数据
	 * @param dataStr 急加速急减速数据	$$|CA|[车辆所属]|[车辆名称]|[GPS数据产生日期]|[GPS数据产生时间]
	 * 																|[纬度]|[经度]|[速度]|[方位角]|[车辆运行标识]
	 * 																|[制动速度]|[标识]|[时长]|[前一站点序号]|##
	 * @return Integer 0:成功；1:存储过程执行失败；2:被条件过滤未执行存储过程；null:程序出现异常
	 */
	public Integer saveSpeedUpDown(String[] dataStr) {
		Connection conn = null;
		CallableStatement statement = null;
		try {
//			conn = SessionFactoryUtils.getDataSource(dao.getSessionFactory()).getConnection();
//			statement = conn.prepareCall("{CALL UP_JK.P_JK_SPEEDUPDOWN(?,?,?,?,?, ?,?,?,?,?, ?)}");
//			statement.setLong(1, Utils.isEmpty(dataStr[12]) ? 0L : Long.valueOf(dataStr[12]));	//IN_FLAG
//			statement.setLong(2, Utils.isEmpty(dataStr[10]) ? 0L : Long.valueOf(dataStr[10]));	//IN_RUNSTATUS
//			statement.setString(3, Utils.isEmpty(dataStr[3]) ? "" : dataStr[3]);	//IN_BUSNAME
//			statement.setString(4, Utils.isEmpty(dataStr[4]) ? "" : dataStr[4]);	//IN_DATE
//			statement.setString(5, Utils.isEmpty(dataStr[5]) ? "" : dataStr[5]);	//IN_TIME
//			statement.setDouble(6, Utils.isEmpty(dataStr[7]) ? 0D : Double.valueOf(dataStr[7]));	//IN_LONGITUDE
//			statement.setDouble(7, Utils.isEmpty(dataStr[6]) ? 0D : Double.valueOf(dataStr[6]));	//IN_LATITUDE
//			statement.setLong(8, Utils.isEmpty(dataStr[13]) ? 0L : Long.valueOf(dataStr[13]));	//IN_TIMETAKEN
//			statement.setLong(9, Utils.isEmpty(dataStr[8]) ? 0L : Long.valueOf(dataStr[8]));	//IN_SPEED
//			statement.setLong(10, Utils.isEmpty(dataStr[11]) ? 0L : Long.valueOf(dataStr[11]));	//IN_UPDOWNSPEED
//			statement.registerOutParameter(11, Types.INTEGER);	//OUT_RET
//			statement.execute();
//			Integer res = statement.getInt(11);
//			return res;
			//存储过程中的过滤条件
			int flag = Utils.isEmpty(dataStr[12]) ? 0 : Integer.valueOf(dataStr[12]);
			int runStatus = Utils.isEmpty(dataStr[10]) ? 0 : Integer.valueOf(dataStr[10]);
			if (flag == 0 && (runStatus == 1 || runStatus == 2)) {
				conn = SessionFactoryUtils.getDataSource(dao.getSessionFactory()).getConnection();
				statement = conn.prepareCall("{CALL  UP_VEH_BEH_NEW.P_BEH_SPEEDUPDOWN(?,?,?,?,?, ?,?,?,?,?, ?)}");
				statement.setString(1, Utils.isEmpty(dataStr[3]) ? "" : dataStr[3]);	//IN_BUSNAME
				statement.setString(2, Utils.isEmpty(dataStr[4]) ? "" : dataStr[4]);	//IN_DATE
				statement.setString(3, Utils.isEmpty(dataStr[5]) ? "" : dataStr[5]);	//IN_TIME
				statement.setDouble(4, Utils.isEmpty(dataStr[7]) ? 0D : Double.valueOf(dataStr[7]));	//IN_LONGITUDE
				statement.setDouble(5, Utils.isEmpty(dataStr[6]) ? 0D : Double.valueOf(dataStr[6]));	//IN_LATITUDE
				statement.setInt(6, flag);	//IN_FLAG
				statement.setLong(7, Utils.isEmpty(dataStr[13]) ? 0L : Long.valueOf(dataStr[13]));	//IN_TIMETAKEN
				statement.setLong(8, Utils.isEmpty(dataStr[8]) ? 0L : Long.valueOf(dataStr[8]));	//IN_SPEED
				statement.setLong(9, Utils.isEmpty(dataStr[11]) ? 0L : Long.valueOf(dataStr[11]));	//IN_UPDOWNSPEED
				statement.setInt(10, runStatus);	//IN_RUNSTATUS
				statement.registerOutParameter(11, Types.INTEGER);	//OUT_RET
				statement.execute();
				Integer res = statement.getInt(11);
				return res;
			} else {
				return 2;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(statement != null) {
					statement.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * 保存socket接收的其他告警数据
	 * @param dataStr 其他告警数据	$$|FD|[告警类型]|[车号]|[GPS数据产生日期]|[GPS数据产生时间]
	 * 														|[纬度]|[经度]|[速度]|[方位角]|[告警内容]|##
	 * @return Integer 0:成功；1:存储过程执行失败；2:被条件过滤未执行存储过程；null:程序出现异常
	 */
	public Integer saveWarningInfo(String[] dataStr) {
		Connection conn = null;
		CallableStatement statement = null;
		try {
			String warnInfo = dataStr[10];
			String[] infoArr = warnInfo.split(",");
			int flag = 0;
			Long timeTaken = 0L;
			Long maxValue = 0L;
			Long standard = 0L;
			if(dataStr[2].equals("247")) {	//空挡滑行, 告警内容：持续时间,滑行最大速度,标识,
				flag = Utils.isEmpty(infoArr[2]) ? 0 : Integer.valueOf(infoArr[2]);
				timeTaken = Utils.isEmpty(infoArr[0]) ? 0L : Long.valueOf(infoArr[0]);
				maxValue = Utils.isEmpty(infoArr[1]) ? 0L : Long.valueOf(infoArr[1]);
			} else if(dataStr[2].equals("248")) {	//超长怠速, 告警内容：怠速持续时间,持续怠速限定值,标识,
				flag = Utils.isEmpty(infoArr[2]) ? 0 : Integer.valueOf(infoArr[2]);
				timeTaken = Utils.isEmpty(infoArr[0]) ? 0L : Long.valueOf(infoArr[0]);
				standard = Utils.isEmpty(infoArr[1]) ? 0L : Long.valueOf(infoArr[1]);
			} else if(dataStr[2].equals("250")) {	//急左转, 告警内容：方向盘角度,横向加速度,横向加速度最大值,(实际数据只有方向盘角度1个参数，flag都取1)
				flag = 1;
				maxValue =  Utils.isEmpty(infoArr[0]) ? 0L : Long.valueOf(infoArr[0]);
			} else if(dataStr[2].equals("251")) {	//急右转, 告警内容：方向盘角度,横向加速度,横向加速度最大值,(实际数据只有方向盘角度1个参数，flag都取1)
				flag = 1;
				maxValue =  Utils.isEmpty(infoArr[0]) ? 0L : Long.valueOf(infoArr[0]);
			} else if(dataStr[2].equals("253")) {	//超转, 告警内容：转速,转速最大值,持续时间,标识,
				flag = Utils.isEmpty(infoArr[3]) ? 0 : Integer.valueOf(infoArr[3]);
				timeTaken =Utils.isEmpty(infoArr[2]) ? 0L : Long.valueOf(infoArr[2]);
				maxValue = Utils.isEmpty(infoArr[0]) ? 0L : Long.valueOf(infoArr[0]);
				standard = Utils.isEmpty(infoArr[1]) ? 0L : Long.valueOf(infoArr[1]);
			}
//				conn = SessionFactoryUtils.getDataSource(dao.getSessionFactory()).getConnection();
//				statement = conn.prepareCall("{CALL UP_JK.P_JK_WARNINGINF(?,?,?,?,?, ?,?,?,?,?, ?,?)}");
//				statement.setLong(1, Utils.isEmpty(flag) ? 0L : Long.valueOf(flag));		//IN_FLAG
//				statement.setLong(2, Utils.isEmpty(dataStr[2]) ? 0L : Long.valueOf(dataStr[2]));	//IN_WARTYPE
//				statement.setString(3, Utils.isEmpty(dataStr[3]) ? "" : dataStr[3]);	//IN_BUSNAME
//				statement.setString(4, Utils.isEmpty(dataStr[4]) ? "" : dataStr[4]);	//IN_DATE
//				statement.setString(5, Utils.isEmpty(dataStr[5]) ? "" : dataStr[5]);	//IN_TIME
//				statement.setDouble(6, Utils.isEmpty(dataStr[7]) ? 0D : Double.valueOf(dataStr[7]));	//IN_LONGITUDE
//				statement.setDouble(7, Utils.isEmpty(dataStr[6]) ? 0D : Double.valueOf(dataStr[6]));	//IN_LATITUDE
//				statement.setLong(8, Utils.isEmpty(timeTaken) ? 0L : Long.valueOf(timeTaken));	//IN_TIMETAKEN
//				statement.setLong(9, Utils.isEmpty(maxValue) ? 0L : Long.valueOf(maxValue));	//IN_MAXVALUE
//				statement.setLong(10, Utils.isEmpty(dataStr[8]) ? 0L : Long.valueOf(dataStr[8]));	//IN_SPEED
//				statement.setLong(11, Utils.isEmpty(standard) ? 0L : Long.valueOf(standard));	//IN_STANDARD
//				statement.registerOutParameter(12, Types.INTEGER);	//OUT_RET
//				statement.execute();
//				Integer res = statement.getInt(12);
//				return res;
			//存储过程中的过滤条件
			int wartype = Integer.valueOf(dataStr[2]);
			if (wartype == 253 && timeTaken > 1800) {	//超转 时间超过30分钟
				return 2;
			}
			int speed = Integer.valueOf(dataStr[8]);
			boolean condition1 = (flag == 0 && (wartype == 253 || wartype == 247 || wartype == 248 || wartype == 249));
			boolean condition2 = (flag == 1 && (wartype == 250 || wartype == 251) && speed >= 30 && maxValue >= 40);
			if (condition1 || condition2) {
				conn = SessionFactoryUtils.getDataSource(dao.getSessionFactory()).getConnection();
				statement = conn.prepareCall("{CALL UP_VEH_BEH_NEW.P_BEH_WARNINGINF(?,?,?,?,?, ?,?,?,?,?, ?)}");
				statement.setString(1, Utils.isEmpty(dataStr[3]) ? "" : dataStr[3]);	//o_busname
				statement.setString(2, Utils.isEmpty(dataStr[4]) ? "" : dataStr[4]);	//o_date
				statement.setString(3, Utils.isEmpty(dataStr[5]) ? "" : dataStr[5]);	//o_time
				statement.setDouble(4, Utils.isEmpty(dataStr[7]) ? 0D : Double.valueOf(dataStr[7]));	//o_longitude
				statement.setDouble(5, Utils.isEmpty(dataStr[6]) ? 0D : Double.valueOf(dataStr[6]));	//o_latitude
				statement.setInt(6, flag);		//o_flag
				statement.setLong(7, timeTaken);	//o_timetaken
				statement.setLong(8, standard);	//o_standard
				statement.setLong(9, maxValue);	//o_maxvalue
				statement.setLong(10, wartype);	//o_wartype
				statement.registerOutParameter(11, Types.INTEGER);	//OUT_RET
				statement.execute();
				Integer res = statement.getInt(11);
				return res;
			} else {
				return 2;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(statement != null) {
					statement.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}