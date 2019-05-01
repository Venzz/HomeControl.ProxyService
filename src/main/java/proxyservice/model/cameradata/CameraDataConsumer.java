package proxyservice.model.cameradata;

public interface CameraDataConsumer {
    int getId();
    void setEventListener(CameraDataConsumerEventListener eventListener);
    void send(byte[] data);
}
