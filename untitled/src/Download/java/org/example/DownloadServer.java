import java.util.ArrayList;

import org.example.Reader;
import org.example.Url;
import src.Download.java.org.example.Download;

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

        /*try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        url.printRelevanteIndex();

        String search = "flag";
        String[] tokens = search.split(" ");

        ArrayList<CopyOnWriteArraySet<String>> relevantIndexArray = new ArrayList<>();
        for (String string:tokens) {
            CopyOnWriteArraySet<String> set = url.getIndex(string);
            relevantIndexArray.add(set);
        }

        for (int i = 1; i < relevantIndexArray.size(); ++i)
            relevantIndexArray.get(0).retainAll(relevantIndexArray.get(i));

        ArrayList<String> relevantUrl = new ArrayList<>(relevantIndexArray.get(0));

        relevantUrl.sort((s, t1) -> url.getRelevantIndexSize(s) > url.getRelevantIndexSize(t1) ? -1 : (url.getRelevantIndexSize(s) > url.getRelevantIndexSize(t1)) ? 1 : 0);

        if (relevantUrl.size() > 0) {
            for (String urlFromRelevant:relevantUrl)
                System.out.println("Enjoy: " + urlFromRelevant + " . " + url.getRelevantIndexSize(urlFromRelevant));
        } else {
            System.out.println("No results found");
        }


        String point = "https://en.wikipedia.org/w/index.php?title=Special:CreateAccount&returnto=Penafiel";

        CopyOnWriteArraySet<String> aux = url.getRelevantIndex(point);
        if (aux != null) {
            for (String str:aux) {
                System.out.println("Poited by: " + str);
            }
        }*/
    }
}
