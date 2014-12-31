package com.zdtx.ifms.specific.web.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.SocketUtil;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.service.monitor.SocketManager;

/**
 * @description 新实时定位数据websocket service端(Java 6)
 * @author Liu Jun
 * @since 2014-9-3 13:41:17
 */
@SuppressWarnings("deprecation")
@WebServlet(name = "realtime", urlPatterns="/websocket/realtime_old", loadOnStartup = 5)
public class RealtimeWsServerOld extends WebSocketServlet {
	
	private static final long serialVersionUID = -4196119292352244908L;

	private static final Logger LOGGER = Logger.getLogger(RealtimeWsServerOld.class);

	@Autowired
	private SocketManager socketManager;
	
	protected static final Set<RealtimeWsInbound> clients = new CopyOnWriteArraySet<RealtimeWsInbound>();

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddhh:mm:dd");

	private static Socket socket = null;
	private static boolean running = false;
	private long lastSendTime;
	String fencingStr = SocketUtil.getFenceStr(SocketUtil.SOCKET_GPS_NAME);// 订阅围栏信息
	InputStream inputStream = null;
	OutputStream outputStream = null;
	
	@Override
	protected StreamInbound createWebSocketInbound(String str, HttpServletRequest request) {
		return new RealtimeWsInbound();
	}

	@Override
	public void init() throws ServletException {	//加载方法
		super.init();
		if (SocketUtil.SOCKET_GPS_ISOPEN.trim().equals("true")) {
			System.out.println("RealtimeWsServer6: start up automatic!");
			creatSocket();
		}
	}
	
