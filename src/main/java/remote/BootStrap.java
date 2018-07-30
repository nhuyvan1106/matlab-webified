package remote;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import remoteproxy.TaskRunnerRemoteObject;

public final class BootStrap {
    public static final int PORT = 6666;
    public static final String SERVER_NAME = "RUNNER_" + PORT;

    private static final Logger LOGGER = LoggerFactory.getLogger(BootStrap.class);

    public static void main(String[] args) {
        System.setProperty("java.rmi.server.hostname", "localhost");
        Registry registry = getRegistry();
        if (registry == null) {
            LOGGER.error("Unable to locate registry server on port " + PORT);
            System.exit(1);
        }
        try {
            registry.rebind(SERVER_NAME, UnicastRemoteObject.exportObject(new TaskRunnerRemoteObject(), 0));
        } catch (RemoteException e) {
            LOGGER.error("Unable to rebind a task runner remote object.", e);
        }
    }

    private static Registry getRegistry() {
        try {
            return LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            LOGGER.error("Unable to create registry on port " + PORT, e);
            try {
                return LocateRegistry.getRegistry(PORT);
            } catch (RemoteException e1) {
                LOGGER.error("Unable to get registry on port " + PORT, e1);
                return null;
            }
        }
    }

}