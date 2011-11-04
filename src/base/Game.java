package base;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Game extends Remote {

	public String owner() throws RemoteException;
	public String title() throws RemoteException;
	public String opponent(String name) throws RemoteException;
	public void move(String name, String move) throws RemoteException;
	public boolean amIstupid(String name) throws RemoteException;
	public boolean finished() throws RemoteException;
	public String wins(String name) throws RemoteException;
	public String draws(String name) throws RemoteException;
	public String defeats(String name) throws RemoteException;
	public void withdraw() throws RemoteException;
	public boolean canceled() throws RemoteException;
	public void endTimes() throws RemoteException;

}