package RietFlipper;

import io.protocol.json.Json;
import io.protocol.json.JsonValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;

public class HTTPResponse  {

    private HttpURLConnection connection;
    private int responseCode;
    private List<String> lineList;

    public HTTPResponse (HttpURLConnection connection) throws IOException {
        this.connection = connection;
        this.responseCode = connection.getResponseCode();
        this.lineList = new LinkedList<>();
    }

    public HTTPResponse read() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            lineList.add(line);
        }
        return this;
    }

    public List<String> getResponse() {
        return lineList;
    }

    public JsonValue getJsonResponse() {
        String raw = getRawResponse();
        return Json.parse(raw);
    }

    public int getIndexContaining(String string) {
        for(int i = 0; i < lineList.size();i++) {
            String line = lineList.get(i);
            if (line.contains(string))
                return i;
        }
        return -1;
    }

    public String getRawResponse() {
        return String.join("",lineList);
    }

    public int getResponseCode() {
        return responseCode;
    }
}
