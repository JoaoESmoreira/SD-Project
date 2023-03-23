// import java.rmi.*;
// import java.rmi.registry.LocateRegistry;
// import java.rmi.registry.Registry;
// import java.rmi.server.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.example.Reader;
import org.example.Url;


public class DownloadServer /*extends UnicastRemoteObject implements Download*/ {
    static ConcurrentLinkedQueue<String> urlQueue;

    public DownloadServer() /*throws RemoteException*/ {
        super();
        urlQueue = new ConcurrentLinkedQueue<>();
    }

    /*public String debug() throws RemoteException {
        System.out.println("Working on server!");

        return "Hello, World!";
    }

    public static void getLinks(String url) {
        try {
            if (url != null)
            {
                Document doc = Jsoup.connect(url).get();
                // StringTokenizer tokens = new StringTokenizer(doc.text());

                // int countTokens = 0;
                // while (tokens.hasMoreElements() && countTokens++ < 100)
                //     System.out.println(tokens.nextToken().toLowerCase());
                // System.out.println("\n\n");

                Elements links = doc.select("a[href]");

                for (Element link : links)
                    urlQueue.add(link.attr("abs:href"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void main(String[] args) throws InterruptedException {
        // Registry and Bind server
        /*try {
            DownloadServer server = new DownloadServer();
            Registry r = LocateRegistry.createRegistry(7000);
            r.rebind("DownloadNameServer", server);
            System.out.println("Server ready.");
        } catch (RemoteException re) {
            System.out.println("Exception in ServerImpl.main: " + re);
        }*/

        System.out.println("Starting");
        Url url = new Url();

        ArrayList<Reader> readers = new ArrayList<>();
        for (int id = 0; id < 300; id += 1) {
            Reader reader = new Reader(url, id);
            readers.add(reader);
        }

        for (Reader reader:readers)
            reader.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        url.addUrl("https://en.wikipedia.org/wiki/Penafiel");

        // this is a test to add another url
        try {
            Thread.sleep(18000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("============================================================");
        url.addUrl("https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Queue.html#remove()");

        System.out.println(url.urlSet.size());
    }
}
