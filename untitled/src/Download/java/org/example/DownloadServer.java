
/*package org.example;

public class DownloadServer {

    public static void main(String[] args) {
        String url = "https://en.wikipedia.org/wiki/Castelo_de_Sines";


        try {
            Document doc = Jsoup.connect(url).get();
            StringTokenizer tokens = new StringTokenizer(doc.text());
            int countTokens = 0;

            while (tokens.hasMoreElements() && countTokens++ < 100)
                System.out.println(tokens.nextToken().toLowerCase());

            Elements links = doc.select("a[href]");

            for (Element link : links)
                System.out.println(link.text() + "\n" + link.attr("abs:href") + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/



import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.concurrent.SynchronousQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.StringTokenizer;
public class DownloadServer extends UnicastRemoteObject implements Download {
    static SynchronousQueue<String> urlQueue;

    public DownloadServer() throws RemoteException {
        super();
        urlQueue = new SynchronousQueue<>();
    }

    public String debug() throws RemoteException {
        System.out.println("Working on server!");

        return "Hello, World!";
    }

    // =========================================================
    public static void main(String[] args) {
        // Registry and Bind server
        try {
            DownloadServer server = new DownloadServer();
            Registry r = LocateRegistry.createRegistry(7000);
            r.rebind("DownloadNameServer", server);
            System.out.println("Server ready.");
        } catch (RemoteException re) {
            System.out.println("Exception in HelloImpl.main: " + re);
        }

        urlQueue.add("https://en.wikipedia.org/wiki/Castelo_de_Sines");

        try {
            String url = urlQueue.;
            Document doc = Jsoup.connect(url).get();
            StringTokenizer tokens = new StringTokenizer(doc.text());
            int countTokens = 0;

            while (tokens.hasMoreElements() && countTokens++ < 100)
                System.out.println(tokens.nextToken().toLowerCase());

            Elements links = doc.select("a[href]");

            for (Element link : links)
                System.out.println(link.text() + "\n" + link.attr("abs:href") + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
