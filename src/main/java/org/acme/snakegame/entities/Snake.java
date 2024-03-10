package org.acme.snakegame.entities;

import io.quarkus.logging.Log;
import io.vertx.core.json.JsonObject;

public class Snake {

    private String username;
    private int x;
    private int y;
    private int velX; // 1 = right, -1 = left
    private int velY; // 1 = up, -1 = down
    private int score;
    private int[][] body;

    public Snake(int x, int y, String username){
        this.x = x;
        this.y = y;
        this.velX = 0;
        this.velY = 0;
        this.username = username;
        this.score = 0;
        this.body = new int[100][2];
        this.body[0][0] = x;
        this.body[0][1] = y-1;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVelX() {
        return this.velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return this.velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int[][] getBody() {
        return this.body;
    }

    public void changeVelocities(JsonObject json){
        Log.info("Changing velocities of " + this.username + " to " + json.getString("direction"));
        if(json.getString("direction").equals("up")){
            this.velX = 0;
            this.velY = 1;
        } else if(json.getString("direction").equals("down")){
            this.velX = 0;
            this.velY = -1;
        } else if(json.getString("direction").equals("left")){
            this.velX = -1;
            this.velY = 0;
        } else if(json.getString("direction").equals("right")){
            this.velX = 1;
            this.velY = 0;
        }
    }

    public void addScore(int score){
        // Add score to the player
        this.score += score;
        // Add a new cell to the player's body in the direction of the last cell
        for(int i = 0; i < this.body.length; i++){
            if(this.body[i][0] == -1 && this.body[i][1] == -1){
                this.body[i][0] = this.body[i-1][0];
                this.body[i][1] = this.body[i-1][1];
                break;
            }
        }

    }

    public void move(){
        // Move the player
        this.x += this.velX;
        this.y += this.velY;
        // Move the player's body
        for(int i = 0; i < this.body.length; i++){
            if(this.body[i][0] != -1 && this.body[i][1] != -1){
                if(i == 0){
                    this.body[i][0] = this.x;
                    this.body[i][1] = this.y;
                } else {
                    int tempX = this.body[i][0];
                    int tempY = this.body[i][1];
                    this.body[i][0] = this.body[i-1][0];
                    this.body[i][1] = this.body[i-1][1];
                    this.body[i-1][0] = tempX;
                    this.body[i-1][1] = tempY;
                }
            }
        }
    }
}
