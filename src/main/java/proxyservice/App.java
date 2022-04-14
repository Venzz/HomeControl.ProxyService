package proxyservice;

import proxyservice.logging.Logger;
import proxyservice.model.ApplicationModel;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import proxyservice.servlets.CameraDataConsumerServlet;
import proxyservice.servlets.CameraDataProviderServlet;

public class App {
    public static ApplicationModel model = new ApplicationModel();
    public static Logger logger = new Logger();

    public static void main(String[] args) throws Exception {
        logger.log("Free memory (bytes)", String.valueOf(Runtime.getRuntime().freeMemory()));
        logger.log("Maximum memory (bytes)", String.valueOf(Runtime.getRuntime().maxMemory()));

        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder("client", CameraDataConsumerServlet.class), "/client/*");
        context.addServlet(new ServletHolder("service", CameraDataProviderServlet.class), "/service/*");
        server.start();
        server.join();
    }
}
