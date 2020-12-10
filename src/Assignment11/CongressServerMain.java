package Assignment11;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CongressServerMain {
    private static final int DAYS = 3;
    private static final int SESSIONS_PER_DAY = 12;
    private static final int MAX_SPEAKERS_PER_SESSION = 5;
    private static final int PORT = 6789;

    public static void main(String[] args) throws RemoteException {
        try {
            CongressServer congressServer = new CongressServer(DAYS, SESSIONS_PER_DAY, MAX_SPEAKERS_PER_SESSION);
            CongressInterface congressStub = (CongressInterface) UnicastRemoteObject.exportObject(congressServer, PORT);
            LocateRegistry.createRegistry(PORT);
            Registry registry = LocateRegistry.getRegistry("localhost", PORT);
            registry.rebind("CONGRESS-SERVER", congressStub);
            System.out.println("RMI SERVER READY PORT 6789");
        }catch(RemoteException e){
            System.out.println("RMI SERVER ERROR");
            System.exit(-1);
        }
    }
}
