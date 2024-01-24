package main.server.RequestHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import main.logic.models.Card;
import main.logic.models.User;
import main.server.DBConnect.dbCommunication;

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
                this.loginUser();
                break;
            case "/packages":
                this.createPackage();
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
        String username, password;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(postString);
            Map<String, Object> map = objectMapper.convertValue(jsonNode, Map.class);
            username = (String) map.get("Username");
            password = (String) map.get("Password");
            User createUser = new User(username, password);

            dbCommunication connection = new dbCommunication();
            connection.connect();
            if(connection.insertUser(createUser))
                this.respond("Successfully added User: " +username +"\nWith password: " + password, "201 Created");

            else{
                this.respond("Error during Database communication", "500 Internal Server Error");
            }
            connection.disconnect();

        }catch(IOException e){
            this.respond("Error during User Creation", "500");
            System.err.println("Exception occurred in postUser: " + e);
        }
    }

    public void loginUser(){
        String postString = incomingRequest.getPostContent();
        String username, password;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(postString);
            Map<String, Object> map = objectMapper.convertValue(jsonNode, Map.class);
            username = (String) map.get("Username");
            password = (String) map.get("Password");
            dbCommunication connection = new dbCommunication();

            connection.connect();
            User loginAttempt = connection.getUser(username);
            if(loginAttempt != null &&
                    loginAttempt.getUserName().equals(username) && loginAttempt.getPassword().equals(password)){

                if(connection.createToken(loginAttempt)){
                    this.respond("Successfully logged in and set authToken", "200 OK");
                }
                else{
                    this.respond("Error during Login", "500 Internal Server Error");
                }
            }
            else{
                this.respond("Login failed due to incorrect credentials.", "400 Bad Request");
            }
            connection.disconnect();

        }catch(IOException e){
            this.respond("Error during User Creation", "500");
            System.err.println("Exception occurred in postUser: " + e);
        }
    }

    public void createPackage(){
        if(!checkForAdmin()){
            this.respond("Access denied.", "403 Forbidden");
        }
        String postString = incomingRequest.getPostContent();
        String cardName, cardID, cardElement, cardType, cardSpecialty;
        double dmg;
        List<Card> cardsToAdd = new ArrayList<Card>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<Map<String, Object>> jsonStringList = objectMapper.readValue(postString, new TypeReference<List<Map<String, Object>>>() {});
            for(Map<String, Object> json : jsonStringList){
                cardName = (String) json.get("Name");
                cardID = (String) json.get("Id");
                dmg = (Double) json.get("Damage");

                Card c = new Card(cardID, cardName, dmg);
                c.setCardType(c.getTypeFromName());
                c.setElement(c.getElementFromName());
                c.setSpecialty(c.getSpecialFromName());

                cardsToAdd.add(c);
            }

            dbCommunication connection = new dbCommunication();
            connection.connect();
            if(connection.createPackage(cardsToAdd)){
                this.respond("Created Package.", "201 Created");
            }
            else{
                this.respond("Error during Package creation.", "500 Internal Server Error");
            }
            connection.disconnect();


        }catch(IOException e){
            this.respond("Error during User Creation", "500");
            System.err.println("Exception occurred in postUser: " + e);
        }

    }

    public boolean checkForAdmin(){
        boolean isAdmin = false;
        String authorization = incomingRequest.getHeaderMap().get("Authorization");

        if(authorization.contains("admin-mtcgToken")) isAdmin = true;

        return isAdmin;
    }

    public void respond(String text, String code){
        out.print("HTTP/1.1 "+code+"\r\n");
        out.print("Content-Type: text/plain\r\n");
        out.print("Content-Length: " + text.length()+"\r\n");
        out.print("\r\n");
        out.print(text);
    }
}
