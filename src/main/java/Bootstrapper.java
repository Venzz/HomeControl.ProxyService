import data.camera.outdoor.OutdoorCameraClientServlet;
import data.camera.outdoor.OutdoorCameraServiceServlet;
import data.camera.indoor.IndoorCameraClientServlet;
import data.camera.indoor.IndoorCameraServiceServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

public class Bootstrapper {
    public static void main(String[] args) throws Exception{
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder("outdoor-client", OutdoorCameraClientServlet.class), "/outdoor-client/*");
        context.addServlet(new ServletHolder("outdoor-service", OutdoorCameraServiceServlet.class), "/outdoor-service/*");
        context.addServlet(new ServletHolder("indoor-client", IndoorCameraClientServlet.class), "/indoor-client/*");
        context.addServlet(new ServletHolder("indoor-service", IndoorCameraServiceServlet.class), "/indoor-service/*");
        server.start();
        server.join();
    }
}