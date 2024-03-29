package main.server.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class RequestHandler {
    private Request HTTPRequest;
    private Socket clientSocket;

    public RequestHandler() {
    }

    public void handle(Socket incomingSocket){
        this.clientSocket = incomingSocket;
        try {
            ArrayList<String> requestArray = new ArrayList<>();
            InputStream inClient = null;
            inClient = clientSocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inClient));
            String requestLine = br.readLine();

            System.out.println("Getting Request.");
            while (!requestLine.isBlank()) {
                requestArray.add(requestLine);
                requestLine = br.readLine();
            }

            System.out.println("Building POST Content");
            StringBuilder postContent = new StringBuilder();
            while(br.ready()){
                postContent.append((char) br.read());
            }
            this.generateRequest(requestArray, postContent.toString());
            Response r = new Response(HTTPRequest, clientSocket);
            r.handleResponse();
            System.out.println("Handle response.");
            clientSocket.close();
            System.out.println("Connection closed.");

        }catch (IOException e){
            System.err.println("IOException during Request Handling." + e);
        }
    }

    public void generateRequest(ArrayList<String> requestString, String postContent){
        HTTPRequest = new Request();
        String[] Line;
        Line = requestString.get(0).split(" ", 3);
        HTTPRequest.setHTTPMethod(Line[0]);
        HTTPRequest.setRequestPath(Line[1]);
        HTTPRequest.setRequestVersion(Line[2]);

        for(int i = 1; i < requestString.size(); ++i){
            Line = requestString.get(i).split(": ", 2);
            HTTPRequest.addHeader(Line[0], Line[1]);
        }
        HTTPRequest.setPostContent(postContent);

    }


}
