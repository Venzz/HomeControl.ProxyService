package proxyservice.model.messages;

import proxyservice.App;
import proxyservice.model.messages.service.PushChannelSettingsMessage;
import proxyservice.model.messages.service.PushChannelUriMessage;
import proxyservice.model.messages.service.PushNotificationContentMessage;
import proxyservice.model.messages.service.ServiceMessageId;
import proxyservice.model.messages.standard.StandardMessage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class Message {
    protected Message() {
    }

    public static Message tryCreate(byte[] data){
        if (data.length < 8) {
            return null;
        }

        ByteBuffer dataBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        int header = dataBuffer.getInt(0);
        if (header == 0xFEFFFFFF) {
            int consumerId = dataBuffer.getInt(4);
            return new StandardMessage(consumerId);
        } else if (header == 0xFDFFFFFF) {
            int dataLength = dataBuffer.getInt(8);
            if (dataLength < data.length - 12) {
                return null;
            }

            dataBuffer.position(16);
            ServiceMessageId messageId = ServiceMessageId.create(dataBuffer.get());
            if (messageId == ServiceMessageId.PushChannelUri) {
                String previousUri = getString(dataBuffer);
                String uri = getString(dataBuffer);
                return new PushChannelUriMessage(previousUri, uri);
            } else if (messageId == ServiceMessageId.PushChannelSettings) {
                String clientId = getString(dataBuffer);
                String clientSecret = getString(dataBuffer);
                return new PushChannelSettingsMessage(clientId, clientSecret);
            } else if (messageId == ServiceMessageId.PushNotification) {
                String content = getString(dataBuffer);
                return new PushNotificationContentMessage(content);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static String getString(ByteBuffer dataBuffer) {
        int length = dataBuffer.getInt();
        byte[] stringData = new byte[length];
        dataBuffer.get(stringData);
        return new String(stringData, Charset.forName("UTF-8"));
    }
}
