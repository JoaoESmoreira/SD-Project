package com.example.demo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class DownloadServer extends UnicastRemoteObject implements Download {
    static Url url;
    static ArrayList<Reader> readers;
    int ndownloaders;

    public DownloadServer() throws RemoteException {
        super();
        url = new Url();
        readers = new ArrayList<>();
    }

    public int getNdownloaders() throws RemoteException {
        return ndownloaders;
    }

    public void Setndownloaders(int n){
        this.ndownloaders = n;
    }

    public void GetURL(String s) throws RemoteException, InterruptedException {

        url.addUrl(s);
    }

    public static void main(String[] args) {
        // Registry and Bind server

        int downloaders = 10;

        try {
            DownloadServer server = new DownloadServer();
            Registry r = LocateRegistry.createRegistry(8000);
            r.rebind("DownloadNameServer", server);
            server.Setndownloaders(downloaders);

            System.out.println("Server ready.");
        } catch (RemoteException re) {
            System.out.println("Exception in ServerImpl.main: " + re);
        }

        System.out.println("Starting");
        for (int id = 0; id < downloaders; ++id) {
            Reader reader = new Reader(url, id);
            readers.add(reader);
        }

        for (Reader reader:readers)
            reader.start();
    }
}
