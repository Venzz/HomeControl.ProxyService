package data.camera.outdoor;

import data.ServiceSocket;
import logging.Log;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import service.Service;

import java.nio.ByteBuffer;

public class OutdoorCameraServiceSocket extends WebSocketAdapter implements ServiceSocket {
    private Log log = new Log("Outdoor CameraProxy Service");

    public OutdoorCameraServiceSocket() {
        Service.outdoorCamera.set(this);
        System.out.println("Outdoor CameraProxy Service => Created.");
    }

    @Override
    public void send(byte[] data) {
        try {
            if (isNotConnected()) {
                return;
            }
            RemoteEndpoint endpoint = getRemote();
            endpoint.sendBytes(ByteBuffer.wrap(data));
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        System.out.println("Outdoor CameraProxy Service => Connected.");
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        super.onWebSocketBinary(payload, offset, len);
        log.logData(payload);
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
