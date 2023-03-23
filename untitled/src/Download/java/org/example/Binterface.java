//package src.Download.java.org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Binterface extends Remote{
    public String Infos(String s) throws RemoteException;
}
