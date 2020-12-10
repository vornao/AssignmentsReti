package Assignment11;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface CongressInterface extends Remote {

    /**
     * @return null if schedule is empty, Arraylist of hasmaps containing session and speakers o/w.
     * @throws RemoteException if errors during remote call.
     */
    ArrayList<HashMap<Integer, ArrayList<String>>> getSchedule() throws RemoteException;

    /**
     *
     * @param name speaker's username
     * @param day  day in which speaker wants to enroll
     * @param slot session slot in which speaker wants to enroll
     * @return true if success, false if params wrong.
     * @throws RemoteException if any errors during remote call.
     */
    boolean enrollSpeaker(String name, int day, int slot) throws RemoteException;

}
