package data.camera.outdoor;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet(name = "Outdoor CameraProxy Service", urlPatterns = { "/outdoor-service/*" })
public class OutdoorCameraServiceServlet extends WebSocketServlet {
    public OutdoorCameraServiceServlet() {
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(60000);
        factory.getPolicy().setMaxBinaryMessageBufferSize(1024 * 1024);
        factory.getPolicy().setMaxBinaryMessageSize(1024 * 1024);
        factory.register(OutdoorCameraServiceSocket.class);
    }
}