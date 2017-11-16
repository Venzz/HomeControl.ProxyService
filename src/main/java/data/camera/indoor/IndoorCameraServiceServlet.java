package data.camera.indoor;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
@WebServlet(name = "Indoor CameraProxy Service", urlPatterns = { "/indoor-service/*" })
public class IndoorCameraServiceServlet extends WebSocketServlet {
    public IndoorCameraServiceServlet() {
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(60000);
        factory.getPolicy().setMaxBinaryMessageBufferSize(1024 * 1024);
        factory.getPolicy().setMaxBinaryMessageSize(1024 * 1024);
        factory.register(IndoorCameraServiceSocket.class);
    }
}