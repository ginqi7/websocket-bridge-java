package com.qiqijin.websocketBridge;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

/**
 * Hello world!
 *
 */
public class WebSocketBridge extends WebSocketClient
{
    private String appName;
    private String serverPort;
    private String url;

    public WebSocketBridge(String appName, String serverPort) throws URISyntaxException {
        super(new URI(String.format("ws://localhost:%s", serverPort)));
        this.url = String.format("ws://localhost:%s", serverPort);
        this.appName = appName;
        this.serverPort = serverPort;
    }


    public WebSocketBridge(URI serverURI) {
        super(serverURI);
    }

    public WebSocketBridge(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.printf("WebSocket Client %s connected, the server port is %s\n", this.appName, this.serverPort);
        Map<String, String> message = new HashMap<>();
        message.put("type", "client-app-name");
        message.put("content", this.appName);
        Gson gson = new Gson();
        String jsonString = gson.toJson(message);
        send(jsonString);
    }

    @Override
    public void onMessage(String message) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(message, JsonArray.class);
        JsonElement secondElement = jsonArray.get(1);
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> values = gson.fromJson(secondElement, listType);
        onMessage(values);
    }

    public void onMessage(List<String> data) {
        System.out.println("You should implement public void onMessage(List<String> data)");
    }

    public void evalInEmacs(String code) {
        Map<String, String> message = new HashMap<>();
        System.out.println(code);
        message.put("type", "eval-code");
        message.put("content", code);
        Gson gson = new Gson();
        String jsonString = gson.toJson(message);
        send(jsonString);
    }

    private String argFormat (String arg) {
        return String.format("\"%s\"", arg);
    }

    public void runInEmacs(String func, String... args) {
        if (args.length == 0) {
            evalInEmacs(String.format("(%s)", func));
        }
        evalInEmacs(String.format("(%s %s)",
            func,
            Arrays.asList(args).stream()
            .map(this::argFormat)
            .collect(Collectors.joining(" ")))) ;
    }

    public void runInEmacs(String func, Map<String, String> args) {
        evalInEmacs(String.format("(%s %s)",
            func,
            args.entrySet().stream().map(entry -> String.format("'%s \"%s\"", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(" "))
        )) ;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println(
            "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
            + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }
}
