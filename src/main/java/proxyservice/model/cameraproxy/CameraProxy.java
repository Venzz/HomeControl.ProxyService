package proxyservice.model.cameraproxy;

import proxyservice.model.cameradata.CameraDataConsumer;
import proxyservice.model.cameradata.CameraDataConsumerEventListener;
import proxyservice.model.cameradata.CameraDataProvider;
import proxyservice.model.cameradata.CameraDataProviderEventListener;

import java.util.HashSet;
import java.util.Set;

public class CameraProxy implements CameraDataProviderEventListener, CameraDataConsumerEventListener {
    private CameraDataProvider provider;
    private Set<CameraDataConsumer> consumers = new HashSet<CameraDataConsumer>();

    public synchronized void setProvider(CameraDataProvider provider) {
        this.provider = provider;
        this.provider.setEventListener(this);
    }

    public synchronized void addConsumer(CameraDataConsumer consumer) {
        consumers.add(consumer);
        consumer.setEventListener(this);
    }

    public synchronized void removeConsumer(CameraDataConsumer consumer) {
        consumers.remove(consumer);
    }

    public synchronized void onConsumerDataReceived(int id, byte[] data) {
        if (provider == null) {
            return;
        }
        provider.send(data);
    }

    public synchronized void onProviderDataReceived(byte[] data) {
        for (CameraDataConsumer consumer : consumers) {
            consumer.send(data);
        }
    }
}
