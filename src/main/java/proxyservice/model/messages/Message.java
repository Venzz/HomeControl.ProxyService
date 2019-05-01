package proxyservice.model.messages;

import proxyservice.model.messages.standard.StandardMessage;

import java.nio.ByteBuffer;

public class Message {
    protected Message() {
    }

    public static Message tryCreate(byte[] data){
        if (data.length < 8) {
            return null;
        }

        ByteBuffer dataBuffer = ByteBuffer.wrap(data);
        int header = dataBuffer.getInt(0);
        if (header == 0xFFFFFFFE) {
            int consumerId = dataBuffer.getInt(4);
            return new StandardMessage(consumerId);
        } else {
            return null;
        }
    }
}
