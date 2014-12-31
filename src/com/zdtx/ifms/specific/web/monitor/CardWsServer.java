package com.zdtx.ifms.specific.web.monitor;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;

import org.apache.log4j.Logger;

import com.zdtx.ifms.common.utils.SocketUtil;
import com.zdtx.ifms.common.utils.Utils;

/**
 * @description 新刷卡数据websocket service端(Java 7)
 * @author Liu Jun
 * @since 2014年9月3日 下午3:14:29
 */
//@ServerEndpoint(value = "/websocket/card/{id}")
//@WebServlet(name = "card", urlPatterns="/card", loadOnStartup = 10)
public class CardWsServer extends HttpServlet {

	private static final long serialVersionUID = 1260679459367463052L;
	
	private static final Logger LOGGER = Logger.getLogger(CardWsServer.class);
	
	private static final Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();
	
	@Override
	public void init() throws ServletException {
		super.init();
		if (SocketUtil.SOCKET_CARD_ISOPEN.equals("true")) {
			System.out.println("CardWsServer7: start up!");
			new Thread(new ReceiveWatchDog()).start();
		}
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("id") String id) {
		sessionMap.put(id, session);
		System.out.println("ID: " + id + " join to card websocket7!");
		LOGGER.info("ID: " + id + " join to card websocket7!");
	}

	@OnMessage
	public void onMessage(String message, Session session) {
	}

	@OnClose
	public void onClose(Session session, @PathParam("id") String id) {
		sessionMap.remove(id);
		System.out.println("ID: " + id + " remove from card websocket7!");
		LOGGER.info("ID: " + id + " remove from card websocket7!");
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
		if(sessionMap.containsKey(dataArr[0]) && sessionMap.get(dataArr[0]) != null) {
			try {
				sessionMap.get(dataArr[0]).getBasicRemote().sendText(dataArr[1]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}