
import java.io.IOException;    
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
@ServerEndpoint("/endpoint")
public class MyWebSocket {
	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onOpen::" + session.getId());
		HashMap<String,String> map=new HashMap<String,String>();//Creating HashMap    
		map.put("Sport","SportsPush");  //Put elements in Map  
		map.put("Time","PushTimeService");
		map.put("News", "NewsPush");
		map.put("Media", "MediaPush");
//		map.put(3,"Banana");   
//		map.put(4,"Grapes");   
		///////////////////////////////////////////////////////////////////////////// 
		// Access request parameters from URL query String.
		// If a client subscribes, add Session to PushTimeService. 
		//
		Map<String, List<String>> params = session.getRequestParameterMap();

		//        if (params.get("push") != null && (params.get("push").get(0).equals("TIME"))) {
		//            
		//          PushTimeService.initialize();
		//          PushTimeService.add(session);
		//        }
		/////////////////////////////////////////////////////////////////////////////
		//        System.out.print("subscribed "+ params.get("push").get(0));
		String str = params.get("push").get(0);
		String[] arrOfStr = str.split(",");
//		System.out.println(arrOfStr[0]);
//		System.out.println(arrOfStr[1]);
//		System.out.println(arrOfStr[2]);
		if (params.get("push") != null) {
			for(int i=1;i<arrOfStr.length;i++)
			{
				if(map.containsKey(arrOfStr[i]))
				{
					System.out.println("Exists"+map.get(arrOfStr[i]).getClass().getSimpleName());
					
					if(map.get(arrOfStr[i])=="SportsPush")
					{
					System.out.println("Sports Push");
					SportsPush.initialize();
					SportsPush.add(session);
					}
					else if(map.get(arrOfStr[i]).equals("PushTimeService"))
					{
						System.out.println("Push time service");  
						PushTimeService.initialize();
						PushTimeService.add(session);
					}
					else if(map.get(arrOfStr[i]).equals("NewsPush"))
					{
						System.out.println("News time service");  
						NewsPush.initialize();
						NewsPush.add(session);
					}
					else if(map.get(arrOfStr[i]).equals("Media"))
					{
						System.out.println("Push time service");  
						MediaPush.initialize();
						MediaPush.add(session);
					}
			}
		}
	}
	}
	@OnClose
	public void onClose(Session session) {
		System.out.println("onClose::" +  session.getId());
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("onMessage::From=" + session.getId() + " Message=" + message);

		try {
			session.getBasicRemote().sendText("Hello Client " + session.getId() + "!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnError
	public void onError(Throwable t) {
		System.out.println("onError::" + t.getMessage());
	}
	public static synchronized void sendmessage(Session s, String d) throws IOException
	{
		s.getBasicRemote().sendText("Push time " + d);
	}
}