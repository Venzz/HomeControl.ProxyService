package proxyservice.model.messages.service;

import proxyservice.model.messages.Message;

public class PushNotificationContentMessage extends Message implements ServiceMessage {
    private String content;

    public PushNotificationContentMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
