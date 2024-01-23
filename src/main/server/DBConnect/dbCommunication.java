package main.server.DBConnect;

import main.logic.models.User;

import java.sql.*;

public class dbCommunication {
    private Connection c = null;
    private boolean connected = false;

    public dbCommunication(){}

    public void connect(){
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tcgdb", "postgres", "postgres");
            System.out.println("Successfully connected to DB");
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

    public boolean createToken(User userToAuth){
        boolean success = false;
        if(!connected){
            System.err.println("Not connected to Database!");
            return success;
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
            return success;
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
}
