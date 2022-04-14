package proxyservice.model.cameradata;

public interface CameraDataConsumer {
    int getId();
    boolean isMessagesBufferSent();
    void notifyMessagesBufferSent();
    void setEventListener(CameraDataConsumerEventListener eventListener);
    void send(byte[] data);
}
