// import java.rmi.*;
// import java.rmi.registry.LocateRegistry;
// import java.rmi.registry.Registry;
// import java.rmi.server.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.example.Reader;
import org.example.Url;
import src.Download.java.org.example.Download;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;


public class DownloadServer extends UnicastRemoteObject implements Download {
    // static ConcurrentLinkedQueue<String> urlQueue;
    static Url url;
    static ArrayList<Reader> readers;

    public DownloadServer() throws RemoteException {
        super();
        // urlQueue = new ConcurrentLinkedQueue<>();
        url = new Url();
        readers = new ArrayList<>();
    }

    public void GetURL(String s) throws RemoteException, InterruptedException {
        System.out.println("Got the URL: "+s);

        url.addUrl(s);
    }

    public static void main(String[] args) {
        // Registry and Bind server
        try {

            DownloadServer server = new DownloadServer();
            Registry r = LocateRegistry.createRegistry(8000);
            r.rebind("DownloadNameServer", server);
            System.out.println("Server ready.");
        } catch (RemoteException re) {
            System.out.println("Exception in ServerImpl.main: " + re);
        }

        System.out.println("Starting");

        for (int id = 0; id < 300; id += 1) {
            Reader reader = new Reader(url, id);
            readers.add(reader);
        }

        for (Reader reader:readers)
            reader.start();

        // url.addUrl("https://en.wikipedia.org/wiki/Penafiel");

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        url.printRelevanteIndex();
        // System.out.println(url);
        // System.out.println(url.urlSet.size());
    }
}
