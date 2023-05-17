package com.example.demo;

// import org.example.Download;

import com.example.demo.model.UrlModel;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;


@SpringBootApplication
public class SearchModule extends UnicastRemoteObject implements Inter {

	public static Download conection;
    static HashMap<String, String> Regists;


	static ArrayList<Pair<Binterface, Boolean>> clients;


	@Bean
	RmiProxyFactoryBean rmiProxy() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceInterface(Inter.class);
		bean.setServiceUrl("rmi://localhost:1099/helloworldrmi");

		return bean;
	}

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

	public ArrayList<UrlModel> saySearch(String s) throws RemoteException {

		//String output = "No SearchBarrel Operational";
		ArrayList<UrlModel> output = new ArrayList<>();
		ArrayList<UrlModel> outputf = new ArrayList<>();
		int cont=0;
		for (Pair<Binterface, Boolean> pair:clients) {
			System.out.println(s);
			if (!pair.getSecond()) {
				pair.setSecond(true);
				if(cont==0){
					outputf = pair.getFirst().getSearch(s);
				}
				else {
					output = pair.getFirst().getSearch(s);
				}
				pair.setSecond(false);
			}
			cont++;
		}
		// System.out.println(output);
		return outputf;
	}

	public ArrayList<UrlModel> pointToLink(String link) throws RemoteException {
		ArrayList<UrlModel> output = new ArrayList<>();
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
		String outputf = "No SearchBarrel Operational\n";
		int cont=0;
		for (Pair<Binterface, Boolean> pair:clients) {
			if (!pair.getSecond()) {
				pair.setSecond(true);
				if(cont==0){
					output = pair.getFirst().GetInfos();
				}
				else{
					outputf = pair.getFirst().GetInfos();
				}
				output = pair.getFirst().GetInfos();
				pair.setSecond(false);
			}
			cont++;
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

		try {

			SearchModule loginService = new SearchModule();
			LocateRegistry.createRegistry(6000).rebind("LoginService", loginService);

			System.out.println("Servidor RMI pronto para receber conexões...");
		} catch (Exception e) {
			System.err.println("Erro no servidor RMI: " + e);
			e.printStackTrace();
		}


	}
}
