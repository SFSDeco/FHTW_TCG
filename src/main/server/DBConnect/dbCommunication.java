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

    public boolean insertUser(User toInsert){
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
                stmt.close();
                c.commit();
                return true;
            }
            else{
                return false;
            }
        } catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return false;
    }
}
