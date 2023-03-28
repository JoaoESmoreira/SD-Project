import java.util.ArrayList;

import org.example.Reader;
import org.example.Url;
import org.example.Download;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;


public class DownloadServer extends UnicastRemoteObject implements Download {
    static Url url;
    static ArrayList<Reader> readers;

    public DownloadServer() throws RemoteException {
        super();
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
        for (int id = 0; id < 10; ++id) {
            Reader reader = new Reader(url, id);
            readers.add(reader);
        }

        for (Reader reader:readers)
            reader.start();
    }
}
