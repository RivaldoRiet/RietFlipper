package RietFlipper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {

    private final String url, method;

    private Map<String,String> parameterMap = new HashMap<>();

    public HTTPRequest(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public HTTPRequest(String url) {
        this(url,"GET");
    }

    public HTTPRequest put(String k, String v) {
        parameterMap.put(k,v);
        return this;
    }

    public HTTPResponse read() {
        String baseUrl = url;
        if (method.equals("GET") || method.equals("get")) {
            int i = 0;
            for(Map.Entry<String,String> entry : parameterMap.entrySet()) {
                baseUrl = baseUrl.concat((i==0 ? "?" : "&") + entry.getKey() + "=" + entry.getValue());
                i++;
            }
        }
        try {
            System.out.println("URL: " + baseUrl);
            URL url = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
            if (method.equals("POST") || method.equals("post")) {
                String contentString = "";
                for (Map.Entry<String,String> entry : parameterMap.entrySet()) {
                    contentString = contentString.concat("&"+entry.getKey()+"="+entry.getValue());
                }
                connection.setDoOutput(true);
                PrintStream out = new PrintStream(connection.getOutputStream());
                out.print(contentString.substring(1));
            }
            return new HTTPResponse(connection).read();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}