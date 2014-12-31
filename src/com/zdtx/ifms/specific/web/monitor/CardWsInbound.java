package com.zdtx.ifms.specific.web.monitor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.log4j.Logger;

/**
 * @description 新刷卡数据websocket Inbound对象(Java 6)
 * @author Liu Jun
 * @since 2014年9月3日 下午1:51:56
 */
@SuppressWarnings("deprecation")
public class CardWsInbound extends MessageInbound {
	
	private static final Logger LOGGER = Logger.getLogger(CardWsInbound.class);

	private String id;
	
	public CardWsInbound(String id) {
		this.id = id;
	}
	
	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
	}

	@Override
	protected void onTextMessage(CharBuffer arg0) throws IOException {
	}

	
	@Override
	protected void onOpen(WsOutbound outbound) {
		super.onOpen(outbound);
		CardWsServerOld.sessionMap.put(id, this);
		System.out.println("ID: " + id + " join to card websocket6!");
		LOGGER.info("ID: " + id + " join to card websocket6!");
	}
	
	@Override
	protected void onClose(int status) {
		CardWsServerOld.sessionMap.remove(id);
		LOGGER.info("ID: " + id + " remove from card websocket6!");
		super.onClose(status);
	}
}
