
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        
        try {
			Inter h = (Inter) LocateRegistry.getRegistry(7000).lookup("ALGO");

			System.out.print("URL: ");
			Scanner sc = new Scanner(System.in);
			byte[] buffer2 = new byte[256];
			String urll = sc.nextLine();

			String message = h.sayURL(urll);
			System.out.println("MENSAGEM recebida: " + message);

		} catch (Exception e) {
			System.out.println("Exception in main: " + e);
			e.printStackTrace();
		}

	}


}

