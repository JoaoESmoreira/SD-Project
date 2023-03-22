//package src.Download.java.org.example;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class StorageBarrel extends UnicastRemoteObject implements Binterface {

    StorageBarrel() throws RemoteException{
        super();
    }

    public String Infos(String s) throws RemoteException{
        System.out.println("In barrel: "+s);
        return "Info from barrel";
    }

    public static void main(String[] args) {
        try {
            Binterface h = new StorageBarrel();
            LocateRegistry.createRegistry(9000).rebind("barrel", h);
            System.out.println("Storage Barrel ready.");
        } catch (RemoteException re) {
            System.out.println("Exception in barrel: " + re);
        }

    }
}
