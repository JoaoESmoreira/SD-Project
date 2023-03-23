
import java.rmi.registry.LocateRegistry;

public class DownloadClient {

    public static void main(String[] args) {

        try {

            Download downloader = (Download) LocateRegistry.getRegistry(7000).lookup("DownloadNameServer");

            String message = downloader.debug();
            System.out.println("Server said: " + message);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
            e.printStackTrace();
        }

    }

}