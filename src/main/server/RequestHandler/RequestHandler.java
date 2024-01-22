package main.server.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class RequestHandler {
    private Request HTTPRequest;
    private final Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void handle(){
        try {
            ArrayList<String> requestArray = new ArrayList<>();
            InputStream inClient = null;
            inClient = clientSocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inClient));
            String requestLine;

            System.out.println("Getting Request.");
            while ((requestLine = br.readLine()) != null && !requestLine.isEmpty()) {
                requestArray.add(requestLine);
            }
            System.out.println("Generating Request.");
            this.generateRequest(requestArray);
            System.out.println(this.HTTPRequest);
            Response r = new Response(HTTPRequest, clientSocket);
            r.handleResponse();
            System.out.println("Handle response.");
            clientSocket.close();
            System.out.println("Connection closed.");

        }catch (IOException e){
            System.err.println("IOException during Request Handling.");
        }
    }

    public void generateRequest(ArrayList<String> requestString){
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

    }


}
