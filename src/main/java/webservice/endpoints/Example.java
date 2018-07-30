package webservice.endpoints;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mathworks.toolbox.javabuilder.MWException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import remote.TaskExecutor;
import remote.TaskResult;
import remote.tasks.AddTwoNumbersTask;

@Path("example")
public class Example {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GET
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject add(@QueryParam("first") int firstNumber, @QueryParam("second") int secondNumber) {
        JsonObjectBuilder jsonResponseBuilder = Json.createObjectBuilder();
        AddTwoNumbersTask task = null;
        try {
            task = new AddTwoNumbersTask(firstNumber, secondNumber);
            TaskResult result = TaskExecutor.execute(task);
            jsonResponseBuilder.add("result", result.getInteger(1));
            task.dispose();
            result.dispose();
            return jsonResponseBuilder.build();
        } catch (RemoteException | MWException | NotBoundException ex) {
            logger.error("Unable to execute task: {}", task.getClass().getSimpleName());
            logger.error("Reason", ex);
            return jsonResponseBuilder.add("error", ex.getCause().getMessage())
                    .add("status", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .build();
        }
    }

}