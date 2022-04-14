package proxyservice.model.cameradata;

public interface CameraDataConsumerEventListener {
    void onConsumerDataReceived(int id, byte[] data);
}
