package proxyservice.model.messages.service;

import proxyservice.model.messages.Message;

public class PushChannelUriMessage extends Message implements ServiceMessage {
    private String previousUri;
    private String uri;

    public PushChannelUriMessage(String previousUri, String uri) {
        this.previousUri = previousUri;
        this.uri = uri;
    }

    public String getPreviousUri() {
        return previousUri;
    }

    public String getUri() {
        return uri;
    }
}
