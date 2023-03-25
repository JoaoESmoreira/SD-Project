// import java.rmi.*;
// import java.rmi.registry.LocateRegistry;
// import java.rmi.registry.Registry;
// import java.rmi.server.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.example.Reader;
import org.example.Url;
import src.Download.java.org.example.Download;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

import static java.lang.Thread.sleep;


public class DownloadServer extends UnicastRemoteObject implements Download , Runnable {
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

            Thread thread = new Thread(server);
            thread.start();

            System.out.println("Server ready.");
        } catch (RemoteException re) {
            System.out.println("Exception in ServerImpl.main: " + re);
        }

        //multicasting thread


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

    public void run() {
        MulticastSocket socket = null;
        long counter = 0;
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("ola");
        arrayList.add("boas");
        System.out.println(" running...");
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            while (true) {
                String message = " packet " + counter++;
                byte[] buffer = message.getBytes();

                String MULTICAST_ADDRESS = "224.3.2.1";
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                int PORT = 4321;

                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
                objOut.writeObject(arrayList);
                objOut.flush();

                byte[] byteArray = byteOut.toByteArray();
                System.out.println("Byte array length: " + byteArray.length);

                DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length , group, PORT);
                socket.send(packet);

                try {
                    long SLEEP_TIME = 5000;
                    sleep((long) (Math.random() * SLEEP_TIME)); } catch (InterruptedException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert socket != null;
            socket.close();
        }
    }
}
