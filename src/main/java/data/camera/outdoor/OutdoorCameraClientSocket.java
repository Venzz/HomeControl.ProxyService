package data.camera.outdoor;

import data.ClientSocket;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import service.Service;
import data.CommandPerformer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class OutdoorCameraClientSocket extends WebSocketAdapter implements ClientSocket {
    private List<CommandPerformer> performers = new ArrayList<CommandPerformer>();

    public OutdoorCameraClientSocket() {
        Service.outdoorCamera.add(this);
        System.out.println("Outdoor CameraProxy Client => Created.");
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        System.out.println("Outdoor CameraProxy Client => Connected.");
    }

    public synchronized void addPerformer(CommandPerformer performer) {
        performers.add(performer);
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
    public synchronized void onWebSocketBinary(byte[] payload, int offset, int len) {
        super.onWebSocketBinary(payload, offset, len);
        for (CommandPerformer performer : performers) {
            performer.send(payload);
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode,reason);
        Service.outdoorCamera.remove(this);
        System.out.println("Outdoor CameraProxy Client => Closed (" + statusCode + ", " + reason + ")");
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}
