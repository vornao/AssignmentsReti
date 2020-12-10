package Assignment11;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Si progetti un’applicazione Client/Server per la gestione delle registrazioni ad un congresso.
 * L’organizzazione del congresso fornisce agli speaker delle varie sessioni un’interfaccia tramite
 * la quale iscriversi ad una sessione, e la possibilità di visionare i programmi
 * delle varie giornate del congresso, con gli interventi delle varie sessioni.
 * Il server mantiene i programmi delle 3 giornate del congresso, ciascuno dei quali è memorizzato in una struttura
 * dati come quella mostrata di seguito, in cui ad ogni riga corrisponde una sessione (in tutto 12 per ogni giornata).
 * Per ciascuna sessione vengono memorizzati i nomi degli speaker che si sono registrati (al massimo 5).
 *
 * Il client può richiedere operazioni per:
 *
 * registrare uno speaker ad una sessione;
 * ottenere il programma del congresso;
 *
 * Il client inoltra le richieste al server tramite il meccanismo di RMI. Prevedere, per ogni possibile operazione
 * una gestione di eventuali condizioni anomale (ad esempio la richiesta di registrazione ad una giornata
 * e/o sessione inesistente oppure per la quale sono già stati coperti tutti gli spazi d’intervento)
 *
 * Il client è implementato come un processo ciclico che continua a fare richieste sincrone
 * fino ad esaurire tutte le esigenze utente.
 * Stabilire una opportuna condizione di terminazione del processo di richiesta.
 *
 *
 */

public class CongressServer extends RemoteServer implements CongressInterface {

    private ArrayList<HashMap<Integer, ArrayList<String>>> days;
    private int maxSessionsPerDay;
    private int maxDays;
    private int maxSpeakersPerSession;

    public CongressServer(int maxDays, int sessionsPerDay, int maxSpeakersPerSession){
        this.maxDays = maxDays;
        this.maxSessionsPerDay= sessionsPerDay;
        this.maxSpeakersPerSession = maxSpeakersPerSession;

        days = new ArrayList<>(maxDays);
        for(int i = 0; i < this.maxDays; i++){
            days.add(i, new HashMap<>(this.maxSessionsPerDay));
            for(int j = 0; j < this.maxSessionsPerDay; j++)
                days.get(i).put(j, new ArrayList<>(this.maxSpeakersPerSession));
        }
    }


    @Override
    public synchronized ArrayList<HashMap<Integer, ArrayList<String>>> getSchedule() throws RemoteException {
        return days;
    }

    @Override
    public synchronized boolean enrollSpeaker(String name, int day, int slot) throws RemoteException {
        if(this.days.get(day).get(slot).size() < maxSpeakersPerSession)
        return this.days.get(day).get(slot).add(name);
        else return false;
    }

    public void printSchedule(){
        Iterator it;

        for(HashMap e : days){
            System.out.println("**********************");
            it = e.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry entry = (Map.Entry) it.next();
                System.out.println("Slot: "
                        + entry.getKey().toString()
                        + " Speakers: "
                        + entry.getValue().toString());
            }
        }
        System.out.println("**********************");
    }
}
