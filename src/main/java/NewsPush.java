import java.util.*;
import java.util.concurrent.Future;

import javax.websocket.Session;
public class NewsPush implements Runnable {

	private static NewsPush instance;
	private static Map<String, Session> sMap = new HashMap<String, Session>();
	private NewsPush() {}
	int count =0;
	public static synchronized void add(Session s) {
		sMap.put(s.getId(), s);
	}

	public static void initialize() {
		if (instance == null) {
			instance = new NewsPush();
			new Thread(instance).start();
		}
	}
	@Override
	public synchronized void run() {

		while (count<100) {
			try {
				Thread.sleep(1*10);
				for (String key : sMap.keySet()) {

					Session s = sMap.get(key); 

					if (s.isOpen()) {
						Date d = new Date(System.currentTimeMillis());
//						                        s.getBasicRemote().sendText("Sports Push "+d.toString());
						MyWebSocket.sendmessage(s,"News");
						count++;
//						Future<Void> deliveryTracker = s.getAsyncRemote().sendText("Sports Push time " + d.toString());
//						deliveryTracker.isDone(); //blocks
//						 s.getBasicRemote().getSendWriter().write("Sports Push"+d.toString());
//						s.getAsyncRemote().sendText("Push time " + d.toString());
					} else {
						sMap.remove(key);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

