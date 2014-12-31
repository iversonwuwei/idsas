package com.zdtx.ifms.specific.service.map;


public class MySocket /*extends MessageInbound*/ {
	/*WsOutbound out = this.getWsOutbound();
	HttpSession httpSession = null;
	Map<String, MySocket> mysockets = null;
	String ids = null;
	Set<String> busset = null;
	Map<String, String> busmap = new HashMap<String, String>();

	public MySocket() {}

	@SuppressWarnings("unchecked")
	public MySocket(Map<String, MySocket> mysockets, String ids, HttpSession httpSession) {
		this.mysockets = mysockets;
		this.ids = ids;
		this.httpSession = httpSession;
		busset = ((Map<String, String>) Struts2Util.getSession().getAttribute("userVehicle")).keySet();
		busmap = (Map<String, String>) Struts2Util.getSession().getAttribute("busmap");
	}

	@Override
	protected void onTextMessage(CharBuffer arg0) throws IOException {}

	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {}

	@Override
	protected void onClose(int status) {
		mysockets.remove(ids);
		super.onClose(status);
	}

	public Set<String> getBusset() {
		return busset;
	}

	public void setBusset(Set<String> busset) {
		this.busset = busset;
	}

	public void sendBuslist(WsOutbound out) throws IOException {
	}

	@Override
	protected void onOpen(WsOutbound outbound) {
		super.onOpen(outbound);
		mysockets.put(ids, this);
	}

	public Map<String, String> getBusmap() {
		return busmap;
	}

	public void setBusmap(Map<String, String> busmap) {
		this.busmap = busmap;
	}*/
}