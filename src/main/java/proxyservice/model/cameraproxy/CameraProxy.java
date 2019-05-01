package proxyservice.model.cameraproxy;

import proxyservice.model.cameradata.CameraDataConsumer;
import proxyservice.model.cameradata.CameraDataConsumerEventListener;
import proxyservice.model.cameradata.CameraDataProvider;
import proxyservice.model.cameradata.CameraDataProviderEventListener;
import proxyservice.model.messages.Message;
import proxyservice.model.messages.standard.StandardMessage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class CameraProxy implements CameraDataProviderEventListener, CameraDataConsumerEventListener {
    private CameraDataProvider provider;
    private Map<Integer, CameraDataConsumer> consumers = new HashMap<Integer, CameraDataConsumer>();

    public synchronized void setProvider(CameraDataProvider provider) {
        this.provider = provider;
        this.provider.setEventListener(this);
    }

    public synchronized void addConsumer(CameraDataConsumer consumer) {
        consumers.put(consumer.getId(), consumer);
        consumer.setEventListener(this);
    }

    public synchronized void removeConsumer(CameraDataConsumer consumer) {
        consumers.remove(consumer.getId());
    }

    public synchronized void onConsumerDataReceived(int consumerId, byte[] data) {
        if (provider == null) {
            return;
        }

        ByteBuffer dataBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        dataBuffer.putInt(4, consumerId);

        provider.send(data);
    }

    public synchronized void onProviderDataReceived(byte[] data) {
        Message message = Message.tryCreate(data);
        if (message instanceof StandardMessage) {
            StandardMessage standardMessage = (StandardMessage)message;
            int consumerId = standardMessage.getConsumerId();
            if (consumerId == 0) {
                for (CameraDataConsumer consumer : consumers.values()) {
                    consumer.send(data);
                }
            } else if (consumers.containsKey(consumerId)) {
                consumers.get(consumerId).send(data);
            }
        } else {
            for (CameraDataConsumer consumer : consumers.values()) {
                consumer.send(data);
            }
        }
    }
}
