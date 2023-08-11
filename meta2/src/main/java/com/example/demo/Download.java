package com.example.demo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Download extends Remote {
    void GetURL(String url) throws RemoteException, InterruptedException;
    public int getNdownloaders() throws RemoteException;
}