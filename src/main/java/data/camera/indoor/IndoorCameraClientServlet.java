package data.camera.indoor;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet(name = "Indoor CameraProxy Client", urlPatterns = { "/indoor-client/*" })
public class IndoorCameraClientServlet extends WebSocketServlet {
    public IndoorCameraClientServlet() {
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(60000);
        factory.getPolicy().setMaxBinaryMessageBufferSize(1024 * 1024);
        factory.getPolicy().setMaxBinaryMessageSize(1024 * 1024);
        factory.register(IndoorCameraClientSocket.class);
    }
}