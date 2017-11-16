package data.camera.outdoor;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet(name = "Outdoor CameraProxy Client", urlPatterns = { "/outdoor-client/*" })
public class OutdoorCameraClientServlet extends WebSocketServlet {
    public OutdoorCameraClientServlet() {
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(60000);
        factory.getPolicy().setMaxBinaryMessageBufferSize(1024 * 1024);
        factory.getPolicy().setMaxBinaryMessageSize(1024 * 1024);
        factory.register(OutdoorCameraClientSocket.class);
    }
}