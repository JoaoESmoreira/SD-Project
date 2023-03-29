package org.example;//package src.Download.java.org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Binterface extends Remote{
    String getSearch(String search) throws RemoteException;
    String getPointToLink (String point) throws RemoteException;
    public String GetInfos() throws RemoteException;
    public String Regist(String username, String password) throws RemoteException;
    public String Log_in(String username, String password) throws RemoteException;
}
