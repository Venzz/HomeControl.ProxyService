package data.camera.indoor;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import service.Service;

public class IndoorCameraServiceSocket extends WebSocketAdapter {
    public IndoorCameraServiceSocket() {
        System.out.println("Indoor CameraProxy Service => Created.");
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        System.out.println("Indoor CameraProxy Service => Connected.");
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        super.onWebSocketBinary(payload, offset, len);
        System.out.println("Indoor CameraProxy Service => " + payload.length + " bytes.");
        Service.indoorCamera.send(payload);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode,reason);
        System.out.println("Indoor CameraProxy Service => Closed (" + statusCode + ", " + reason + ")");
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}
