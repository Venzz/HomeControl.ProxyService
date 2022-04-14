package proxyservice.utilities.http.content;

import java.util.HashMap;
import java.util.Map;

public class FormUrlEncodedContent implements HttpContent {
    private Map<String, String> values = new HashMap<String, String>();

    public FormUrlEncodedContent() {
    }

    public void add(String name, String value) {
        values.put(name, value);
    }

    public String getContentString() {
        StringBuilder contentBuilder = new StringBuilder();
        for (String name : values.keySet()) {
            contentBuilder.append(name);
            contentBuilder.append("=");
            contentBuilder.append(values.get(name));
            contentBuilder.append("&");
        }
        contentBuilder.deleteCharAt(contentBuilder.length() - 1);
        return contentBuilder.toString();
    }
}
