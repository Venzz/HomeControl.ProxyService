package proxyservice.utilities.http.content;

public class XmlContent implements HttpContent {
    private String value;

    public XmlContent(String value) {
        this.value = value;
    }

    public String getContentString() {
        return value;
    }
}
