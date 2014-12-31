package com.zdtx.ifms.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class SocketUtil {
	
	public static String SOCKET_IP = null;
	public static Integer SOCKET_PORT = null;
	public static Integer SOCKET_TIMEOUT = null;
	public static Integer SOCKET_LOGIN_TIMEOUT = null;
	public static String SOCKET_GPS_ISOPEN = null;
	public static String SOCKET_GPS_NAME = null;
	public static String SOCKET_GPS_PWD = null;
	public static String SOCKET_BASE_ISOPEN = null;
	public static String SOCKET_BASE_NAME = null;
	public static String SOCKET_BASE_PWD = null;
	public static String SOCKET_CARD_ISOPEN = null;
	public static Integer SOCKET_CARD_PORT = null;
	private static final String SOCKET_LOG_PATH = System.getProperty("catalina.home") + "/logs";
	
	/**
	 * 初始化获取配置文件application.properties内参数
	 */
	static {
		InputStream inputStream = SocketUtil.class.getClassLoader().getResourceAsStream("application.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
			SOCKET_IP = properties.getProperty("socket.ip").trim();
			SOCKET_PORT = Integer.valueOf(properties.getProperty("socket.port").trim());
			SOCKET_TIMEOUT = Integer.valueOf(properties.getProperty("socket.time_out").trim());
			SOCKET_LOGIN_TIMEOUT = Integer.valueOf(properties.getProperty("socket.login_time_out").trim());
			SOCKET_GPS_ISOPEN = properties.getProperty("socket.gps.isopen").trim();
			SOCKET_GPS_NAME = properties.getProperty("socket.gps.username").trim();
			SOCKET_GPS_PWD = properties.getProperty("socket.gps.password").trim();
			SOCKET_BASE_ISOPEN = properties.getProperty("socket.base.isopen").trim();
			SOCKET_BASE_NAME = properties.getProperty("socket.base.username").trim();
			SOCKET_BASE_PWD = properties.getProperty("socket.base.password").trim();
			SOCKET_CARD_ISOPEN = properties.getProperty("socket.card.isopen").trim();
			SOCKET_CARD_PORT = Integer.valueOf(properties.getProperty("socket.card.port"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static String LOGIN_STRING = SocketUtil.getLoginStr(SocketUtil.SOCKET_BASE_NAME, SocketUtil.SOCKET_BASE_PWD);
	
	/**
	 * 保存日志到tomcat根目录下文件socket-logs.txt中
	 * @param logStr		日志内容
	 */
	public static void saveLog(String logStr) {
		File dir = new File(SOCKET_LOG_PATH);
		if(!dir.isDirectory()){
			dir.mkdir();
		}
		File file = new File(SOCKET_LOG_PATH + "/socket-logs.txt");
		if (!file.isFile()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		PrintWriter printWriter = null;
		FileWriter fileWriter = null;  
		try {
			fileWriter = new FileWriter(file, true);
			printWriter = new PrintWriter(fileWriter, true);
			printWriter.println(logStr);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(printWriter != null) {
				printWriter.close();
			}
		}
	}
	
	/***
	 * 创建登陆信息请求字符串
	 * @param username
	 * @param password
	 * @return $$|A0|system_admin|system_admin|##
	 */
	public static String getLoginStr(String username, String password) {
		return "$$|A0|" + username + "|" + password + "|##";
	}
	
	/***
	 * 创建订阅围栏信息字符串
	 * 
	 * @param username
	 * @param password
	 * @return $$|A0|system_admin|system_admin|##
	 */
	public static String getFenceStr(String username) {
		return "$$|A6|" + username + "|123456|1|1,|##";
	}
	
	/***
	 * 创建清除故障码
	 * $$|AB|8|[车号]|[发送给OBD的内容]|##
	 * $$|AB|8|955555|001430340D|##
	 * @param vehname [车号]
	 * @param obdStr  [发送给OBD的内容]
	 * @return $$|A0|system_admin|system_admin|##
	 */
	public static String getFaultCodeStr(String vehName, String obdStr) {
		return "$$|AB|8|" + vehName + "|" + obdStr + "|##";
	}
	
	
	/***
	 * 终端设置参数
	 * $$|B5|[时间戳]|[车辆名称]|[设置参数列表]|##
	 * @return
	 */
	public static String setThresholdStr(String timestamp, String vehName, String params) {
		return "$$|B5|" + timestamp + "|" + vehName + "|" + params + "|##";
	}
	
	/***
	 * 创建设置参数列表
	 * @param name
	 * @param startvalue
	 * @param endvalue
	 * @param usetime
	 * @return 指令字符串
	 */
	public static String createSettingParams(Long typeID, Long startValue, Long endValue, Long useTime) {
		String str = "";
		if (typeID == 15) { 						// Speeding
			if (startValue != null) {
				str += "0055," + startValue + ",;";
			}
			if (useTime != null) {
				str += "00F0," + useTime + ",;";
			}
		} else if (typeID == 11) {				// Sudden Acceleration
			if (startValue != null) {
				str += "FC00," + startValue + ",;";
			}
			if (useTime != null) {
				str += "FC01," + useTime + ",;";
			}
		} else if (typeID == 12) {				// Sudden Braking
			if (startValue != null) {
				str += "FC02," + startValue + ",;";
			}
			if (useTime != null) {
				str += "FC03," + useTime + ",;";
			}
		} else if (typeID == 17) {				// Idle
			if (useTime != null) {
				str += "FC15," + useTime + ",;";
			}
		} else if (typeID == 19) { 				// Engine Overspeed
			if (startValue != null) {
				str += "FC17," + startValue + ",;";
			}
			if (useTime != null) {
				str += "00FD," + useTime + ",;";
			}
		} else if (typeID == 13) { 				// Sudden left Sudden Right
			if (startValue != null) {
				str += "FC19," + startValue + ",;";
			}
			if (endValue != null) {
				str += "00FA," + endValue + ",;";
			}
			if (useTime != null) {
				str += "FC1A," + useTime + ",;";
			}
		} else if (typeID == 16) { 				// Neutral Slide
			if (startValue != null) {
				str += "FC21," + startValue + ",;";
			}
			if (endValue != null) {
				str += "FC20," + endValue + ",;";
			}
			if (useTime != null) {
				str += "00F7," + useTime + ",;";
			}
		}
		return str;
	}
	
	/**
	 * 设备连接ip地址和端口
	 * @param ip
	 * @param port
	 * @return 指令字符串
	 */
	public static String setIPAndPort(String ip, String port) {
		String str = "0013," + ip + ",;0018," + port + ",;";
		return str;
	}
	
	/***
	 * 维护用户表
	 * @param userName	用户名
	 * @param flag				 0新增；1删除
	 *	$$|60|ctx|123456|1|1|0|system_admin|##    0新增
	 * $$|60|ctx|123456|1|1|1|system_admin|##	  1删除
	 */
	public static String setAccount( String userName, String flag) {
		return "$$|60|" + SOCKET_BASE_NAME + "|123456|1|1|" + flag + "|" + userName + "|##";
	}
	
	/***
	 * 维护车辆表
	 * @param vehName	车辆名
	 * @param flag				 0新增；1删除
	 *	$$|60|xjp10|123456|2|1|0|1001|##	0新增
	 *	$$|60|xjp10|123456|2|1|1|0001|##	1删除
	 */
	public static String setVehicle( String vehName, String flag) {
		return "$$|60|" + SOCKET_BASE_NAME + "|123456|2|1|" + flag + "|" + vehName + "|##";
	}
	
	/***
	 * 断油断电指令
		断油断电
		$$|AA|1|[时间戳]|[用户名]|[车辆名称]|AA55|##
		恢复供油供电
		$$|AA|1|[时间戳]|[用户名]|[车辆名称]|1111|##
	 * @param time			[时间戳]
	 * @param username	[用户名]
	 * @param vehname	[车辆名称]
	 * @param flag "1"断油断电AA55 "2"恢复供油供电1111
	 * @return $$|A0|system_admin|system_admin|##
	 */
	public static String setCuttingStr(String timestamp, String userName, String vehName, String flag) {
		if (flag.equals("1")) {
			return "$$|AA|1|" + timestamp + "|" + SOCKET_BASE_NAME + "|" + vehName + "|AA55|##";
		} else {
			return "$$|AA|1|" + timestamp + "|" + SOCKET_BASE_NAME + "|" + vehName + "|1111|##";
		}
	}
	
	/***
	 * 发送socket
	 * @param 登陆用户
	 * @param 字符串
	 */
	public synchronized static boolean sendSocket(String str) {
		Socket socket = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String resStr = "";
		if(SOCKET_BASE_ISOPEN.equals("true")) {
			try {
				socket = new Socket(SocketUtil.SOCKET_IP, SocketUtil.SOCKET_PORT);	//新建socket
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				outputStream.write(LOGIN_STRING.getBytes());	//先发登陆验证
				outputStream.flush();
				Long beginStamp = System.currentTimeMillis();
				while (true) {
					if(inputStream.available() > 0) {
						resStr = getStringByInputStream(inputStream);
						if((System.currentTimeMillis() - beginStamp) / 1000f >  SocketUtil.SOCKET_LOGIN_TIMEOUT) {	//超时关闭
							if(inputStream != null) {
								inputStream.close();
							}
							if(outputStream != null) {
								outputStream.close();
							}
							if(socket != null && !socket.isClosed()) {
								socket.close();
								socket = null;
							}
							return false;
						}
						if (SocketUtil.checkHeadAndTail(resStr)) {
							if (SocketUtil.checkLogin(resStr)) {
								break;
							} else {
								if(inputStream != null) {
									inputStream.close();
								}
								if(outputStream != null) {
									outputStream.close();
								}
								if(socket != null && !socket.isClosed()) {
									socket.close();
									socket = null;
								}
								return false;
							}
						}
					}
				}
				outputStream.write(str.getBytes());
				outputStream.flush();
				beginStamp = System.currentTimeMillis();
				while (true) {
					if(inputStream.available() > 0) {
						resStr = SocketUtil.getStringByInputStream(inputStream);
						if ((System.currentTimeMillis() - beginStamp) / 3000f > SocketUtil.SOCKET_TIMEOUT) {	// 超时暂时调整为1秒
							if(inputStream != null) {
								inputStream.close();
							}
							if(outputStream != null) {
								outputStream.close();
							}
							if(socket != null && !socket.isClosed()) {
								socket.close();
								socket = null;
							}
							return false;
						}
						if (SocketUtil.checkHeadAndTail(resStr)) {
							if (resStr.startsWith("$$|67")) {
								break;
							}
						}
					}
				}
				String[] resArr = SocketUtil.splitGPSDate(resStr);
				if (resArr[5].startsWith("0")) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					if(inputStream != null) {
						inputStream.close();
					}
					if(outputStream != null) {
						outputStream.close();
					}
					if(socket != null && !socket.isClosed()) {
						socket.close();
						socket = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			return false;
		}
	}

	/***
	 * 解析登陆回馈信息
	 * @param str
	 * @return true 登陆成功 false 登陆失败
	 */
	public static boolean checkLogin(String str) {
		if (str == null || str.equals("")) {
			return false;
		}
		String[] strArr = str.split("\\|");
		if (strArr[4].equals("1")) {
			return true;
		}
		return false;
	}

	/***
	 * 拆分指令（"|"拆分）
	 * @param str
	 *            : 1.返回车辆数据
	 *            $$|B6|[车辆所属]|[车辆名称]|[GPS数据产生日期]|[GPS数据产生时间]|[纬度]|[经度]
	 *            |[速度]|[方位角]
	 *            |[高度]|[车辆运行标识]|[下一个站站点序号]|[发动机温度]|[车厢内温度]|[行使里程]|[车上人数]
	 *            |[已发车或已到达终点标识]
	 *            |[已发车或已到达终点日期]|已发车或已到达终点时间]|[已发车或已到达终点方向]|[车辆告警状态]
	 *            |[驾驶员ID]|[是否在线]
	 *            |[离线原因]|[趟次]|[班次]|[原始的线路号]|[增加字段离线类型]|[驾驶员名称]|[司机在线]|##
	 *            2.返回定位数据 $$|C2|[车辆所属]|[车辆名称]|[GPS数据产生日期]|[GPS数据产生时间]|[纬度]|[经度]
	 *            |[速度]|[方位角]
	 *            |[高度]|[车辆运行标识]|[下一个站站点序号]|[发动机温度]|[车厢内温度]|[行使里程]|[车上人数]
	 *            |[已发车或已到达终点标识]
	 *            |[已发车或已到达终点日期]|已发车或已到达终点时间]|[已发车或已到达终点方向]|[车辆告警状态]
	 *            |[驾驶员ID]|[是否在线]
	 *            |[离线原因]|[趟次]|[班次]|[原始的线路号]|[增加字段离线类型]|[驾驶员名称]|[司机在线]|##
	 * @return
	 */
	public static String[] splitGPSDate(String gpsData) {
		return gpsData.split("\\|");
	}

	/***
	 * 获取车辆数据
	 * @param busName
	 * @return 例：$$|A1|110001|##
	 */
	public static String getVehicles(String busName) {
		return "$$|A1|" + busName + "|##";
	}

	/**
	 * 获取InputStream中字符串
	 * @param stream
	 * @return String
	 * @throws IOException
	 */
	public static String getStringByInputStream(InputStream stream) throws IOException {
		if(stream.available() != 0) {
			int count = stream.available();
			byte[] cacheSize = new byte[1024];
			StringBuffer stringBuffer = new StringBuffer("");
			int readSize = stream.read(cacheSize, 0, cacheSize.length);
			while(readSize != 0 && readSize != -1) {
				stringBuffer.append((new String(Arrays.copyOf(cacheSize, readSize), "GBK")));
				count = count - readSize;
				if(readSize < 1024) {
					break;
				 }
				 if(count <= 0) {
					 break;
				 }
				 readSize = stream.read(cacheSize, 0, cacheSize.length);
			  }
			return stringBuffer.toString();
		} else {
			return "";
		}
	}

	/***
	 * 取得gps字符串的数组
	 * @param str
	 * @return
	 */
	public static List<String[]> getGpsStrArr(String command) {
		List<String[]> resList = new ArrayList<String[]>();
		if (checkHeadAndTail(command)) {	// 验证数据是否完整
			List<String> commandList = subCommand(command);
			if(commandList != null && commandList.size() != 0) {
				for(String comm : commandList) {
					resList.add(SocketUtil.splitGPSDate(comm));
				}
				return resList;
			}
		}
		return null;
	}

	/***
	 * 验证指令是否是以$$开始 以 ##结尾
	 * @param command 待验证字符串
	 * @return boolean true:是；false:否
	 */
	public static boolean checkHeadAndTail(String command) {
		return command.startsWith("$$") && command.endsWith("##");
	}

	/***
	 * 验证指令是否是gps数据
	 * @param command 待验证指令
	 * @return boolean true:是；false:否
	 */
	public static boolean isGPSData(String command) {
		if(!checkHeadAndTail(command)) {
			return false;
		}
		return command.startsWith("$$|C2|");
	}
	
	/***
	 * 连续指令串分解返回指令List
	 * @param command
	 * @return List<String>
	 */
	public static List<String> subCommand(String command) {
		List<String> commandList = new ArrayList<String>();
		while(command.indexOf("$$") != -1) {
			commandList.add(command.substring(command.indexOf("$$"), command.indexOf("##")+2));
			command = command.substring(command.indexOf("##") + 2);
		}
		return commandList;
	}
}