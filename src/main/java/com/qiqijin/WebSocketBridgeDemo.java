package com.qiqijin;

import java.net.URISyntaxException;
import java.util.List;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

public class WebSocketBridgeDemo extends WebSocketBridge {

    public WebSocketBridgeDemo(String appName, String serverPort) throws URISyntaxException {
	super(appName, serverPort);
    }


    @Override
    public void onMessage(List<String> data) {
        switch (data.get(0)) {
	    case "value":
                System.out.println(data.get(1));
		break;
            case "message":
                this.runInEmacs("message", data.get(1));
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
