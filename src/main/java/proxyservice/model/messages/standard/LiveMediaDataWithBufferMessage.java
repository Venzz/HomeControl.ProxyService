package proxyservice.model.messages.standard;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;

public class LiveMediaDataWithBufferMessage {
    public static byte[] getData(int consumerId, byte[] message, Collection<byte[]> messagesBuffer) {
        byte type = 3;
        int id = 0;

        int totalSize = 4 + 4 + 4 + 4 + 1;
        totalSize += 4 + message.length;
        totalSize += 4;
        for (byte[] messageBuffer : messagesBuffer) {
            totalSize += 4 + messageBuffer.length;
        }

        ByteBuffer dataBuffer = ByteBuffer.allocate(totalSize).order(ByteOrder.LITTLE_ENDIAN);
        dataBuffer.put(new byte[] { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFE });
        dataBuffer.putInt(consumerId);
        dataBuffer.putInt(totalSize - 12);
        dataBuffer.putInt(id);
        dataBuffer.put(type);

        dataBuffer.putInt(message.length);
        dataBuffer.put(message);
        dataBuffer.putInt(messagesBuffer.size());
        for (byte[] messageBuffer : messagesBuffer) {
            dataBuffer.putInt(messageBuffer.length);
            dataBuffer.put(messageBuffer);
        }

        byte[] data = new byte[totalSize];
        dataBuffer.position(0);
        dataBuffer.get(data);
        return data;
    }
}
