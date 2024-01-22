package main.server.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String HTTPMethod;
    private String requestPath;
    private String requestVersion;

    private Map<String ,String> headers;

    public Request(){
        this.headers = new HashMap<String, String>();
    }

    public String getHTTPMethod() {
        return HTTPMethod;
    }

    public void setHTTPMethod(String HTTPMethod) {
        this.HTTPMethod = HTTPMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getRequestVersion() {
        return requestVersion;
    }

    public void setRequestVersion(String requestVersion) {
        this.requestVersion = requestVersion;
    }

    public String getHeader(String header){
        String foundHeader = "notFound";

        if(headers.containsKey(header)){
            foundHeader = headers.get(header);
        }

        return foundHeader;
    }

    public void addHeader(String Header, String Value){
        headers.put(Header, Value);
    }

    public Map<String, String> getHeaderMap(){
        return headers;
    }

    @Override
    public String toString() {
        return "Request{" +
                "HTTPMethod='" + HTTPMethod + '\'' +
                ", requestPath='" + requestPath + '\'' +
                ", requestVersion='" + requestVersion + '\'' +
                ", headers=" + headers +
                '}';
    }
}
