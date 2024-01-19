package main.server.HTTPServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port = 8080;
    public Server(){
    }

    public Server(int port) {
        this.port = port;
    }

    public void start()  {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server is running on port: " + port);
            while(true){
                try(Socket clientSocket = serverSocket.accept()){
                    System.out.println("Client connected: " + clientSocket);

                    //handleClient
                } catch (IOException e){
                    System.err.println("Error during client communication: " + e.getMessage());
                }
            }
        }catch(IOException e){
            System.err.println("Could not listen on port " +  port);
        }
    }

}
