package webservice;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import remote.*;

@Provider
public class ApplicationStartupEventListener implements ApplicationEventListener {

    @Context
    private ServletContext servletContext;

    private Process process = null;

    @Override
    public void onEvent(ApplicationEvent event) {
        if (event.getType() == ApplicationEvent.Type.INITIALIZATION_FINISHED) {
            process = TaskExecutor.startRmiServer(servletContext.getRealPath("/WEB-INF"));
        } else if (event.getType() == ApplicationEvent.Type.DESTROY_FINISHED)
            process.destroyForcibly();
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return null;
    }

}