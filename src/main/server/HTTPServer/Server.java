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
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Server is running on port: " + port);
            while(true){
                try{
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket);
                    //handleClient
                    new Thread(() -> handleClient(clientSocket)).start();

                } catch (IOException e){
                    System.err.println("Error during client communication: " + e.getMessage());
                }
            }
        }catch(IOException e){
            System.err.println("Could not listen on port " +  port);
        }
        finally {
            if(serverSocket != null){
                try{
                    serverSocket.close();
                }
                catch (IOException e){
                    System.err.println("Error during Server shutdown: " + e);
                }
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        // Handle the client in a separate thread
        RequestHandler requestHandler = new RequestHandler(clientSocket);
        requestHandler.handle();
    }
}
