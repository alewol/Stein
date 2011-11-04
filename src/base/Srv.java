package base;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Srv extends Remote {

	 public static final String SERVICE_NAME = "SteinEngine";
	 
	 public void register(Player p) throws RemoteException;

	 /**
	  * Get array of Users
	  * @return Object[] - array of User objects
	  * @throws RemoteException
	  */
	 public Object[] getPlayers() throws RemoteException;

	public Game createGame(String opponent, String name) throws RemoteException;
	public Game getGame(String name, String attacker) throws RemoteException;
	 
}
