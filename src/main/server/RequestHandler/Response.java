package main.server.RequestHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import main.logic.models.*;
import main.server.DBConnect.dbCommunication;
import main.server.models.BattleReady;

public class Response {

    public static final BattleReady waitingForBattle = new BattleReady();
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
                this.getCards();
                break;
            case "/deck":
                this.getDeck();
                break;
            case "/stats":
                this.getStats();
                break;
            case "/scoreboard":
                this.getScoreboard();
                break;
            case "/users":
                this.getUser();
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
                this.acquirePackage();
                break;
            case "/battles":
                this.fightBattle();
                break;
            case "/logout":
                this.logoutUser();
                break;
            default:
                this.respond("Service has not been Implemented.", "501 Not Implemented");
        }
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public void putHandler() {
        String path = incomingRequest.getRequestPath();
        if(path.startsWith("/users")) path = "/users";
        switch (path) {
            case "/deck":
                this.updateDeck();
                break;
            default:
                this.respond("Service not implemented", "501 Not Implemented");
        }
    }

    public void logoutUser(){
        if(!validateAuth()) {
            this.respond("Authorization missing.", "402 Authorization required");
            return;
        }
        dbCommunication connection = new dbCommunication();
        String authorization = incomingRequest.getHeaderMap().get("Authorization").replace("Bearer ", "");
        connection.connect();
        if(!connection.deleteAuthToken(authorization)){
            this.respond("Error during Logout attempt.", "500 Internal Server Error");
            return;
        }
        connection.disconnect();
        this.respond("Successfully logged out! We hope you'll be back soon!", "200 OK");
    }

    public void fightBattle(){
        if(!validateAuth()) {
            this.respond("Authorization missing.", "402 Authorization required");
            return;
        }

        //Theoretically One User Object would be enough, two are used to improve clarity
        User playerA, playerB;
        dbCommunication connection = new dbCommunication();
        String authorization = incomingRequest.getHeaderMap().get("Authorization").replace("Bearer ", "");
        if(!waitingForBattle.isWaiting()){
            synchronized (waitingForBattle){
                try{
                    waitingForBattle.setWaiting(true);
                    connection.connect();
                    playerA = connection.getUserFromAuth(authorization);

                    waitingForBattle.setWaitingUser(playerA);

                    while(waitingForBattle.getWaitingUser() != null){
                        waitingForBattle.wait();
                    }
                    if(!connection.updateScore(waitingForBattle.getBattle().getPlayerA().getScore(), waitingForBattle.getBattle().getPlayerA().getId())){
                        this.respond("Tried to use a teakettle for score updates.", "418 I'm a teapot");
                        connection.disconnect();
                        return;
                    }

                    connection.disconnect();
                    this.respond("Successfully battled. Result: " + waitingForBattle.getBattle().outcomeString(), "200 OK");
                }catch (Exception e){
                    System.err.println("Error during Battle finder: " + e);
                }
            }
        }
        else{
            synchronized (waitingForBattle){
                waitingForBattle.setWaiting(false);


                connection.connect();
                playerB = connection.getUserFromAuth(authorization);

                waitingForBattle.setBattle(new Battle(waitingForBattle.getWaitingUser(), playerB));
                waitingForBattle.getBattle().start();
                if(!connection.updateScore(waitingForBattle.getBattle().getPlayerB().getScore(), waitingForBattle.getBattle().getPlayerB().getId())){
                    this.respond("Tried to use a teakettle for score updates.", "418 I'm a teapot");
                    connection.disconnect();
                    return;
                }
                waitingForBattle.setWaitingUser(null);
                waitingForBattle.notify();
                connection.disconnect();
                this.respond("Entered Battle. Result: " + waitingForBattle.getBattle().outcomeString(), "200 OK");
            }
        }

    }

    public void getStats(){
        if(!validateAuth()) {
            this.respond("Authorization missing.", "402 Authorization required");
            return;
        }
        String authorization = incomingRequest.getHeaderMap().get("Authorization").replace("Bearer ", "");

        dbCommunication connection = new dbCommunication();
        connection.connect();

        User user = connection.getUserFromAuth(authorization);
        int score = user.getScore();

        connection.disconnect();
        this.respond("Your current score is: " + score, "200 OK");
    }

    public void getScoreboard(){
        if(!validateAuth()) {
            this.respond("Authorization missing.", "402 Authorization required");
            return;
        }

        dbCommunication connection = new dbCommunication();
        connection.connect();
        String scoreBoard = connection.getScoreboard();

        connection.disconnect();
        this.respond("The current Rankings are: " + scoreBoard, "200 OK");
    }

    public void getUser(){
        if(!validateAuth()) {
            this.respond("Authorization missing.", "402 Authorization required");
            return;
        }
        String authorization = incomingRequest.getHeaderMap().get("Authorization").replace("Bearer ", "");
        String response = "";

        dbCommunication connection = new dbCommunication();
        connection.connect();

        User user = connection.getUserFromAuth(authorization);

        response += "Username: " + user.getUserName()
                + "\n Coins: " + user.getCurrency()
                + "\n Score: " + user.getScore();

        connection.disconnect();
        this.respond(response, "200 OK");
    }

    public void getDeck(){
        if(!validateAuth()) {
            this.respond("Authorization missing.", "402 Authorization required");
            return;
        }
        String authorization = incomingRequest.getHeaderMap().get("Authorization").replace("Bearer ", "");

        dbCommunication connection = new dbCommunication();
        connection.connect();

        User user = connection.getUserFromAuth(authorization);
        Deck userDeck = connection.getDeck(user);

        connection.disconnect();

        if(userDeck.isEmpty()){
            this.respond("No Deck set for queried User", "200 OK");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonArrayString = objectMapper.writeValueAsString(userDeck);

            this.respond(jsonArrayString, "200 OK");
        }catch(Exception e){
            this.respond("Error response creation", "500");
            System.err.println("Exception occurred in getCards: " + e);
        }
    }

    public void getCards(){
        if(!validateAuth()) {
            this.respond("Authorization missing.", "402 Authorization required");
            return;
        }
        String authorization = incomingRequest.getHeaderMap().get("Authorization").replace("Bearer ", "");

        dbCommunication connection = new dbCommunication();
        connection.connect();

        User user = connection.getUserFromAuth(authorization);

        System.out.println(user);

        Vector<Card> userCards = connection.getCards(user);

        System.out.println(userCards);

        connection.disconnect();

        if(userCards.isEmpty()){
            this.respond("No Cards for queried User", "200 OK");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonArrayString = objectMapper.writeValueAsString(userCards);

            this.respond(jsonArrayString, "200 OK");
        }catch(Exception e){
            this.respond("Error response creation", "500");
            System.err.println("Exception occurred in getCards: " + e);
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
            return;
        }
        String postString = incomingRequest.getPostContent();
        String cardName, cardID;
        double dmg;
        List<Card> cardsToAdd = new ArrayList<>();

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

    public void acquirePackage(){
        if(!validateAuth()) {
            this.respond("Authorization missing.", "402 Authorization required");
            return;
        }
        String authorization = incomingRequest.getHeaderMap().get("Authorization").replace("Bearer ", "");
        dbCommunication connection = new dbCommunication();
        connection.connect();
        User user = connection.getUserFromAuth(authorization);

        if(user == null){
            this.respond("Server Error.", "500 Internal Server Error");
            connection.disconnect();
            return;
        }

        if(user.getCurrency() < 1){
            this.respond("User does not possess required currency.", "200 OK");
            connection.disconnect();
            return;
        }

        cardPackage acquiredPackage = connection.getPackage();

        if(acquiredPackage == null){
            this.respond("No valid Packages.", "500 Internal Server Error");
            connection.disconnect();
            return;
        }

        user.addPackage(acquiredPackage);
        user.setCurrency(user.getCurrency()-5);

        if(!connection.updateUser(user)){
            this.respond("Server Error.", "500 Internal Server Error");
            connection.disconnect();
            return;
        }

        if(!connection.deletePackage(acquiredPackage.getPackageID())){
            this.respond("Unexpected Failure!", "418 I am a teapot");
            connection.disconnect();
            return;
        }

        connection.disconnect();
        this.respond("Acquired package.", "200 OK");
    }

    public void updateDeck(){
        String putString = incomingRequest.getPostContent();

        if(!validateAuth()) {
            this.respond("Authorization missing.", "402 Authorization required");
            return;
        }

        try {
            String authorization = incomingRequest.getHeaderMap().get("Authorization").replace("Bearer ", "");
            //get List of Card IDs to add
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> stringList = objectMapper.readValue(putString, new TypeReference<List<String>>() {});

            if(stringList.size() < 4){
                this.respond("Not enough Cards in the requested Deck.", "400 Bad Request");
                return;
            }

            dbCommunication connection = new dbCommunication();
            connection.connect();

            User user = connection.getUserFromAuth(authorization);
            if(user == null){
                this.respond("Access denied/Invalid Authorization.", "402 Authorization required.");
            }

            assert user != null;

            for(String cardID : stringList){
                boolean found = user.getCardStack().stream().map(Card::getId).anyMatch(str -> str.equals(cardID));
                if(!found){
                    this.respond("Card not found in Deck.", "400 Bad Request");
                    return;
                }
            }

            if(!connection.replaceDeck(user.getId(), stringList)){
                this.respond("Error occured during update.", "500 Internal Server Error");
                return;
            }

            this.respond("Updated Deck.", "200");
            connection.disconnect();

        }catch(IOException e){
            this.respond("Error during User Creation", "500");
            System.err.println("Exception occurred in postUser: " + e);
        }
    }

    public boolean validateAuth(){
        boolean validate;
        if(!incomingRequest.getHeaderMap().containsKey("Authorization")) return false;
        String authorization = incomingRequest.getHeaderMap().get("Authorization").replace("Bearer ", "");

        System.out.println(authorization);

        dbCommunication connection = new dbCommunication();
        connection.connect();
        validate = connection.validateAuthorization(authorization);
        connection.disconnect();
        return validate;
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
