package org.polytech.snakegame;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@QuarkusTest
public class SnakeWebSocketTest {

    @Test void testOnOpen(){
        SnakeWebSocket snakeWebSocket = new SnakeWebSocket();
        Session session1 = Mockito.mock(Session.class);
        Session session2 = Mockito.mock(Session.class);

        RemoteEndpoint.Basic remote1 = Mockito.mock(RemoteEndpoint.Basic.class);
        RemoteEndpoint.Basic remote2 = Mockito.mock(RemoteEndpoint.Basic.class);
        when(session1.getBasicRemote()).thenReturn(remote1);
        when(session2.getBasicRemote()).thenReturn(remote2);
        SnakeWebSocket.initialized = true;
        snakeWebSocket.onOpen(session1, "user1");
        snakeWebSocket.onOpen(session2, "user2");
        assertNotNull(SnakeWebSocket.sessions.get("user2"));
    }

    @Test void testOnEmptyUsername(){
        SnakeWebSocket snakeWebSocket = new SnakeWebSocket();
        Session session = Mockito.mock(Session.class);
        RemoteEndpoint.Basic remote = Mockito.mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(remote);
        snakeWebSocket.onOpen(session, "");
    }

    @Test void testOnClose(){
        SnakeWebSocket snakeWebSocket = new SnakeWebSocket();
        Session session1 = Mockito.mock(Session.class);
        Session session2 = Mockito.mock(Session.class);

        RemoteEndpoint.Basic remote1 = Mockito.mock(RemoteEndpoint.Basic.class);
        RemoteEndpoint.Basic remote2 = Mockito.mock(RemoteEndpoint.Basic.class);
        when(session1.getBasicRemote()).thenReturn(remote1);
        when(session2.getBasicRemote()).thenReturn(remote2);
        SnakeWebSocket.initialized = true;
        snakeWebSocket.onOpen(session1, "user1");
        snakeWebSocket.onClose(session1, "user1");
        assertEquals(0, SnakeWebSocket.sessions.size());
    }

    @Test void testOnBroadcast(){
        SnakeWebSocket snakeWebSocket = new SnakeWebSocket();
        Session session1 = Mockito.mock(Session.class);
        Session session2 = Mockito.mock(Session.class);

        RemoteEndpoint.Basic remote1 = Mockito.mock(RemoteEndpoint.Basic.class);
        RemoteEndpoint.Basic remote2 = Mockito.mock(RemoteEndpoint.Basic.class);
        when(session1.getBasicRemote()).thenReturn(remote1);
        when(session2.getBasicRemote()).thenReturn(remote2);
        SnakeWebSocket.initialized = true;
        snakeWebSocket.onOpen(session1, "user1");

        // Test broadcast
        JsonObject message = new JsonObject().put("testOnBroadCast", "test");
        SnakeWebSocket.broadcast(message);
        try {
            Mockito.verify(remote1).sendText(message.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        snakeWebSocket.onClose(session1, "user1");
    }


}
