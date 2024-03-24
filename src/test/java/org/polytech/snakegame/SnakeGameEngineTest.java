package org.polytech.snakegame;

import org.junit.jupiter.api.BeforeAll;
import org.polytech.snakegame.entities.Snake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SnakeGameEngineTest {

    private static  Snake player1;
    private static Snake player2;

    @BeforeAll static void setUp(){
        //SnakeGameEngine.initGameForTest();
        player1 = SnakeGameEngine.addPlayer("Player1");
        player2 = SnakeGameEngine.addPlayer("Player2");
    }

    @BeforeEach void setUpEach(){
        //SnakeGameEngine.initGameForTest();
        player1 = SnakeGameEngine.getPlayerByUsername("Player1");
        player2 = SnakeGameEngine.getPlayerByUsername("Player2");
    }

    
    @Test
    public void testAddAndRemovePlayer() {
        Snake player3 = SnakeGameEngine.addPlayer("Player3");
        List<Snake> players = SnakeGameEngine.getPlayers();
        assertEquals(3, players.size());
        assertTrue(Arrays.asList(player1, player2,player3).containsAll(players));
        SnakeGameEngine.removePlayer(player3);
        players = SnakeGameEngine.getPlayers();
        assertEquals(2,players.size());
        assertTrue(Arrays.asList(player1, player2).containsAll(players));

    }

    @Test
    public void testGetPlayerByUsername() {

        Snake player = SnakeGameEngine.getPlayerByUsername("Player1");
        System.out.println(player);
        assertEquals(player1, player);

        player = SnakeGameEngine.getPlayerByUsername("Player3");
        assertNull(player);
        // Remove the player
        SnakeGameEngine.removePlayer(player);
    }

    @Test
    public void testMovePlayers() {
        Snake player1 = SnakeGameEngine.addPlayer("Player1");
        Snake player2 = SnakeGameEngine.addPlayer("Player2");

        SnakeGameEngine.movePlayers();

        List<Snake> players = SnakeGameEngine.getPlayers();
        for (Snake player : players) {
            assertTrue(player.isAlive());
        }
    }

    @Test
    public void testInitGame() {
        SnakeGameEngine.initGameForTest();

        assertEquals(0, SnakeGameEngine.getPlayers().size());
        // Checks if the foodboard is a matrix filed with -1
        int[][] foodboard = SnakeGameEngine.getFoodBoard();
        for (int i = 0;i<foodboard.length;i++){
            for(int j = 0; j<foodboard[0].length; j++){
                assertEquals(-1,foodboard[i][j]);
            }
        }
    }

    @Test
    public void testCheckWallCollisions() {
        Snake player3 = SnakeGameEngine.addPlayer("Player3");
        player3.setX(-1);

        SnakeGameEngine.checkWallCollisions();

        assertFalse(player3.isAlive());

        SnakeGameEngine.removePlayer(player3);
    }

    @Test
    public void testRemoveDeadSnakes() {
        player1.kill();
        SnakeGameEngine.removeDeadSnakes();
        assertEquals(1,SnakeGameEngine.getPlayers().size());
        player1 = SnakeGameEngine.addPlayer("Player1");
        
    }
}