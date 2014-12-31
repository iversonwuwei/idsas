package com.zdtx.ifms.specific.service.map;


/***
 * 此线程用于接收socket 发送的信息，以及通过websocket将信息发送到页面上
 * 
 * @author a
 * 
 */
public class Getsocket/* extends Thread*/ {
//	public Socket socket;
//	private boolean test = true;
//	public WsOutbound out = null;
//	public InputStream brin;
//	public OutputStream pwout;
//	private String busname = null;
//	private Set<String> busset = null;
//
//	public Getsocket() {
//	}
//
//	public Getsocket(Socket s, InputStream brin, OutputStream pwout,
//			WsOutbound wsOutbound) {
//		socket = s;
//		this.out = wsOutbound;
//		try {
//			this.brin = brin;
//			this.pwout = pwout;
//			busset = new HashSet<String>();
//			start();
//		} catch (Exception e) {
//		}
//	}
//
//	public void run() {
//		try {
//			// while(test){
//			// String str = SocketUtil.getStringByInputStream(brin);
//			// if(str!=null && !"".equals(str)){
//			// }
//			// }
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public boolean isTest() {
//		return test;
//	}
//
//	public void setTest(boolean test) {
//		this.test = test;
//	}
//
//	public String getBusname() {
//		return busname;
//	}
//
//	public void setBusname(String busname) {
//		this.busname = busname;
//	}
//
//	public void sendOut(String[] gpsArr, WsOutbound out) throws IOException {
//		Double x = new Double(gpsArr[6]) / 60;
//		Double y = new Double(gpsArr[7]) / 60;
//		Double jiao = new Double(gpsArr[9]) / 10;
//		out.writeTextMessage(CharBuffer.wrap("bus," + gpsArr[3] + "," + y + ","
//				+ x + "," + gpsArr[29] + "," + gpsArr[2] + "," + gpsArr[13]
//				+ "," + gpsArr[14] + "," + gpsArr[21] + "," + gpsArr[8] + ","
//				+ (int) Math.ceil(jiao) + "," + gpsArr[9] + ","
//				+ DateUtil.formatLongTimeDate(new Date())));
//		out.flush();
//	}
//
//	public void sendBuslist(WsOutbound out) throws IOException {
//		List<String> buslist = new ArrayList<String>(busset);
//		String str = "list,";
//		if (buslist != null && buslist.size() != 0) {
//			for (int i = 0; i < buslist.size(); i++) {
//				str += buslist.get(i) + ",";
//			}
//		}
//		out.writeTextMessage(CharBuffer.wrap(str));
//		out.flush();
//	}
}