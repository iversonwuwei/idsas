package com.zdtx.ifms.specific.web.monitor;



public  class SocketServlet /*extends HttpServlet*/ {

//	private static final long serialVersionUID = 1481704582404666627L;
//	
//	static Socket socket = null;
//	static InputStream brin = null;
//	static OutputStream pwout = null;
//	static SocketCreat socketCreat = null;
//	static String loginStr = SocketUtil.getLoginStr(SocketUtil.SOCKET_BASE_NAME, SocketUtil.SOCKET_BASE_PWD);
//	static long beginStamp;
//	static long endStamp;
//	
//	@Override
//	public void init() throws ServletException {
//		super.init();
//		if(SocketUtil.SOCKET_DOSEND.trim().equals("true")) {	//如果配置文件参数DOSEND为true，创建SocketCreat线程对象
//			socketCreat = new SocketCreat(); 
//		}
//	}
//	
//	// socket服务创建 连接 测试线程
//	static class SocketCreat extends Thread {
//		public SocketCreat() {
//			this.start();
//		}
//		@Override
//		public void run() {
//			super.run();
//			while (true) {
//				try {
//					Thread.sleep(30000l);
//				} catch (InterruptedException e1) {
//				}
//				if(socket == null) {
//					creatSocket();	//连接
//				}else {
//					try {
//						writeStr(loginStr);//验证socket
//					} catch (Exception e) {
//						creatSocket();
//					}
//				}
//				try {
//					Thread.sleep(280000l);
//				} catch (InterruptedException e1) {
//				}
//			}
//		}
//	}
//	
//	/***
//	 * 创建连接
//	 */
//	synchronized private static void creatSocket() {
//		beginStamp = System.currentTimeMillis();
//		try {
//		socket = new Socket(SocketUtil.SOCKET_IP, SocketUtil.SOCKET_PORT);
//		brin = socket.getInputStream();
//		pwout = socket.getOutputStream();
//		pwout.write(loginStr.getBytes());	//给socket服务器 发送登陆信息
//		pwout.flush();
//		String resStr = "";
//		while (true) {
//			resStr = SocketUtil.getStringByInputStream(brin);
//			/***
//			 * 取得登陆返回信息
//			 */
//			endStamp = System.currentTimeMillis();
//			if((endStamp - beginStamp) / 1000f > SocketUtil.SOCKET_TIMEOUT) {		//超时关闭socket
//				socket.close();
//				socket = null;
//				break;
//			}
//			if (SocketUtil.checkHeadAndTail(resStr)) {
//				/***
//				 * 验证登陆返回信息
//				 */
//				if (SocketUtil.checkLogin(resStr)) {
//					break;
//				} else {
//					socket = null;
//					break;
//				}
//			}
//		}
//		} catch (Exception e) {
//			socket = null;
//		}
//	}
//	/****
//	 * 发送socket
//	 * @param str
//	 * @return "nosocket" 无法连接 "timeout" 超时
//	 */
//	synchronized public static String sendsocket(String str) {
//		String resStr = "";
//		if (SocketUtil.SOCKET_DOSEND.trim().equals("true")) {	//如果配置文件参数DOSEND为true，创建SocketCreat线程对象
//			if (socket == null) {
//				creatSocket();
//			}
//			try {
//				resStr = writeStr(str);
//			} catch (IOException e) {
//				resStr = "nosocket";// 无法连接socket
//				socket = null;
//			}
//		} else {
//			resStr = "nosocket";// 无法连接socket
//		}
//		return resStr;
//	}
//	
//	/**
//	 * 刷socket
//	 * @param str
//	 * @return
//	 * @throws IOException
//	 */
//	synchronized public static String writeStr(String str) throws IOException {
//		String resStr = "";
//		beginStamp = System.currentTimeMillis();
//		pwout.write(str.getBytes());
//		pwout.flush();
//		while (true) {
//			resStr = SocketUtil.getStringByInputStream(brin);
//			endStamp = System.currentTimeMillis();
//			if ((endStamp - beginStamp) / 3000f > SocketUtil.SOCKET_TIMEOUT) {	// 超时暂时调整为1秒
//				resStr = "timeout";
//				break;// 超时
//			}
//			if (SocketUtil.checkHeadAndTail(resStr)) {
//				if (resStr.startsWith("$$|67")) {
//					break;// 返回
//				}
//			}
//		}
//		return resStr;
//	}
}