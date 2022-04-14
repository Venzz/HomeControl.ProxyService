package proxyservice.utilities.http;

import proxyservice.utilities.http.content.FormUrlEncodedContent;
import proxyservice.utilities.http.content.HttpContent;
import proxyservice.utilities.http.content.XmlContent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {
    private Map<String, String> headers = new HashMap<String, String>();

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public String post(String url, HttpContent content) {
        try {
            byte[] contentData = content.getContentString().getBytes("UTF-8");
            HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setRequestMethod("POST");
            for (String name : headers.keySet()) {
                connection.setRequestProperty(name, headers.get(name));
            }
            if (content instanceof FormUrlEncodedContent) {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            } else if (content instanceof XmlContent) {
                connection.setRequestProperty("Content-Type", "text/xml");
            }
            connection.setRequestProperty("Content-Length", Integer.toString(contentData.length));
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            try (DataOutputStream dataWriter = new DataOutputStream(connection.getOutputStream())) {
                dataWriter.write(contentData, 0, contentData.length);
            }

            StringBuffer responseString = new StringBuffer();
            try (BufferedReader dataReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                while (true) {
                    String line = dataReader.readLine();
                    if (line == null) {
                        break;
                    }
                    responseString.append(line);
                    responseString.append('\n');
                }
            }

            connection.disconnect();
            return responseString.toString();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
