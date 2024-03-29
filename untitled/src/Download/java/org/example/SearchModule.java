package org.example;

// import org.example.Download;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class SearchModule extends UnicastRemoteObject implements Inter {

	public static Download conection;
    static HashMap<String, String> Regists;


	static ArrayList<Pair<Binterface, Boolean>> clients;

    public SearchModule() throws RemoteException {
		super();
        Regists = new HashMap<>();
		clients = new ArrayList<>();
	}


	public String sayURL(String s) throws RemoteException {
        System.out.println("server: "+s);

		try{
			conection.GetURL(s);

		} catch (Exception e) {
			System.out.println("Exception in 8000: " + e);
			e.printStackTrace();
		}

		return "URL sent";
    }

	public String registerBarrel(Binterface client) throws RemoteException {
		clients.add(new Pair<>(client, false));
		System.out.println("Barrel registed");

		return "Barrel registed";
	}

	public void logoutBarrel (Binterface client) throws RemoteException {
		boolean remove = clients.removeIf(obj -> obj.getFirst().equals(client));
		if (remove) {
			System.out.println("\nBarrel removed\n");
		}
	}

	public String saySearch(String s) throws RemoteException {
		String output = "No SearchBarrel Operational";
		for (Pair<Binterface, Boolean> pair:clients) {
			if (!pair.getSecond()) {
				pair.setSecond(true);
				output = pair.getFirst().getSearch(s);
				pair.setSecond(false);
			}
		}
		return output;
	}

	public String pointToLink(String link) throws RemoteException {
		String output = "No SearchBarrel Operational";
		for (Pair<Binterface, Boolean> pair:clients) {
			if (!pair.getSecond()) {
				pair.setSecond(true);
				output = pair.getFirst().getPointToLink(link);
				pair.setSecond(false);
				break;
			}
		}
		return output;
	}

    public String Register(String username, String password ) throws RemoteException{

		String output = "No SearchBarrel Operational";
		for (Pair<Binterface, Boolean> pair:clients) {
			if (!pair.getSecond()) {
				pair.setSecond(true);
				output = pair.getFirst().Regist(username, password);
				pair.setSecond(false);
			}
		}
		return output;

    }

    public String Login(String username, String password ) throws RemoteException{

		String output = "No SearchBarrel Operational";
		for (Pair<Binterface, Boolean> pair:clients) {
			if (!pair.getSecond()) {
				pair.setSecond(true);
				output = pair.getFirst().Log_in(username,password);
				pair.setSecond(false);
				break;
			}
		}
		return output;

    }


	public String Stats() throws RemoteException {
		String output = "No SearchBarrel Operational\n";
		for (Pair<Binterface, Boolean> pair:clients) {
			if (!pair.getSecond()) {
				pair.setSecond(true);
				output = pair.getFirst().GetInfos();
				pair.setSecond(false);
			}
		}
		output = output.concat("Number of Active Barrels: " + clients.size() + "\n" );
		output = output.concat("Number of Active Downloaders: " + conection.getNdownloaders() + "\n" );

		return output;
	}

	public static void main(String[] args) {
        try {
			System.getProperties().put("java.security.policy","policy.all") ;
			//System.setSecurityManager(new RMISecurityManager()); 

			Inter h = new SearchModule();
		    LocateRegistry.createRegistry(7000).rebind("ALGO", h);


		} catch (RemoteException re) {
			System.out.println("Exception in main: " + re);
		} 

		try{
			conection = (Download) LocateRegistry.getRegistry(8000).lookup("DownloadNameServer");

		} catch (Exception e) {
			System.out.println("Exception in 8000: " + e);
			e.printStackTrace();
		}

		try {
			SearchModule con = new SearchModule();
			LocateRegistry.createRegistry(5000).rebind("barrel", con);
			System.out.println("Search Module ready.");
		} catch (Exception re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}


	}
}
