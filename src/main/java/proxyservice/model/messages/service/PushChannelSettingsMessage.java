package proxyservice.model.messages.service;

import proxyservice.model.messages.Message;

public class PushChannelSettingsMessage extends Message implements ServiceMessage {
    private String clientId;
    private String clientSecret;

    public PushChannelSettingsMessage(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
