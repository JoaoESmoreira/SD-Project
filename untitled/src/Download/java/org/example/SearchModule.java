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

		return "URL Sent";
    }

	public String saySearch(String s) throws RemoteException {
		System.out.println("server: "+s);

		return "Search Sent";
	}

    public static void main(String[] args) {
        try {

			//criar thread para comunicar com barrel

			//isto fica no programa principal
			Inter h = new SearchModule();
		    LocateRegistry.createRegistry(7000).rebind("ALGO", h);
			System.out.println("Search Module ready.");
		} catch (RemoteException re) {
			System.out.println("Exception in main: " + re);
		}
    }
}
