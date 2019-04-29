package proxyservice.servlets;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet(name = "Data Provider", urlPatterns = { "/service/*" })
public class CameraDataProviderServlet extends WebSocketServlet {
    public CameraDataProviderServlet() {
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(60000);
        factory.getPolicy().setMaxBinaryMessageBufferSize(1024 * 1024);
        factory.getPolicy().setMaxBinaryMessageSize(1024 * 1024);
        factory.register(CameraDataProviderSocket.class);
    }
}