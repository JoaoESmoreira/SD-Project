import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;


public class SearchModule extends UnicastRemoteObject implements Inter{

    public SearchModule() throws RemoteException {
		super();
	}

    public String sayURL(String s) throws RemoteException {
        System.out.println("server: "+s);

		return "URL ENVIADO";
    }

    public static void main(String[] args) {
        try {
			Inter h = new SearchModule();
		    LocateRegistry.createRegistry(7000).rebind("ALGO", h);
			System.out.println("Search Module ready.");
		} catch (RemoteException re) {
			System.out.println("Exception in main: " + re);
		}
    }
}
