
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class DownloadClient {

    public static void main(String[] args) {

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