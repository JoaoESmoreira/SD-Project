import java.rmi.*;

public interface Download extends Remote {
    String debug() throws java.rmi.RemoteException;
}