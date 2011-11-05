package base;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author runar
 *
 */
public interface Srv extends Remote {

	 /**
	 * The name of the game Server. 
	 */
	public static final String SERVICE_NAME = "SteinEngine";
	 
	 /**
	  * Register a {@link Player} by name.
	 * @param p		player's name
	 * @throws RemoteException
	 */
	 public void register(Player p) throws RemoteException;

	 /**
	 * Get array of players.
	 * @return Object[]	array of {@link Player} objects
	 * @throws RemoteException
	 */
	 public Object[] getPlayers() throws RemoteException;

	 /**
	 * Load a game at the server.
	 * @param opponent	opponent's name
	 * @param name		player's name
	 * @return			A Game object.
	 * @throws RemoteException
	 */
	 public Game loadGame(String opponent, String name) throws RemoteException;

	 /**
	  * End a game at the server.
	 * @param game
	 */
	public void endGame(Game game) throws RemoteException;
	
	
}
