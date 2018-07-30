package remote;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.json.Json;
import javax.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import remoteproxy.Task;
import remoteproxy.TaskRunner;

public final class TaskExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskExecutor.class);

    private TaskExecutor() {
    }

    /**
     * @param task
     *                 This is an implementation of the Task interface, this
     *                 implementation should extend from MATLAB class and accept any
     *                 necessary arguments, e.g Class1 and it must implement
     *                 Serializable interface
     * @throws RemoteException
     * @throws NotBoundException
     */
    public static final TaskResult execute(Task task) throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(BootStrap.PORT);
        TaskRunner generator = (TaskRunner) reg.lookup(BootStrap.SERVER_NAME);
        Object[] result = generator.runTask(task);
        return new TaskResult(result);
    }

    public static final Process startRmiServer(String WEBINF) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Path webInfDirectory = Paths.get(WEBINF);
        processBuilder.directory(webInfDirectory.resolve("classes").toFile());
        processBuilder.command("java", "-cp", getClasspath(webInfDirectory.resolve("lib")), "remote.BootStrap");
        int retry = 0;
        while (true) {
            if (retry == 3) {
                LOGGER.error("Failed to start server. Exiting");
                System.exit(1);
            }
            try {
                return processBuilder.start();
            } catch (Exception ex) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    LOGGER.error("Thread interrupted while sleeping", e);
                }
                LOGGER.error("Failed to start server. Retrying in 5 seconds...", ex);
                retry++;
            }
        }
    }

    private static String getClasspath(Path libDirInWebInf) {
        JsonObject matlabConfig = getMatlabConfig(libDirInWebInf.getParent());
        String classpath = "." + File.pathSeparator
                + matlabConfig.getString("matlabHome")
                + File.separator
                + matlabConfig.getString("version")
                + File.separator
                + "toolbox"
                + File.separator
                + "javabuilder"
                + File.separator
                + "jar"
                + File.separator
                + "javabuilder.jar"
                + File.pathSeparator;
        try {
            classpath += Files.list(libDirInWebInf)
                    .map(Path::toString)
                    .collect(joining(File.pathSeparator));
        } catch (IOException e) {
            LOGGER.error("Unable to list content of lib directory in WEB-INF. Can't start server. Exiting", e);
            System.exit(1);
        }
        return classpath;
    }

    private static JsonObject getMatlabConfig(Path webInf) {
        try {
            return Json.createReader(Files.newInputStream(webInf.resolve("config.json")))
                    .readObject();
        } catch (IOException e) {
            LOGGER.error("Unable to read config.json file. Can't start server. Exiting", e);
            System.exit(1);
            return null;
        }
    }

}