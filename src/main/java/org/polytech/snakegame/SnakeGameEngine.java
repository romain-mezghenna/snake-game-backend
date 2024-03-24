package org.polytech.snakegame;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.polytech.snakegame.entities.Snake;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SnakeGameEngine {

    private static List<Snake> players = new CopyOnWriteArrayList<Snake>();

    static final int width = 40;
    static final int height = 40;

    // Fill with -1 to indicate that there is no food
    private static int[][] foodBoard = new int[width][height];


    public static Snake addPlayer(String username){
        // Try to find a random position for the player
        int x = (int) (Math.random() * width);
        int y = (int) (Math.random() * height);
        Snake player = new Snake(x, y, username);
        players.add(player);
        // Add one food to the board
        addFood();
        return player;
    }

    public static void addFood(){
        int x = (int) (Math.random() * width);
        int y = (int) (Math.random() * height);
        boolean found = false;
        // Get a random int from 1 to 7
        int random = (int) (Math.random() * 7) + 1;
        while(!found){
            if(foodBoard[x][y] != -1){
                foodBoard[x][y] = random;
                found = true;
            } else {
                x = (int) (Math.random() * width);
                y = (int) (Math.random() * height);
            }
        }
    }

    public static void removeFood(){
        int x = (int) (Math.random() * width);
        int y = (int) (Math.random() * height);
        boolean found = false;
        while(!found){
            if(foodBoard[x][y] != -1){
                foodBoard[x][y] = -1;
                found = true;
            } else {
                x = (int) (Math.random() * width);
                y = (int) (Math.random() * height);
            }
        }
    }

    public static void removePlayer(Snake player){
        // Remove the food from the board
        removeFood();
        // Remove the player from the list
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

    public static void initGameForTest(){
        players = new CopyOnWriteArrayList<>();
        foodBoard = new int[width][height];
        for (int i = 0; i < width; i++) {
            Arrays.fill(foodBoard[i], -1);
        }
    }

    public static void initGame(){
        players = new CopyOnWriteArrayList<>();
        foodBoard = new int[width][height];
        for (int i = 0; i < width; i++) {
            Arrays.fill(foodBoard[i], -1);
        }
        gameLoop();
    }

    public static void gameLoop(){
        while(true){
            // Update the positions of the players
            movePlayers();
            // Check if the players have collided with the walls
            checkWallCollisions();
            // Check if the players have collided with each other
            checkPlayerCollisions();
            // Remove the dead players
            removeDeadSnakes();
            // Checks if the players have eaten food
            checkFoodEating();

            //updateGameBoard();
            try {
                JsonObject message = getGameState();
                for (Snake player : players) {
                    SnakeWebSocket.broadcastToUsername(player.getUsername(), message);
                }
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void checkWallCollisions(){
        for(Snake player : players){
            if(player.getX() < 0 || player.getX() >= width || player.getY() < 0 || player.getY() >= height){
               player.kill();
            }
        }
    }

    public static void checkPlayerCollisions(){
        for(Snake player : players){
            if(player.isAlive()){
                // Check if the head of player collided with its body
                for(int i = 0; i < player.getScore(); i++){
                    if(player.getX() == player.getBody()[i][0] && player.getY() == player.getBody()[i][1]){
                        player.kill();
                    }
                }
                for(Snake otherPlayer : players){
                    if(!player.getUsername().equals(otherPlayer.getUsername())){
                        if(player.getVelX() == - otherPlayer.getVelX() || player.getVelY() == - otherPlayer.getVelY()){
                            if((player.getX() == otherPlayer.getX() -1 || player.getX() + 1 == otherPlayer.getX())  && player.getY() == otherPlayer.getY()){
                                player.kill();
                                otherPlayer.kill();
                            } else if ((player.getY() == otherPlayer.getY() -1 || player.getY() + 1 == otherPlayer.getY()) && player.getX() == otherPlayer.getX()){
                                player.kill();
                                otherPlayer.kill();
                            }
                        }
                       // Check if the head of player collided with the body of otherPlayer or its head
                        for(int i = 0; i < otherPlayer.getScore(); i++){
                            if(player.getX() == otherPlayer.getBody()[i][0] && player.getY() == otherPlayer.getBody()[i][1]){
                                player.kill();
                            }
                        }
                        if(player.getX() == otherPlayer.getX() && player.getY() == otherPlayer.getY()){
                            player.kill();
                        }
                    }
                }
            }
        }
    }

    public static void removeDeadSnakes(){
        for(Snake player : players){
            if(!player.isAlive()){
                // Remove the player from the player list
                players.remove(player);
                // Broadcast to the client the game over message
                SnakeWebSocket.broadcastToUsername(player.getUsername(), new JsonObject().put("action", "gameover").put("username", player.getUsername()));
            }
        }

    }

    public static void checkFoodEating(){
        for(Snake player : players){
            if(foodBoard[player.getX()][player.getY()] != -1){
                player.addScore(1);
                foodBoard[player.getX()][player.getY()] = -1;
                // Add one food to the board
                addFood();
            }
        }
    }

    public static JsonObject getGameState(){
        JsonObject message = new JsonObject().put("action", "update");
        JsonArray playersArray = new JsonArray();
        for(Snake player : players){
            JsonObject playerJson = new JsonObject().put("x", player.getX()).put("y", player.getY()).put("username", player.getUsername()).put("score", player.getScore());
            // Convert the body to an array of coordinates
            JsonArray bodyArray = new JsonArray();
            for(int i = 0; i < player.getScore(); i++){
                bodyArray.add(new JsonObject().put("x", player.getBody()[i][0]).put("y", player.getBody()[i][1]));
            }
            playerJson.put("body", bodyArray);
            playersArray.add(playerJson);
        }
        message.put("players", playersArray);
        // Convert the food board to an array of coordinates
        JsonArray foodArray = new JsonArray();
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(foodBoard[i][j] != -1){
                    foodArray.add(new JsonObject().put("x", i).put("y", j).put("type", foodBoard[i][j]));
                }
            }
        }
        message.put("food", foodArray);
        return message;
    }

    public static int[][] getFoodBoard() {
        return foodBoard;
    }
}
