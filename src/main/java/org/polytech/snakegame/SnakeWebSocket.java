package org.polytech.snakegame;


import io.quarkus.logging.Log;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@ServerEndpoint("/game/{username}")
public class SnakeWebSocket {

    // Sessions map to hold all connected clients with their pseudos
    static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    static boolean initialized = false;


    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        if(username == null || username.isEmpty()){
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if(!initialized){
                // Launch the game engine on another thread
                Thread gameThread = new Thread(SnakeGameEngine::initGame);
                gameThread.start();
                initialized = true;
            }
            SnakeGameEngine.addPlayer(username);
            Log.info("User " + username + " is connected");
            sessions.put(username, session);
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        if(username != null && !username.isEmpty()){
            broadcast(new JsonObject().put("message", username + " left the game"));
            Log.info("User " + username + " is disconnected");
            sessions.remove(username);
            SnakeGameEngine.removePlayer(SnakeGameEngine.getPlayerByUsername(username));
            if(SnakeGameEngine.getPlayers().isEmpty()){
                initialized = false;
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username){
        System.out.println("Message from " + username + ": " + message);
        // Convert the received message to a JsonObject
        JsonObject messageJson = new JsonObject(message);
        // Understand the message and act accordingly
        if(messageJson.getString("action").equals("move")){
            // Get the player from the engine and change its direction
            if(SnakeGameEngine.getPlayerByUsername(username) != null){
                SnakeGameEngine.getPlayerByUsername(username).changeVelocities(messageJson);
            }
        } else if(messageJson.getString("action").equals("restart")){
            // Recreates an instance of a snake for the player
            if(SnakeGameEngine.getPlayerByUsername(username) == null){
                SnakeGameEngine.addPlayer(username);
            } else {
                try {
                    session.getBasicRemote().sendText(new JsonObject().put("action", "error").put("message", "This name is already taken").toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    // Method to broadcast a message to all connected clients
    public static void broadcast(JsonObject message) {
        sessions.values().forEach(s -> {
            try {
                s.getBasicRemote().sendText(message.toString());
               /* if(s.isOpen()){
                    s.getBasicRemote().sendText(message.toString());
                } else {
                    sessions.values().remove(s);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Method to broadcast to a single client
    public static void broadcastToUsername(String username, JsonObject message){
        try {
            if(sessions.get(username) != null && sessions.get(username).isOpen()){
                sessions.get(username).getBasicRemote().sendText(message.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to disconnect a client by its username
    public static void disconnectByUsername(String username){
        try {
            sessions.get(username).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}