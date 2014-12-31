package com.zdtx.ifms.specific.web.monitor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


public  class BaseSocketServlet extends HttpServlet {

	private static final long serialVersionUID = 1481704582404666627L;
	
	@Override
	public void init() throws ServletException {
//		super.init();
//		if(SocketUtil.setsend.trim().equals("true")) {	//如果配置文件参数dosend为true，创建SocketCreat线程对象
//			socketCreat = new SocketCreat(); 
//		}
	}
	
//	static Socket socket = null;
//	static InputStream brin = null;
//	static OutputStream pwout = null;
//	static SocketCreat socketCreat=null;
//	static String loginSocketStr = SocketUtil.loginStr(SocketUtil.socketbasename, SocketUtil.socketbasepwd);
//	static long a;
//	static String strtemp;
//	static long b;
	
	// socket服务创建 连接 测试线程
//	static class SocketCreat extends Thread {
//		public SocketCreat() {
//			this.start();
//		}
//		@Override
//		public void run() {
//			super.run();
//			while (true) {
//				try {
//					Thread.sleep(30000l);	//心跳频率
////					Thread.sleep(300000l);
//				} catch (InterruptedException e1) {
//				}
//				if(socket == null) {
//					creatSocket();//连接
//				}else {
//					try {
//						writeStr(loginSocketStr);//验证socket
//					} catch (Exception e) {
//						creatSocket();
//					}
//				}
//				try {
////					Thread.sleep(10000l);
//					Thread.sleep(280000l);
//				} catch (InterruptedException e1) {
//				}
//			}
//		}
//	}
	
	/***
	 * 创建连接
	 */
//	synchronized private static void creatSocket() {
//		a = System.currentTimeMillis();
//		try {
//		socket = new Socket(SocketUtil.socketip, SocketUtil.socketport);
////		socket = new Socket("192.168.80.110", 8060);//自己
//		brin = socket.getInputStream();
//		pwout = socket.getOutputStream();
//		pwout.write(loginSocketStr.getBytes());//给socket服务器 发送登陆信息
//		pwout.flush();
//		while (true) {
//			strtemp = SocketUtil.getStringByInputStream(brin);
//			/***
//			 * 取得登陆返回信息
//			 */
//			b=System.currentTimeMillis();
//			if((b-a)/1000f >  SocketUtil.logintimeout){
//				socket.close();
//				socket=null;
//				break;
//			}
//			if (SocketUtil.stringAllCheck(strtemp)) {
//				/***
//				 * 验证登陆返回信息
//				 */
//				if (SocketUtil.logincheck2(strtemp)) {
//					break;
//				} else {
//					socket=null;
//					break;
//				}
//			}
//		}
//		} catch (Exception e) {
//			socket=null;
//		}
//	}
	
	/****
	 * 发送socket
	 * @param str
	 * @return "nosocket" 无法连接 "timeout" 超时
	 */
//	synchronized public static String sendsocket(String str) {
//		strtemp = "";
//		if (SocketUtil.setsend.trim().equals("true")) {
//			if (socket == null) {
//				creatSocket();
//			}
//			try {
//				strtemp = writeStr(str);
//			} catch (IOException e) {
//				strtemp = "nosocket";// 无法连接socket
//				socket = null;
//			}
//		} else {
//			strtemp = "nosocket";// 无法连接socket
//		}
//		return strtemp;
//	}
	
	/**
	 * 刷socket
	 * @param str
	 * @return
	 * @throws IOException
	 */
//	synchronized public static String writeStr(String str) throws IOException {
//		strtemp = "";
//		a = System.currentTimeMillis();
//		pwout.write(str.getBytes());
//		pwout.flush();
//		while (true) {
//			strtemp = SocketUtil.getStringByInputStream(brin);
//			b = System.currentTimeMillis();
//			if ((b - a) / 3000f > SocketUtil.sockettimeout) {// 超时暂时调整为1秒
//				strtemp = "timeout";
//				break;// 超时
//			}
//			if (SocketUtil.stringAllCheck(strtemp)) {
//				if (strtemp.startsWith("$$|67")) {
//					break;// 返回
//				}
//			}
//		}
//		return strtemp;
//	}
}