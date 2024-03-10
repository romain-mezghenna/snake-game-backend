package org.acme.snakegame;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.acme.snakegame.entities.Snake;

import java.util.ArrayList;
import java.util.List;

public class SnakeGameEngine {

    private static List<Snake> players = new ArrayList<Snake>();

    private static int width = 80;
    private static int height = 60;

    private static boolean[][] gameBoard = new boolean[width][height];

    public static Snake addPlayer(String username){
        // Try to find a random position for the player
        int x = (int) (Math.random() * width);
        int y = (int) (Math.random() * height);
        boolean found = false;
        while(!found){
            if(gameBoard[x][y]){
                x = (int) (Math.random() * width);
                y = (int) (Math.random() * height);
            } else {
                found = true;
            }
        }
        Snake player = new Snake(x, y, username);
        players.add(player);
        gameBoard[x][y] = true;
        return player;
    }

    public static void removePlayer(Snake player){
        players.remove(player);
    }

    public static List<Snake> getPlayers(){
        return players;
    }

    public static Snake getPlayerByUsername(String username){
        for(Snake player : players){
            if(player.getUsername().equals(username)){
                return player;
            }
        }
        return null;
    }

    public static void movePlayers(){
        for(Snake player : players){
            player.move();
        }
    }

    public static void initGame(){
        players = new ArrayList<Snake>();
        gameBoard = new boolean[width][height];
        gameLoop();
    }

    public static void gameLoop(){
        while(true){
            movePlayers();
            // Check for collisions

            try {
                JsonObject message = getGameState();
                SnakeWebSocket.broadcast(message);
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static JsonObject getGameState(){
        JsonObject message = new JsonObject().put("action", "update");
        JsonArray playersArray = new JsonArray();
        for(Snake player : players){
            JsonObject playerJson = new JsonObject().put("x", player.getX()).put("y", player.getY()).put("username", player.getUsername()).put("score", player.getScore());
            playersArray.add(playerJson);
        }
        message.put("players", playersArray);
        return message;
    }




}
