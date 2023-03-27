package org.example;//package src.Download.java.org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Binterface extends Remote{
    public String getSearch(String search) throws RemoteException;
    public String getPointToLink (String point) throws RemoteException;
}
