package com.qiqijin.websocketBridge;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebSocketBridgeDemo extends WebSocketBridge {

    public WebSocketBridgeDemo(String appName, String serverPort) throws URISyntaxException {
	super(appName, serverPort);
    }


    @Override
    public void onMessage(List<String> data) {
        System.out.println(data);
        switch (data.get(0)) {
	    case "value":
                System.out.println(data.get(1));
		break;
            case "message":
                this.runInEmacs("message", data.get(1));
                break;
            case "plist":
                this.runInEmacs("message", data.get(1));
                Map<String, String> map = new HashMap<>();
                map.put(":name", "Java");
                map.put(":id", "3.1415926");
                this.runInEmacs("java-demo-plist-message", map);
                break;
	    default:
		break;
        }
    }

    public static void main(String[] args) throws URISyntaxException {
        String appName = args[0];
        String serverPort = args[1];
        WebSocketBridgeDemo c = new WebSocketBridgeDemo(appName, serverPort); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
        c.connect();
    }
}
