package proxyservice.model.cameradata;

public interface CameraDataProvider {
    void setEventListener(CameraDataProviderEventListener eventListener);
    void send(byte[] data);
}
