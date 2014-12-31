package com.zdtx.ifms.specific.web.monitor;


//@WebServlet(name = "card", urlPatterns = "/card.do", loadOnStartup = 8)
public class CardWSServlet /*extends WebSocketServlet*/ {

//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 5779506656080698710L;
//	Map<String, CardMsg> mysockets = null;
//	private ServerSocket ss = null;
//	SocketServer sockets = null;
//
//	@Override
//	public void init() throws ServletException {
//		super.init();
//
//		mysockets = new HashMap<String, CardMsg>();
//		if (SocketUtil.opencard.trim().equals("true")) {
//			sockets = new SocketServer();
//		}
//	}
//
//	@Override
//	protected StreamInbound createWebSocketInbound(String arg0,
//			HttpServletRequest request) {
//		CardMsg cardMsg = new CardMsg(mysockets,
//				(String) request.getParameter("ids"), request.getSession());
//		return cardMsg;
//	}
//
//	private void creatSocketServer() {
//		try {
//			ss = new ServerSocket(SocketUtil.socketcardport);
//		} catch (IOException e) {
//		}
//	}
//
//	class SocketServer extends Thread {
//		public SocketServer() {
//			this.start();
//		}
//
//		@Override
//		public void run() {
//			super.run();
//			while (true) {
//				if (ss == null) {
//					creatSocketServer();
//				} else {
//					try {
//						Socket socket = ss.accept();
//						new ServerOJ(socket);
//					} catch (IOException e) {
//						creatSocketServer();
//					}
//				}
//			}
//
//		}
//	}
//
//	class ServerOJ extends Thread {
//		private Socket socket;
//		private BufferedReader bdq;
//
//		public ServerOJ() {
//		}
//
//		public ServerOJ(Socket s) {
//			socket = s;
//			try {
//				bdq = new BufferedReader(new InputStreamReader(
//						socket.getInputStream()));
//				start();
//			} catch (Exception e) {
//			}
//
//		}
//
//		public void run() {
//			try {
//				while (true) {
//					String str = bdq.readLine();
//					if (str != null && !"".equals(str)) {
//						String[] tary = str.split(":");
//						CardMsg c = mysockets.get(tary[0]);
//						if (c != null) {
//							WsOutbound out = c.getWsOutbound();
//							out.writeTextMessage(CharBuffer.wrap(tary[1]));
//							out.flush();
//						}
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					bdq.close();
//					socket.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
}