	/**
	 * 创建新socket连接，并保持长连接
	 */
	private void creatSocket() {
		if(running) {	//正在运行则返回
			return;
		}
		try {
			socket = new Socket(SocketUtil.SOCKET_IP, SocketUtil.SOCKET_PORT);	//创建socket链接
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			outputStream.write(SocketUtil.LOGIN_STRING.getBytes());
			outputStream.flush();
			lastSendTime = System.currentTimeMillis();
			while (true) {
				try {
					if ((System.currentTimeMillis() - lastSendTime) / 1000f > SocketUtil.SOCKET_LOGIN_TIMEOUT) {	//验证超时（暂定20秒）
						System.out.println("First connect time out, close socket!");
						running = false;
						socket.close();	//超时则关闭socket
						socket = null;
						break;
					}
					if (inputStream.available() > 0) {
						String answerStr = SocketUtil.getStringByInputStream(inputStream);	//获取应答
						if (SocketUtil.checkHeadAndTail(answerStr)) {	//验证格式是否合法
							if (SocketUtil.checkLogin(answerStr)) {
								System.out.println("Login succeed!");
								running = true;
								new Thread(new KeepAliveWatchDog()).start();
								new Thread(new ReceiveWatchDog()).start();
							} else {
								System.out.println("Login failed, close socket!");
								running = false;
								socket.close();	//登陆失败关闭socket
								socket = null;
							}
							break;
						}
					} else {
						Thread.sleep(100);	//心跳频率
					}
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		} catch (Exception e) {
			try {
				if(socket != null) {
					socket.close();
				}
				socket = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * @description 保持socket长连接心跳监听线程
	 * @author Liu Jun
	 * @since 2014年8月8日 上午9:53:56
	 */
	class KeepAliveWatchDog implements Runnable {
        long checkDelay = 100;	//等待时间
        long keepAliveDelay = 3000;  //心跳频率
        long keepAliveLastSendTime = System.currentTimeMillis();
		public void run() {
			System.out.println("Start to keepAliveDelay6");
			while (running) {
                if(System.currentTimeMillis() - keepAliveLastSendTime > keepAliveDelay) {
                    try {
                    	outputStream.write(fencingStr.getBytes());// 给socket服务器发送订阅信息
                    	outputStream.flush();
                    	keepAliveLastSendTime = System.currentTimeMillis();
                    } catch (IOException e) {
                    	running = false;
                        try {
                        	socket.close();		//心跳失败关闭socket
                        } catch (IOException e1) {
                        }
                        socket = null;
                        System.out.println("Keep alive failed, close socket!");
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(checkDelay);	//Sleep失败关闭socket
                    } catch (InterruptedException e) {
                    	running = false;
                        try {
                        	if(socket != null) {
                        		socket.close();		//登陆失败关闭socket
                        	}
                        } catch (IOException e1) {
                        }
                        socket = null;
                        System.out.println("Sleep failed, close socket!");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
	
	/**
	 * @description 应答监听线程
	 * @author Liu Jun
	 * @since 2014年8月8日 上午9:59:37
	 */
	class ReceiveWatchDog implements Runnable {
		public void run() {
			System.out.println("Start to ReceiveWatchDog6");
			while (running) {
				try {
					if (inputStream.available() > 0) {
						String answerStr = SocketUtil.getStringByInputStream(inputStream);
						if (!Utils.isEmpty(answerStr)) {
							List<String[]> answerList = SocketUtil.getGpsStrArr(answerStr);
							if (answerList != null && answerList.size() != 0) {
								for (String[] answerArr : answerList) {
									if (answerArr != null) {
										if (answerArr[1].equals("C2")) {	//GPS指令
											//$$|C2|[车辆所属]|[车辆名称]|[GPS数据产生日期]|[GPS数据产生时间]|[纬度]|[经度]|[速度]|[方位角]
											//|[高度]|[车辆运行标识]|[下一个站站点序号]|[发动机温度]|[车厢内温度]|[行使里程]|[车上人数]
											//|[已发车或已到达终点标识]|[已发车或已到达终点日期]|已发车或已到达终点时间]|[已发车或已到达终点方向]
											//|[车辆告警状态]|[驾驶员ID]|[是否在线]|[离线原因]|[趟次]|[班次]|[原始的线路号]|[增加字段离线类型]|[驾驶员名称]|[司机在线]|##	
											sendGPSData(answerArr);
										} else if (answerArr[1].equals("CA")) {		//紧急加减速数据
											//$$|CA|[车辆所属]|[车辆名称]|[GPS数据产生日期]|[GPS数据产生时间]|[纬度]|[经度]|[速度]|[方位角]
											//|[车辆运行标识]|[制动速度]|[标识]|[时长]|[前一站点序号]|##
											if (answerArr[10].equals("1")) {	//Sudden Acceleration
												sendAlert(new String[] {answerArr[3], "5", answerArr[7], answerArr[6], answerArr[4] + " " + answerArr[5], answerArr[8]});
											} else if (answerArr[10].equals("2")) {	//Sudden Braking
												sendAlert(new String[] {answerArr[3], "6", answerArr[7], answerArr[6], answerArr[4] + " " + answerArr[5], answerArr[8]});
											}
											//socketManager.saveSpeedUpDown(answerArr);	//保存数据
										} else if (answerArr[1].equals("C9")) {	//超速数据
											//$$|C9|[车辆所属]|[车辆名称]|[GPS数据产生日期]|[GPS数据产生时间]|[纬度]|[经度]|[速度]|[方位角]
											//|[车辆运行标识]|[标准速度]|[标识]|[超速时长]|[前一站点序号]|##
											if (answerArr[12].equals("1")) {
												sendAlert(new String[] {answerArr[3], "4", answerArr[7], answerArr[6], answerArr[4] + " " + answerArr[5], answerArr[8]});
											}
											//socketManager.saveOverSpeed(answerArr);	//保存数据
										} else if (answerArr[1].equals("FD")) {		//其他告警事件数据
											//$$|FD|[告警类型]|[车号]|[GPS数据产生日期]|[GPS数据产生时间]|[纬度]|[经度]|[速度]|[方位角]|[告警内容]|##
											if (answerArr[2].equals("247")) {	//Neutral slide
												sendAlert(new String[] {answerArr[3], "9", answerArr[7], answerArr[6], answerArr[4] + " " + answerArr[5], answerArr[10]});
											} else if (answerArr[2].equals("248")) {	//Idle
												sendAlert(new String[] {answerArr[3], "3", answerArr[7], answerArr[6], answerArr[4] + " " + answerArr[5], answerArr[10]});
											} else if (answerArr[2].equals("250")) {		//Sudden left
												sendAlert(new String[] {answerArr[3], "7", answerArr[7], answerArr[6], answerArr[4] + " " + answerArr[5], answerArr[10]});
											} else if (answerArr[2].equals("251")) {		//Sudden right
												sendAlert(new String[] {answerArr[3], "8", answerArr[7], answerArr[6], answerArr[4] + " " + answerArr[5], answerArr[10]});
											} else if (answerArr[2].equals("253")) {		//Engine overspeed
												sendAlert(new String[] {answerArr[3], "11", answerArr[7], answerArr[6], answerArr[4] + " " + answerArr[5], answerArr[10]});
											}
											//socketManager.saveWarningInfo(answerArr);
										} else if (answerArr[1].equals("B5")) {
											running = false;
										} else if (answerArr[1].equals("B4")) {	//返回设备下线
											sendOffline(new String[] {answerArr[3], answerArr[7], answerArr[6], answerArr[9]});
										}
									}
								}
							}
						}
					} else {
						Thread.sleep(100);
					}
				} catch (Exception e) {
					running = false;
					 try {
						 if(socket != null) {
                     		socket.close();		//处理实时数据失败关闭socket
                     	}
                     } catch (IOException e1) {
                     }
                     socket = null;
                     System.out.println("Process data failed, close socket!");
                     e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 发送定位数据
	 * @param gpsArr
	 *            $$, C2, 车辆所属(1为单兵设备，其他为车载设备), 车辆名称, GPS数据产生日期, GPS数据产生时间,
	 *            纬度, 经度, 速度, 方位角, 高度,
	 *            车辆运行标识, 下一个站站点序号, 发动机温度, 车厢内温度, 行使里程,
	 *            车上人数, 已发车或已到达终点标识, 已发车或已到达终点日期, 已发车或已到达终点时间, 已发车或已到达终点方向,
	 *            车辆告警状态, 驾驶员ID, 是否在线, 离线原因, 趟次,
	 *            班次, 原始的线路号, 增加字段离线类型, 驾驶员名称, 司机在线, ##
	 * @return "gpsdata, 车辆名称, 经度, 纬度, 驾驶员名称, 车辆所属(1为单兵设备，其他为车载设备), 车辆告警状态, 速度, 处理后方位角, 发送时系统时间"
	 */
	private static void sendGPSData(String[] dataArray) {
		Double lat = new Double(dataArray[6]) / 60;
		Double lng = new Double(dataArray[7]) / 60;
		Integer angle = (int)(36 - Math.ceil(new Double(dataArray[9]) / 10));//向上取整
		String message = "";
		if (lat == 0D) {	//错误数据
			message = "gpsData," + dataArray[3] + ","
					+ (6227.2995 / 60) + "," + (79.7982 / 60) + ","
					+ dataArray[29] + "," + dataArray[2] + ","
					 + dataArray[21] + "," + dataArray[8] + ","
					+ angle + "," + DateUtil.formatLongTimeDate(new Date());
		} else {
			message = "gpsData," + dataArray[3] + ","
					+ lng + "," + lat + "," + dataArray[29] + "," + dataArray[2] + ","
					+ dataArray[21] + "," + dataArray[8] + ","
					+ angle + "," + DateUtil.formatLongTimeDate(new Date());
		}
		for (RealtimeWsInbound inbound : clients) {
			try {
				synchronized (inbound) {
					inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
					inbound.getWsOutbound().flush();
				}
			} catch (IOException e) {
				LOGGER.error("Failed to send GPS data to client: " + inbound + ", remove it!", e);
				clients.remove(inbound);
				try {
					inbound.getWsOutbound().close(0, null);
				} catch (IOException e1) {
				}
			}
		}
	}

	/***
	 * 发送车辆下线
	 * @param dataArray[] {车辆/单兵名称, 经度, 纬度, 时间}
	 * @return "offline, 车辆/单兵名称, 经度, 纬度, 时间"
	 */
	private static void sendOffline(String[] dataArray) {
		String message = "offline," + dataArray[0] + "," + (new Double(dataArray[1]) / 60) + ","
				+ (new Double(dataArray[2]) / 60) + "," + dataArray[3];
		for (RealtimeWsInbound inbound : clients) {
			try {
				synchronized (inbound) {
					inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
					inbound.getWsOutbound().flush();
				}
			} catch (IOException e) {
				LOGGER.error("Failed to send offline message to client: " + inbound + ", remove it!", e);
				clients.remove(inbound);
				try {
					inbound.getWsOutbound().close(0, null);
				} catch (IOException e1) {
				}
			}
		}
	}
	
	/***
	 * 发送报警数据
	 * @param dataArray[] {车辆/单兵名称, 报警类型, 经度, 纬度, 时间, 报警值}
	 * @return "alertData, 车辆/单兵名称, 报警类型, 经度, 纬度, 时间, 报警值"
	 */
	private static void sendAlert(String[] dataArray) {
		String message = "alertData," + dataArray[0] + "," + dataArray[1] + "," + (new Double(dataArray[2]) / 60) + ","
				+ (new Double(dataArray[3]) / 60) + "," + dataArray[4] + "," + dataArray[5];
		for (RealtimeWsInbound inbound : clients) {
			try {
				synchronized (inbound) {
					inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
					inbound.getWsOutbound().flush();
				}
			} catch (IOException e) {
				LOGGER.error("Failed to send alert data to client: " + inbound + ", remove it!", e);
				clients.remove(inbound);
				try {
					inbound.getWsOutbound().close(0, null);
				} catch (IOException e1) {
				}
			}
		}
	}
}