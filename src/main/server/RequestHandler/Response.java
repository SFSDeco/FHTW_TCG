package main.server.RequestHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;
import main.logic.models.User;

public class Response {
    private final Request incomingRequest;
    private final PrintStream out;

    public Response(Request request, Socket socket) throws IOException {
        this.incomingRequest = request;
        this.out = new PrintStream(socket.getOutputStream());
    }

    public void handleResponse(){
        String method = incomingRequest.getHTTPMethod();
        switch(method){
            case "GET":
                this.getHandler();
                break;
            case "POST":
                this.postHandler();
                break;
            case "PUT":
                this.putHandler();
                break;
            case "DELETE":
                this.deleteHandler();
                break;

        }
    }

    public void getHandler(){
        String path = incomingRequest.getRequestPath();
        if(path.startsWith("/users")) path = "/users";
        switch(path){
            case "/cards":
                this.respond("Got Cards", "200 OK");
                break;
            case "/deck":
                this.respond("Got Deck", "200 OK");
                break;
            case "/stats":
                this.respond("Got Stats", "200 OK");
                break;
            case "/scoreboard":
                this.respond("Got Scoreboard", "200 OK");
                break;
            case "/tradings":
                this.respond("Got Trading", "200 OK");
                break;
            case "/users":
                this.respond("Got User", "200 OK");
                break;
            default:
                this.respond("Service not implemented", "501 Not Implemented");
        }
    }

    public void postHandler(){
        String path = incomingRequest.getRequestPath();
        switch(path){
            case "/users":
                this.postUser();
                break;
            case "/sessions":
                this.respond("Successfully logged in.", "200 OK");
                break;
            case "/packages":
                this.respond("Created Package.", "201 Created");
                break;
            case "/transactions/packages":
                this.respond("User received Package.", "200 OK");
                break;
            default:
                this.respond("Service has not been Implemented.", "501 Not Implemented");
        }
    }

    public void putHandler() {
        String path = incomingRequest.getRequestPath();
        if(path.startsWith("/users")) path = "/users";
        switch (path) {
            case "/deck":
                this.respond("Updated Deck", "200 OK");
                break;
            case "/users":
                this.respond("Updated User", "200 OK");
                break;
            default:
                this.respond("Service not implemented", "501 Not Implemented");
        }
    }

    public void deleteHandler() {
        String path = incomingRequest.getRequestPath();
        if(path.startsWith("/users")) path = "/users";
        switch (path) {
            case "/tradings":
                this.respond("Deleted Trading", "200 OK");
                break;
            case "/users":
                this.respond("Deleted Users", "200 OK");
                break;
            default:
                this.respond("Service not implemented", "501 Not Implemented");
        }
    }

    public void postUser() {
        String postString = incomingRequest.getPostContent();
        System.out.println("postString to be mapped: " + postString);
        String username, password;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(postString);
            Map<String, Object> map = objectMapper.convertValue(jsonNode, Map.class);
            username = (String) map.get("Username");
            password = (String) map.get("Password");
            this.respond("Successfully added User: " +username +"\nWith password: " + password, "200 OK");

        }catch(IOException e){
            this.respond("Error during User Creation", "500");
            System.err.println("Exception occurred in postUser: " + e);
        }
    }



    public void respond(String text, String code){
        out.print("HTTP/1.1 "+code+"\r\n");
        out.print("Content-Type: text/plain\r\n");
        out.print("Content-Length: " + text.length()+"\r\n");
        out.print("\r\n");
        out.print(text);
    }
}
