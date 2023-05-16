package com.example.demo;

// import java.rmi.*;
import com.example.demo.model.UrlModel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Inter extends Remote {
	String sayURL(String s) throws RemoteException;
	ArrayList<UrlModel> saySearch(String s) throws RemoteException;
	String pointToLink(String link) throws RemoteException;
	String Register(String username, String password ) throws RemoteException;
	String Login(String username, String password ) throws RemoteException;

	String registerBarrel(Binterface client) throws RemoteException;
	void logoutBarrel (Binterface client) throws RemoteException;
	String Stats() throws RemoteException;

	public String saySomething(String msg) throws RemoteException;

}