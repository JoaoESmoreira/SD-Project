package org.example;

import java.rmi.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Inter extends Remote {
	public String sayURL(String s) throws RemoteException;
	public String saySearch(String s) throws RemoteException;
	public String Register(String username, String password ) throws RemoteException;
	public String Login(String username, String password ) throws RemoteException;
}