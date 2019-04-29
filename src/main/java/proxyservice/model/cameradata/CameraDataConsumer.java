package proxyservice.model.cameradata;

public interface CameraDataConsumer {
    void setEventListener(CameraDataConsumerEventListener eventListener);
    void send(byte[] data);
}
