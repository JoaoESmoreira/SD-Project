package org.example;

import org.example.Binterface;
import src.Download.java.org.example.Download;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class SearchModule extends UnicastRemoteObject implements Inter {

    static HashMap<String, String> Regists;
	static ArrayList<String> LoggedIN;

	private List<Binterface> clients;

    public SearchModule() throws RemoteException {
		super();
        Regists = new HashMap<>();
		LoggedIN = new ArrayList<>();
		clients = new ArrayList<>();
	}

    public String sayURL(String s) throws RemoteException {
        System.out.println("server: "+s);

		try{
			Download conection = (Download) LocateRegistry.getRegistry(8000).lookup("DownloadNameServer");
			conection.GetURL(s);

		} catch (Exception e) {
			System.out.println("Exception in 8000: " + e);
			e.printStackTrace();
		}

		return "URL sent";
    }

	public String registerBarrel(Binterface client) throws RemoteException {
		clients.add(client);
		System.out.println("Barrel registed");


		for (Binterface s : clients) {
			System.out.println(s.Infos("teste"));
		}


		return "Barrel registed";
	}

	public String saySearch(String s) throws RemoteException {
		System.out.println("server: "+s);

		return "Error";
	}

    public String Register(String username, String password ) throws RemoteException{

		if(Regists.get(username)== null){
			Regists.put(username,password);
			return "Regist Done";
		} else{
			return "Username already registed";
		}

    }

    public String Login(String username, String password ) throws RemoteException{

		if(Regists.get(username)== null){
			return "Need to register";
		} else{
			if(Objects.equals(Regists.get(username), password)){
				LoggedIN.add(username);
				return "LOGGED IN";
			}
			return "Wrong password";
		}


    }


    public static void main(String[] args) {
        try {

			System.getProperties().put("java.security.policy","policy.all") ;
			//System.setSecurityManager(new RMISecurityManager()); 

			Inter h = new SearchModule();
		    LocateRegistry.createRegistry(7000).rebind("ALGO", h);

			System.out.println("Search Module ready.");

		} catch (RemoteException re) {
			System.out.println("Exception in main: " + re);
		} 
		//catch (MalformedURLException me){
		//	System.out.println("Exception : " + me);
		 //catch (NotBoundException ne){
			//System.out.println("in main: " + ne);
		//}


		try {

			SearchModule con = new SearchModule();
			LocateRegistry.createRegistry(5000).rebind("barrel", con);
			System.out.println("Conection to barrels ");

		} catch (Exception re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}


	}
}
