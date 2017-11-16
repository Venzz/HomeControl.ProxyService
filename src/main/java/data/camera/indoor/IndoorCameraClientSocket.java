package data.camera.indoor;

import data.ClientSocket;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import service.Service;

import java.nio.ByteBuffer;

public class IndoorCameraClientSocket extends WebSocketAdapter implements ClientSocket {
    public IndoorCameraClientSocket() {
        Service.indoorCamera.add(this);
        System.out.println("Indoor CameraProxy Client => Created.");
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        System.out.println("Indoor CameraProxy Client => Connected.");
    }

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
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        super.onWebSocketBinary(payload, offset, len);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode,reason);
        Service.indoorCamera.remove(this);
        System.out.println("Indoor CameraProxy Client => Closed (" + statusCode + ", " + reason + ")");
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}
