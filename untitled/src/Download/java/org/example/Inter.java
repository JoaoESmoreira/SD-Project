import java.rmi.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Inter extends Remote {
	public String sayURL(String s) throws java.rmi.RemoteException;
}