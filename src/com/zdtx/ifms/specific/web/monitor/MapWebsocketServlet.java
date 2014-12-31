package com.zdtx.ifms.specific.web.monitor;


//

//@SuppressWarnings("deprecation")
//@WebServlet(name = "busmap", urlPatterns = "/busmap.do", loadOnStartup = 9)
public class MapWebsocketServlet/* extends WebSocketServlet*/ {

//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 6825717702506149915L;
//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddhh:mm:dd");
//	Date testtime = null;
//	MapWebsocketServlet mapwebservlet;
//	// Map<String, Object> map ;
//
//	// static final int PORT = 8666;// 提供接收服务端口
//	// ServerSocket ss = null;
//	// RunServer runServer = null;
//
//	/**
//	 * 用来保存websocket流的map容器
//	 */
//	private Map<String, MySocket> mysockets = null;
//	private Socket socket = null;
//	String loginSocketStr = SocketUtil.loginStr(SocketUtil.socketgpsname,
//			SocketUtil.socketgpspwd);//登录规则串
//	String fencingStr = SocketUtil.fenceStr(SocketUtil.socketgpsname);// 订阅围栏信息
//	InputStream brin = null;
//	OutputStream pwout = null;
//	SendallSocket sendallSocket = null;// 接收socket 发送websocket 线程
//	SocketCreat socketCreat = null;
//	
//	@Autowired
//	private SocketManager socketManager;
//
//	@Override
//	public void init() throws ServletException {//加载方法
//		mysockets = new HashMap<String, MySocket>();//创建容器，用来保存登录用户的链接信息
//		super.init();
//		if (SocketUtil.setgps.trim().equals("true")) {
//			socketCreat = new SocketCreat();//创建socket 长连接线程
//		}
//		mapwebservlet = this;
//	}
//
//	private void creatSocket() {
//		SocketUtil.savelog("cr:" + DateUtil.formatLongTimeDate(new Date()));//创建连接日志
//		long a = System.currentTimeMillis();
//		try {
//			socket = new Socket(SocketUtil.socketip, SocketUtil.socketport);//创建socket链接
//
//			brin = socket.getInputStream();
//			pwout = socket.getOutputStream();
//			pwout.write(loginSocketStr.getBytes());// 给socket服务器 发送登陆信息
//			pwout.flush();
//			while (true) {
//				try {
//					String strtemp = SocketUtil.getStringByInputStream(brin);
//					/***
//					 * 取得登陆返回信息
//					 */
//					long b = System.currentTimeMillis();//计时
//					if ((b - a) / 1000f > SocketUtil.logintimeout) {
//						socket.close();
//						socket = null;
//						break;
//					}
//					if (SocketUtil.stringAllCheck(strtemp)) {
//						/***
//						 * 验证登陆返回信息
//						 */
//						if (SocketUtil.logincheck(strtemp)) {
//							pwout.write(fencingStr.getBytes());// 给socket服务器
//																// 发送订阅信息
//							pwout.flush();
//							sendallSocket = new SendallSocket();
//							break;
//						} else {
//							// if(socket!=null){
//							// try {
//							// socket.close();
//							// } catch (IOException e1) {
//							// }
//							// }
//							sendallSocket = null;
//							socket = null;
//							break;
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					break;
//				}
//			}
//		} catch (Exception e) {
//			// if(socket!=null){
//			// try {
//			// socket.close();
//			// } catch (IOException e1) {
//			// }
//			// }
//			socket = null;
//		}
//	}
//
//	// private final Map<String, MySocket> socketmap = new HashMap<String,
//	// MySocket>();
//	int i = 0;
//
//	@Override
//	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest request) {//创建websocket
//		MySocket mysocket = new MySocket(this.mysockets, (String) request.getParameter("ids"), request.getSession());// 继承MessageInbound类
//		return mysocket;
//	}
//
//	// socket服务线程
//	class SocketCreat extends Thread {
//
//		public SocketCreat() {
//			this.start();
//		}
//
//		@Override
//		public void run() {
//			super.run();
//			while (true) {
//				try {
//					Thread.sleep(20000l);
//					// Thread.sleep(300000l);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//				SocketUtil.savelog("yz:" + DateUtil.formatLongTimeDate(new Date()));//日志验证链接
//				if (socket == null) {
//					creatSocket();
//				} else {
//					try {
//						pwout = socket.getOutputStream();
//						pwout.write(loginSocketStr.getBytes());// 给socket服务器
//																// 发送登陆信息
//						pwout.flush();
//						pwout.write(fencingStr.getBytes());// 给socket服务器 发送登陆信息
//						pwout.flush();
//					} catch (Exception e) {
//						creatSocket();
//					}
//				}
//			}
//		}
//	}
//
//	// 接收socket 发送websocket 线程
//	class SendallSocket extends Thread {
//		// private Set<String> busset=null;
//		public SendallSocket() {
//			// busset=new HashSet<String>();
//			runflag = true;
//			start();
//		}
//
//		private boolean runflag = true;
//
//		@Override
//		public void run() {
//			super.run();
//			try {
//				while (runflag) {
//					String str = SocketUtil.getStringByInputStream(brin);
//					if (str != null && !"".equals(str)) {
//						List<String[]> stras = SocketUtil.getGpsStrArr(str);
//						if (stras != null && stras.size() != 0) {
//							for (String[] gpsArr : stras) {
//								if (gpsArr != null) {
//									if (gpsArr[1].equals("C2")) {
//										//$$|C2|[车辆所属]|[车辆名称]|[GPS数据产生日期]|[GPS数据产生时间]|[纬度]|[经度]|[速度]|[方位角]
//										//|[高度]|[车辆运行标识]|[下一个站站点序号]|[发动机温度]|[车厢内温度]|[行使里程]|[车上人数]
//										//|[已发车或已到达终点标识]|[已发车或已到达终点日期]|已发车或已到达终点时间]|[已发车或已到达终点方向]
//										//|[车辆告警状态]|[驾驶员ID]|[是否在线]|[离线原因]|[趟次]|[班次]|[原始的线路号]|[增加字段离线类型]|[驾驶员名称]|[司机在线]|##	
//										for (Iterator<String> keys = mysockets.keySet().iterator(); keys.hasNext();) {
//											MySocket m = mysockets.get((String) keys.next());
//											WsOutbound out = m.getWsOutbound();
//											sendOut(gpsArr, out, gpsArr[3]);
//										}
//										mapwebservlet.getServletContext().setAttribute(gpsArr[3], gpsArr[3]);
//									} else if (gpsArr[1].equals("CA")) {		//紧急加减速数据
//										//$$|CA|[车辆所属]|[车辆名称]|[GPS数据产生日期]|[GPS数据产生时间]|[纬度]|[经度]|[速度]|[方位角]
//										//|[车辆运行标识]|[制动速度]|[标识]|[时长]|[前一站点序号]|##
//										for (Iterator<String> keys = mysockets.keySet().iterator(); keys.hasNext();) {
//											MySocket m = mysockets.get((String) keys.next());
//											WsOutbound out = m.getWsOutbound();
//											String[] alertArry;
//											if (gpsArr[10].equals("1")) {	//Sudden acceleration
//												alertArry = new String[] { gpsArr[4] + " " + gpsArr[5], gpsArr[3], gpsArr[7], gpsArr[6], "5" };
//												sendAlert(alertArry, out, gpsArr[8], gpsArr[3]);
//											} else if (gpsArr[10].equals("2")) {	//Sudden braking
//												alertArry = new String[] { gpsArr[4] + " " + gpsArr[5], gpsArr[3], gpsArr[7], gpsArr[6], "6" };
//												sendAlert(alertArry, out, gpsArr[8], gpsArr[3]);
//											}
//										}
//										socketManager.saveSpeedUpDown(gpsArr);	//保存数据
//									} else if (gpsArr[1].equals("C9")) {	//超速数据
//										//$$|C9|[车辆所属]|[车辆名称]|[GPS数据产生日期]|[GPS数据产生时间]|[纬度]|[经度]|[速度]|[方位角]
//										//|[车辆运行标识]|[标准速度]|[标识]|[超速时长]|[前一站点序号]|##
//										if (gpsArr[12].equals("1")) {
//											for (Iterator<String> keys = mysockets.keySet().iterator(); keys.hasNext();) {
//												MySocket m = mysockets.get((String) keys.next());
//												WsOutbound out = m.getWsOutbound();
//												String[] alertArry = new String[] { gpsArr[4] + " " + gpsArr[5], gpsArr[3], gpsArr[7], gpsArr[6], "4" };
//												sendAlert(alertArry, out, gpsArr[8], gpsArr[3]);
//											}
//										}
//										socketManager.saveOverSpeed(gpsArr);	//保存数据
//									} else if (gpsArr[1].equals("FD")) {		//其他告警事件数据
//										//$$|FD|[告警类型]|[车号]|[GPS数据产生日期]|[GPS数据产生时间]|[纬度]|[经度]|[速度]|[方位角]|[告警内容]|##
//										for (Iterator<String> keys = mysockets.keySet().iterator(); keys.hasNext();) {
//											MySocket m = mysockets.get((String) keys.next());
//											WsOutbound out = m.getWsOutbound();
//											if (gpsArr[2].equals("247")) {	//Neutral slide
//												String[] alertArry = new String[] { gpsArr[4] + " " + gpsArr[5], gpsArr[3], gpsArr[7], gpsArr[6], "9" };
//												sendAlert(alertArry, out, gpsArr[10], gpsArr[3]);
//											} else if (gpsArr[2].equals("248")) {	//Idle
//												String[] alertArry = new String[] { gpsArr[4] + " " + gpsArr[5], gpsArr[3], gpsArr[7], gpsArr[6], "3" };
//												sendAlert(alertArry, out, gpsArr[10], gpsArr[3]);
//											} else if (gpsArr[2].equals("249")) {	//Idle air conditioning
//												String[] alertArry = new String[] { gpsArr[4] + " " + gpsArr[5], gpsArr[3], gpsArr[7], gpsArr[6], "10" };
//												sendAlert(alertArry, out, gpsArr[10], gpsArr[3]);
//											} else if (gpsArr[2].equals("250")) {	//Sudden left
//												String[] alertArry = new String[] { gpsArr[4] + " " + gpsArr[5], gpsArr[3], gpsArr[7], gpsArr[6], "7" };
//												sendAlert(alertArry, out, gpsArr[10], gpsArr[3]);
//											} else if (gpsArr[2].equals("251")) {	//Sudden right
//												String[] alertArry = new String[] { gpsArr[4] + " " + gpsArr[5], gpsArr[3], gpsArr[7], gpsArr[6], "8" };
//												sendAlert(alertArry, out, gpsArr[10], gpsArr[3]);
//											} else if (gpsArr[2].equals("252")) {
//												// String[] alertArry=new
//												// String[]{gpsArr[4]+" "+gpsArr[5],gpsArr[3],gpsArr[7],gpsArr[6],"9"};
//												// sendAlert(alertArry,out,gpsArr[10]);
//											} else if (gpsArr[2].equals("253")) {	//Engine overspeed
//												String[] alertArry = new String[] { gpsArr[4] + " " + gpsArr[5], gpsArr[3], gpsArr[7], gpsArr[6], "11" };
//												sendAlert(alertArry, out, gpsArr[10], gpsArr[3]);
//											}
//										}
//										socketManager.saveWarningInfo(gpsArr);
//									} else if (gpsArr[1].equals("66")) {	// 围栏
//										for (Iterator<String> keys = mysockets.keySet().iterator(); keys.hasNext();) {
//											MySocket m = mysockets.get((String) keys.next());
//											WsOutbound out = m.getWsOutbound();
//											String[] inners = gpsArr[5].split(",");
//											if (inners[1].equals("1")) {	//Into the Geo Fencing
//												String[] alertArry = new String[] { inners[4] + " " + inners[5], inners[0], "" + new Double(inners[6]) / 600,
//														"" + new Double(inners[7]) / 600, "12" };
//												sendAlert(alertArry, out, "", inners[0]);
//											} else if (inners[1].equals("2")) {	//Exit the Geo Fencing
//												String[] alertArry = new String[] { inners[4] + " " + inners[5], inners[0], "" + new Double(inners[6]) / 600,
//														"" + new Double(inners[7]) / 600, "13" };
//												sendAlert(alertArry, out, "", inners[0]);
//											}
//										}
//									} else if (gpsArr[1].equals("B5")) {
//										runflag = false;
//									} else if (gpsArr[1].equals("B4")) {	//返回设备下线
//										for (Iterator<String> keys = mysockets.keySet().iterator(); keys.hasNext();) {
//											MySocket m = mysockets.get((String) keys.next());
//											WsOutbound out = m.getWsOutbound();
//											sendLogout(gpsArr[3], out, gpsArr[3], gpsArr[6], gpsArr[7], gpsArr[8], gpsArr[9]);
//										}
//										mapwebservlet.getServletContext().removeAttribute(gpsArr[3]);
//									}
//								}
//							}
//						}
//					}
//				}
//				creatSocket();
//			} catch (Exception e) {
//				creatSocket();
//			}
//		}
//
//		/**
//		 * 发送定位数据
//		 * @param gpsArr
//		 *            $$, C2, 车辆所属, 车辆名称, GPS数据产生日期, GPS数据产生时间, 纬度, 经度, 速度, 方位角,
//		 *            高度, 车辆运行标识, 下一个站站点序号, 发动机温度, 车厢内温度, 行使里程, 车上人数,
//		 *            已发车或已到达终点标识, 已发车或已到达终点日期, 已发车或已到达终点时间, 已发车或已到达终点方向,
//		 *            车辆告警状态, 驾驶员ID, 是否在线, 离线原因, 趟次, 班次, 原始的线路号, 增加字段离线类型,
//		 *            驾驶员名称, 司机在线, ##
//		 * @param out
//		 * @param videoid
//		 * @throws IOException
//		 */
//		public void sendOut(String[] gpsArr, WsOutbound out, String videoid) throws IOException {
//			Double x = new Double(gpsArr[6]) / 60;
//			Double y = new Double(gpsArr[7]) / 60;
//			Double jiao = new Double(gpsArr[9]) / 10;
//			jiao = 36 - Math.ceil(jiao);
//			if (y == 0D) {
//				out.writeTextMessage(CharBuffer.wrap("bus," + gpsArr[3] + ","
//						+ (6227.2995 / 60) + "," + (79.7982 / 60) + ","
//						+ gpsArr[29] + "," + gpsArr[2] + "," + gpsArr[13] + ","
//						+ gpsArr[14] + "," + gpsArr[21] + "," + gpsArr[8] + ","
//						+ (int) Math.ceil(jiao) + "," + gpsArr[9] + ","
//						+ DateUtil.formatLongTimeDate(new Date()) + ","
//						+ videoid));
//			} else {
//				out.writeTextMessage(CharBuffer.wrap("bus," + gpsArr[3] + ","
//						+ y + "," + x + "," + gpsArr[29] + "," + gpsArr[2]
//						+ "," + gpsArr[13] + "," + gpsArr[14] + ","
//						+ gpsArr[21] + "," + gpsArr[8] + ","
//						+ (int) Math.ceil(jiao) + "," + gpsArr[9] + ","
//						+ DateUtil.formatLongTimeDate(new Date()) + ","
//						+ videoid));
//			}
//			// System.out.println("******webSocket:"+"bus,"+gpsArr[3]+","+y+","+x+","+gpsArr[29]+","+gpsArr[2]+","+gpsArr[13]+","+gpsArr[14]+","+gpsArr[21]+","+gpsArr[8]+","+(int)
//			// Math.ceil(jiao)+","+gpsArr[9]+","+DateUtil.formatLongTimeDate(new
//			// Date()));
//			out.flush();
//		}
//
//		/***
//		 * 车辆下线
//		 * @return "busout, busname, busname, lng, lat, speed, time"
//		 */
//		public void sendLogout(String busname, WsOutbound out, String video, String a, String b, String c, String d) throws IOException {
//			Double x = new Double(a) / 60;
//			Double y = new Double(b) / 60;
//			out.writeTextMessage(CharBuffer.wrap("busout," + busname + "," + video + "," + y + "," + x + "," + c + "," + d));
//			out.flush();
//		}
//
//		/***
//		 * 发送报警数据
//		 * @param alertArry[] {time, name, x, y, flag}
//		 * @param out
//		 * @param text
//		 * @param vehid
//		 * @throws IOException
//		 */
//		public void sendAlert(String[] alertArry, WsOutbound out, String text, String vehid) throws IOException {
//			out.writeTextMessage(CharBuffer.wrap("busalert," + alertArry[0] + "," + alertArry[1] + "," + (new Double(alertArry[2]) / 60) + ","
//					+ (new Double(alertArry[3]) / 60) + "," + alertArry[4] + "," + text + "," + vehid));
//			out.flush();
//		}
//	}
}