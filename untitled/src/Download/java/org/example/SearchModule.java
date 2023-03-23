package org.example;

import org.example.Binterface;
import src.Download.java.org.example.Download;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;


public class SearchModule extends UnicastRemoteObject implements Inter {

	private Binterface c;

    public SearchModule() throws RemoteException {
		super();
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

		//try {
		//	Binterface c = (Binterface) Naming.lookup("barrel");
		//	return 
		//}
		//catch (Exception e) {
		//	System.out.println("Exception in comunication: " + e);
		//	e.printStackTrace();
		//}
		

		return "URL sent";
    }

	public String saySearch(String s) throws RemoteException {
		System.out.println("server: "+s);

		//System.setSecurityManager(new RMISecurityManager());
		try{
			Binterface h2 = (Binterface) LocateRegistry.getRegistry(9000).lookup("barrel");

			return h2.Infos(s);

		}catch(Exception e){
			System.out.println("Exception in 9000: " + e);
			e.printStackTrace();
		}


		return "Error";
	}

	public void setC(Binterface c){
		this.c = c;
	}

	public Binterface getC(){
		return c;
	}

    public static void main(String[] args) {
        try {

			System.getProperties().put("java.security.policy","policy.all") ;
			//System.setSecurityManager(new RMISecurityManager()); 

			Inter h = new SearchModule();
		    LocateRegistry.createRegistry(7000).rebind("ALGO", h);



			//SearchModule h2 = new SearchModule();
			//h2.setC((Binterface) Naming.lookup("barrel"));

			System.out.println("Search Module ready.");

		} catch (RemoteException re) {
			System.out.println("Exception in main: " + re);
		} 
		//catch (MalformedURLException me){
		//	System.out.println("Exception : " + me);
		 //catch (NotBoundException ne){
			//System.out.println("in main: " + ne);
		//}
    }
}
