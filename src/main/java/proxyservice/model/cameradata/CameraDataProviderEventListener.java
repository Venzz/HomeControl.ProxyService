package proxyservice.model.cameradata;

public interface CameraDataProviderEventListener {
    void onProviderDataReceived(byte[] data);
}
