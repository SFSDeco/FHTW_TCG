package main.server.HTTPServer;

import main.server.RequestHandler.RequestHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    private int port = 10001;
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
                    RequestHandler r = new RequestHandler(clientSocket);
                    r.handle();
                } catch (IOException e){
                    System.err.println("Error during client communication: " + e.getMessage());
                }
            }
        }catch(IOException e){
            System.err.println("Could not listen on port " +  port);
        }
    }


}
