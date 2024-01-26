package main.server.DBConnect;

import main.logic.models.*;

import java.sql.*;
import java.util.List;
import java.util.Vector;

public class dbCommunication {
    private Connection c = null;
    private boolean connected = false;

    public dbCommunication(){}

    public void connect(){
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tcgdb", "postgres", "postgres");

            connected = !connected;
        } catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void disconnect(){
        try{
            c.close();
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage()  );
        }
    }

    public boolean createPackage(List<Card> cardsToAdd){
        boolean success = false;
        if(!connected){
            System.err.println("Not connected to Database!");
            return false;
        }
        try{
            int packageID;
            c.setAutoCommit(false);
            PreparedStatement stmt = c.prepareStatement("INSERT INTO package DEFAULT VALUES", Statement.RETURN_GENERATED_KEYS);

            int count = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(count > 0){
                if(rs.next()) {
                    packageID = rs.getInt(1);
                    System.out.println("ID Generated in DB is: " + packageID);
                    for (Card c : cardsToAdd) {
                        if (!this.createCard(packageID, c)) {
                            System.out.println("Error during Card creation");
                        }
                    }
                    success = true;
                }
            }

            stmt.close();
            c.commit();

        } catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }

        return success;
    }

    public boolean createCard(int cardPackage, Card toAdd){
        boolean success = false;
        if(!connected){
            System.err.println("Not connected to Database!");
            return false;
        }
        try{
            c.setAutoCommit(false);
            PreparedStatement stmt = c.prepareStatement("INSERT INTO cards (cardid, name, dmg, cardtype, element) VALUES (?, ?, ?, ?, ?)");

            stmt.setString(1, toAdd.getId());
            stmt.setString(2, toAdd.getName());
            stmt.setDouble(3, toAdd.getDamage());
            stmt.setString(4, toAdd.getCardType());
            stmt.setString(5, toAdd.getElement());

            int count = stmt.executeUpdate();
            if(count > 0){
                stmt = c.prepareStatement("INSERT INTO packagecards (packageid, cardid) VALUES (?, ?)");
                stmt.setInt(1, cardPackage);
                stmt.setString(2, toAdd.getId());
                count = stmt.executeUpdate();

                if(count > 0) success = true;


            }

            stmt.close();
            c.commit();

        } catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }


        return success;
    }

    public User getUser(String name){

        if(!connected){
            System.err.println("Not connected to Database!");
            return null;
        }

        try{
            String username = "", password = "";
            int id=-1, coins = -1;
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM users WHERE username = ?");

            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();

            if(rs == null){
                return null;
            }

            while(rs.next()){
                username = rs.getString("username");
                password = rs.getString("password");
                coins = rs.getInt("coins");
                id = rs.getInt("userid");
            }

            rs.close();
            stmt.close();

            if(!username.isEmpty() && !password.isEmpty() && coins >= 0){
                return new User(id, username, password, coins, username+"-mtcgToken");
            }

        }catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }

        return null;
    }

    public User getUserFromAuth(String authorization){
        if(!connected){
            System.err.println("Not connected to Database!");
            return null;
        }

        try{
            String username = "", password = "";
            int id=-1, coins = -1;
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM userauth INNER JOIN users ON userauth.userid = users.userid WHERE token = ?");

            stmt.setString(1, authorization);

            ResultSet rs = stmt.executeQuery();

            if(rs == null){
                return null;
            }

            while(rs.next()){
                username = rs.getString("username");
                password = rs.getString("password");
                coins = rs.getInt("coins");
                id = rs.getInt("userid");
            }

            rs.close();
            stmt.close();

            User user = new User(id, username, password, coins, username+"-mtcgToken");

            user.setCardStack(this.getCards(user));
            user.setCardDeck(this.getDeck(user));

            if(!username.isEmpty() && !password.isEmpty() && coins >= 0){
                return user;
            }

        }catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }

        return null;
    }

    public cardPackage getPackage(){
        if(!connected){
            System.err.println("Not connected to Database!");
            return null;
        }

        try{
            cardPackage pack = new cardPackage();
            int packageID = -1;
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM package LIMIT 1");
            ResultSet rs = stmt.executeQuery();

            if(rs == null){
                return null;
            }

            while(rs.next()){
                packageID = rs.getInt("packageid");
            }

            if(packageID >= 0){
                String cardname, cardid, cardtype, element;
                double dmg;
                stmt = c.prepareStatement("SELECT * FROM packagecards " +
                        "INNER JOIN cards ON packagecards.cardid = cards.cardid WHERE packageid = ?");

                stmt.setInt(1, packageID);
                ResultSet cardSet = stmt.executeQuery();
                while(cardSet.next()){
                    cardname = cardSet.getString("name");
                    cardid = cardSet.getString("cardid");
                    cardtype = cardSet.getString("cardtype");
                    element = cardSet.getString("element");
                    dmg = cardSet.getDouble("dmg");
                    Card c = new Card(cardname, cardid, dmg , cardtype, element);

                    pack.addCard(c);
                }

                pack.setPackageID(packageID);

                cardSet.close();
                rs.close();
                stmt.close();
                return pack;
            }

            rs.close();
            stmt.close();

        }catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }

        return null;
    }

    public Vector<Card> getCards(User user){
        Vector<Card> cardList = new Vector<>();
        if(!connected){
            System.err.println("Not connected to Database!");
            return cardList;
        }
        try{
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM usercards INNER JOIN cards ON usercards.cardid = cards.cardid WHERE userid = ?");

            stmt.setInt(1, user.getId());

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                String cardid, name, type, element;
                double dmg;
                cardid = rs.getString("cardid");
                name = rs.getString("name");
                dmg = rs.getDouble("dmg");
                type = rs.getString("cardtype");
                element = rs.getString("element");

                cardList.add(new Card(name, cardid, dmg, type, element));
            }

            rs.close();
            stmt.close();

        }catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }

        return cardList;
    }

    public Deck getDeck(User user){
        Deck userDeck = new Deck(new Vector<>());
        if(!connected){
            System.err.println("Not connected to Database!");
            return userDeck;
        }
        try{
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM deckcards" +
                    " INNER JOIN cards ON deckcards.cardid = cards.cardid" +
                    " INNER JOIN decks ON deckcards.deckid = decks.deckid WHERE userid = ?");

            stmt.setInt(1, user.getId());

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                String cardid, name, type, element;
                double dmg;
                cardid = rs.getString("cardid");
                name = rs.getString("name");
                dmg = rs.getDouble("dmg");
                type = rs.getString("cardtype");
                element = rs.getString("element");

                userDeck.addCard(new Card(name, cardid, dmg, type, element));
            }

            rs.close();
            stmt.close();

        }catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }

        return userDeck;
    }

    public boolean deletePackage(int ID){
        boolean success = false;
        if(!connected){
            System.err.println("Not connected to Database!");
            return false;
        }
        try{
            c.setAutoCommit(false);
            PreparedStatement stmt = c.prepareStatement("DELETE FROM package WHERE packageid = ?");

            stmt.setInt(1, ID);

            int count = stmt.executeUpdate();
            if(count > 0){
                success = true;
            }

            stmt.close();
            c.commit();

        } catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }


        return success;
    }

    public boolean updateUser(User toUpdate){
        boolean success = false;
        if(!connected){
            System.err.println("Not connected to Database!");
            return false;
        }
        try{
            c.setAutoCommit(false);
            PreparedStatement stmt = c.prepareStatement("UPDATE users " +
                    "SET coins = ? " +
                    "WHERE userid = ?");

            stmt.setInt(2, toUpdate.getId());
            stmt.setInt(1, toUpdate.getCurrency());

            int count = stmt.executeUpdate();
            if(count > 0){
                //Delete Old Deck
                stmt = c.prepareStatement("DELETE FROM decks WHERE userid = ?");
                stmt.setInt(1, toUpdate.getId());
                stmt.executeUpdate();

                stmt = c.prepareStatement("INSERT INTO decks (userid) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, toUpdate.getId());
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if(rs.next()) {
                    int deckid = rs.getInt(1);
                    for (Card deckcard : toUpdate.getCardDeck().getDeck()) {
                        String cardID = deckcard.getId();
                        stmt = c.prepareStatement("INSERT INTO deckcards (deckid, cardid) VALUES (?, ?)");
                        stmt.setInt(1, deckid);
                        stmt.setString(2, cardID);
                        stmt.executeUpdate();
                    }
                }


                Vector<Card> currentDBStack = this.getCards(toUpdate);
                Vector<Card> cardsToAdd = new Vector<>();
                for(Card card : toUpdate.getCardStack()){
                    for(Card dbc: currentDBStack){
                        if(card.getId().equals(dbc.getId())) break;
                    }
                    cardsToAdd.add(card);
                }

                if(!cardsToAdd.isEmpty()){
                    stmt = c.prepareStatement("INSERT INTO usercards (cardid, userid) VALUES (?, ?)");
                    for(Card addCard : cardsToAdd){
                        stmt.setString(1, addCard.getId());
                        stmt.setInt(2, toUpdate.getId());
                        stmt.executeUpdate();
                    }
                }
                success = true;
            }

            stmt.close();
            c.commit();

        } catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }

        return success;
    }

    public boolean replaceDeck(int userID, List<String> cardIds){
        boolean success = false;
        if(!connected){
            System.err.println("Not connected to Database!");
            return false;
        }
        try{
            c.setAutoCommit(false);
            //Delete Old Deck
            PreparedStatement stmt = c.prepareStatement("DELETE FROM decks WHERE userid = ?");
            stmt.setInt(1, userID);
            stmt.executeUpdate();

            stmt = c.prepareStatement("INSERT INTO decks (userid) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, userID);
            int count = stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if(count > 0) {
                if (rs.next()) {
                    int deckID = rs.getInt(1);
                    for (String cardID : cardIds) {
                        stmt = c.prepareStatement("INSERT INTO deckcards (deckid, cardid) VALUES (?, ?)");
                        stmt.setInt(1, deckID);
                        stmt.setString(2, cardID);
                        stmt.executeUpdate();
                    }
                }
                success = true;
            }

            stmt.close();
            c.commit();

        } catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }

        return success;
    }

    public boolean createToken(User userToAuth){
        boolean success = false;
        if(!connected){
            System.err.println("Not connected to Database!");
            return false;
        }
        try{
            int id = userToAuth.getId();
            String authToken = userToAuth.getAuthToken();
            c.setAutoCommit(false);
            PreparedStatement stmt = c.prepareStatement("INSERT INTO userauth (token, userid) VALUES (?, ?)");

            stmt.setString(1, authToken);
            stmt.setInt(2, id);

            int count = stmt.executeUpdate();
            if(count > 0){
                success = true;
            }

            stmt.close();
            c.commit();

        } catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return success;
    }

    public boolean insertUser(User toInsert){
        boolean success = false;
        if(!connected){
            System.err.println("Not connected to Database!");
            return false;
        }
        try{
            String username = toInsert.getUserName();
            String password = toInsert.getPassword();
            c.setAutoCommit(false);
            PreparedStatement stmt = c.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");

            stmt.setString(1, username);
            stmt.setString(2, password);

            int count = stmt.executeUpdate();
            if(count > 0){
                success = true;
            }

            stmt.close();
            c.commit();

        } catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return success;
    }

    public boolean validateAuthorization(String authorization){
        boolean auth = false;
        if(!connected){
            System.err.println("Not connected to Database!");
            return false;
        }
        try{
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM userauth WHERE token = ?");

            stmt.setString(1, authorization);

            ResultSet rs = stmt.executeQuery();

            auth = rs.isBeforeFirst();

            stmt.close();

        }catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return auth;
    }
}
