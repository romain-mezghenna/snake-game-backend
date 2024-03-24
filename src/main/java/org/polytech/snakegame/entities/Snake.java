package org.polytech.snakegame.entities;

import io.vertx.core.json.JsonObject;

public class Snake {

    private String username;
    private int x;
    private int y;
    private int velX; // 1 = right, -1 = left
    private int velY; // 1 = up, -1 = down
    private int score;
    private int[][] body;

    private boolean alive;

    public Snake(int x, int y, String username){
        this.x = x;
        this.y = y;
        this.username = username;
        this.score = 0;
        this.body = new int[100][2];
        this.alive = true;
        this.velX = 0;
        this.velY = 0;
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
        //Log.info("Changing velocities of " + this.username + " to " + json.getString("direction"));
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
        // Add a new element to the body of the player
        this.body[this.score][0] = this.x;
        this.body[this.score][1] = this.y;
        // Add score to the player
        this.score += score;

    }

    public void move(){
        // Move the player's head
        this.x += this.velX;
        this.y += this.velY;
        // Move the player's body (the first element of the body is taking the previous position of the head and so on)
        for(int i = this.score; i >= 0; i--){
            if(i == 0){
                this.body[i][0] = this.x - this.velX;
                this.body[i][1] = this.y - this.velY;
                break;
            }
            this.body[i][0] = this.body[i-1][0];
            this.body[i][1] = this.body[i-1][1];
        }


    }

    public boolean isAlive() {
        return this.alive;
    }

    public void kill() {
        this.alive = false;
    }


}
