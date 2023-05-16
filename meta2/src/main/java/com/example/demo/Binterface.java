package com.example.demo;//package src.Download.java.org.example;

import com.example.demo.model.UrlModel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Binterface extends Remote{
    ArrayList<UrlModel> getSearch(String search) throws RemoteException;
    String getPointToLink (String point) throws RemoteException;
    public String GetInfos() throws RemoteException;
    public String Regist(String username, String password) throws RemoteException;
    public String Log_in(String username, String password) throws RemoteException;
}
