package com.zdtx.ifms.specific.web.monitor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

/**
 * @description 新实时定位数据websocket Inbound对象(Java 6)
 * @author Liu Jun
 * @since 2014年9月3日 下午1:51:56
 */
@SuppressWarnings("deprecation")
public class RealtimeWsInbound extends MessageInbound {

	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
	}

	@Override
	protected void onTextMessage(CharBuffer arg0) throws IOException {
	}

	@Override
	protected void onOpen(WsOutbound outbound) {
		super.onOpen(outbound);
		RealtimeWsServerOld.clients.add(this);
	}
	
	@Override
	protected void onClose(int status) {
		RealtimeWsServerOld.clients.remove(this);
		super.onClose(status);
	}
}