package proxyservice.model.messages.standard;

import proxyservice.model.messages.Message;

public class StandardMessage extends Message {
    private int consumerId;

    public StandardMessage(int consumerId) {
        this.consumerId = consumerId;
    }

    public int getConsumerId() {
        return consumerId;
    }
}
