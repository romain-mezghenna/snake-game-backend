package org.acme.snakegame;


import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
@ServerEndpoint("/game")
public class SnakeWebSocket {

    private static final CopyOnWriteArrayList<Session> sessions = new CopyOnWriteArrayList<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Broadcast the received message to all clients except the sender
        for (Session s : sessions) {
            if (!s.equals(session)) {
                try {
                    s.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to broadcast a message to all connected clients
    public static void broadcast(JsonObject message) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}