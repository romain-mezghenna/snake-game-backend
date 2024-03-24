package org.polytech.snakegame.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SnakeTest {

    private Snake snake;

    @BeforeEach void setUp(){
        snake = new Snake(1,1,"Loxy");
    }

    @Test void testSnakeConstructor(){
        assertAll("Snake should be initialized with the correct values",
            () -> assertEquals(1, snake.getX()),
            () -> assertEquals(1, snake.getY()),
            () -> assertEquals("Loxy", snake.getUsername())
        );
    }

    @Test void testGetterSetter(){
        snake.setX(2);
        snake.setY(2);
        snake.setUsername("Loxy2");
        snake.setScore(1);
        snake.setVelY(1);
        snake.setVelX(1);
        snake.kill();
        assertAll("Snake should be updated with the correct values",
            () -> assertEquals(2, snake.getX()),
            () -> assertEquals(2, snake.getY()),
            () -> assertEquals("Loxy2", snake.getUsername()),
            () -> assertEquals(1, snake.getScore()),
            () -> assertFalse(snake.isAlive())
        );
    }

    @Test void testMoveSnake(){
        // We set the X velocity to 1
        // The snake should be in 2,1 after a move
        snake.setVelX(1);
        snake.move();
        assertEquals(2, snake.getX());
    }
}
