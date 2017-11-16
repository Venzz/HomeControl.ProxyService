package data.camera.outdoor;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import service.Service;

public class OutdoorCameraServiceSocket extends WebSocketAdapter {
    public OutdoorCameraServiceSocket() {
        System.out.println("Outdoor CameraProxy Service => Created.");
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        System.out.println("Outdoor CameraProxy Service => Connected.");
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        super.onWebSocketBinary(payload, offset, len);
        System.out.println("Outdoor CameraProxy Service => " + payload.length + " bytes.");
        Service.outdoorCamera.send(payload);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode,reason);
        System.out.println("Outdoor CameraProxy Service => Closed (" + statusCode + ", " + reason + ")");
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}
