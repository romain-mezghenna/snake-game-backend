package org.acme.snakegame;

import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/snake")
public class SnakeGameResource {

    @Inject
    SnakeWebSocket snakeWebSocket;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus!";
    }

    // Example endpoint to broadcast a message to all connected clients
    @GET
    @Path("/broadcast")
    public String broadcastMessage() {
        SnakeWebSocket.broadcast(new JsonObject().put("message", "Hello from the server!"));
        return "Message broadcasted!";
    }
}
