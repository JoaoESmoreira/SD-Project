package org.example;

// import java.rmi.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Inter extends Remote {
	String sayURL(String s) throws RemoteException;
	String saySearch(String s) throws RemoteException;
	String pointToLink(String link) throws RemoteException;
	String Register(String username, String password ) throws RemoteException;
	String Login(String username, String password ) throws RemoteException;

	String registerBarrel(Binterface client) throws RemoteException;
	void logoutBarrel (Binterface client) throws RemoteException;
}