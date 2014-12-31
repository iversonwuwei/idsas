package com.zdtx.ifms.specific.web.monitor;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.log4j.Logger;

import com.zdtx.ifms.common.utils.SocketUtil;
import com.zdtx.ifms.common.utils.Utils;

/**
 * @description 新刷卡数据websocket service端(Java 6)
 * @author Liu Jun
 * @since 2014年9月3日 下午3:10:45
 */
@SuppressWarnings("deprecation")
@WebServlet(name = "card", urlPatterns="/websocket/card_old", loadOnStartup = 6)
public class CardWsServerOld extends WebSocketServlet {

	private static final long serialVersionUID = -5673502241506392907L;

	private static final Logger LOGGER = Logger.getLogger(CardWsServerOld.class);
	
	protected static final Map<String, CardWsInbound> sessionMap = new ConcurrentHashMap<String, CardWsInbound>();
	
	@Override
	protected StreamInbound createWebSocketInbound(String str, HttpServletRequest request) {
		return new CardWsInbound(request.getParameter("id"));
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		if (SocketUtil.SOCKET_CARD_ISOPEN.equals("true")) {
			System.out.println("CardWsServer6: start up!");
			new Thread(new ReceiveWatchDog()).start();
		}
	}

	/**
	 * @description 创建socket server,并处理数据线程(IO)
	 * @author Liu Jun
	 * @since 2014年8月22日 上午11:31:31
	 */
	class ReceiveWatchDog implements Runnable {
		
		private ServerSocket serverSocket = null;
		private Socket socket = null;
		private BufferedReader reader = null;
		
		public void run() {
			try {
				serverSocket = new ServerSocket(SocketUtil.SOCKET_CARD_PORT);
				while (true) {
					try {
						socket = serverSocket.accept();
						reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String resStr = "";
						while (true) {
							resStr = reader.readLine();
							if (!Utils.isEmpty(resStr)) {
								sendData(resStr);
							} else {
								break;
							}
						}
						reader.close();
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if(socket != null) {
							try {
								socket.close();
								socket = null;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {  
				try {
					if (serverSocket != null) {
						serverSocket.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @description 创建socket server,并处理数据线程(NIO)
	 * @author Liu Jun
	 * @since 2014年8月22日 上午11:31:31
	 */
	class NReceiveWatchDog implements Runnable {
		
		Selector selector = null;  
        ServerSocketChannel serverSocketChannel = null;
        
		public void run() {
			try {
				selector = Selector.open();
				serverSocketChannel = ServerSocketChannel.open();  
	            serverSocketChannel.configureBlocking(false);	//非阻塞socket
	            serverSocketChannel.socket().setReuseAddress(true);	//可再利用地址
	            serverSocketChannel.socket().bind(new InetSocketAddress(SocketUtil.SOCKET_CARD_PORT));
	            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);	//注册监听accept事件
				while (selector.select() > 0) {
					Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
					while (iterator.hasNext()) {
						SelectionKey readyKey = iterator.next();
						iterator.remove();
						execute((ServerSocketChannel) readyKey.channel());
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {  
				try {
					selector.close();
					if (serverSocketChannel != null) {
						serverSocketChannel.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void execute(ServerSocketChannel serverSocketChannel) throws IOException {  
        SocketChannel socketChannel = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            socketChannel = serverSocketChannel.accept();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            byte[] bytes;  
            int size = 0;  
            while ((size = socketChannel.read(buffer)) >= 0) {  
            	buffer.flip();  
            	bytes = new byte[size];  
            	buffer.get(bytes);  
            	baos.write(bytes);  
            	buffer.clear();
            }
            sendData(baos.toString("GBK"));
        } finally {
            try {
                socketChannel.close();
            } catch(Exception ex) {}
        }
    }
		
	/**
	 * 发送webSocket数据
	 * @param dataStr 数据串
	 */
	private static void sendData(String dataStr) {
		String[] dataArr = dataStr.split(":");
		CharBuffer buffer = CharBuffer.wrap(dataArr[1]);
		WsOutbound outbound = null;
		if(sessionMap.containsKey(dataArr[0]) && sessionMap.get(dataArr[0]) != null) {
			try {
				outbound = sessionMap.get(dataArr[0]).getWsOutbound();
				outbound.writeTextMessage(buffer);
				outbound.flush();
			} catch (IOException e) {
				LOGGER.error("Failed to send alert data to client: " + sessionMap.get(dataArr[0]) + ", remove it!", e);
				e.printStackTrace();
				try {
					outbound.close(0, null);
				} catch (IOException e1) {
				}
			}
		}
	}
}