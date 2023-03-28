package org.example;//package src.Download.java.org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Binterface extends Remote{
    String getSearch(String search) throws RemoteException;
    String getPointToLink (String point) throws RemoteException;
}
