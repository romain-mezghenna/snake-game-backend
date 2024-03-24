package org.polytech.compiler;

import org.polytech.snakegame.entities.Snake;

public class SnakeController {

    public void moveSnake0(Snake s) {
s.setVelX(-1);
    }

    public void moveSnake1(Snake s) {
s.setVelY(1);
    }

    public void moveSnake2(Snake s) {
s.setVelY(-1);
    }

    public void moveSnake3(Snake s) {
s.setVelX(1);
    }

}

