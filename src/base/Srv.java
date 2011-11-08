package base;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
*  Server Interface
*  
*  The Srv implements the Stein Server 
*  
* @author runar & alex
* @version 1.0
*/

public interface Srv extends Remote {

	 /**
	 * The name of the game Server. 
	 */
	public static final String SERVICE_NAME = "SteinEngine";
	 
	 /**
	  * Register a {@link Player} at the Server.
	 * @param p		Player
	 * @throws RemoteException
	 */
	 public void register(Player p) throws RemoteException;

	 /**
	  * Remove a {@link Player} from the Server
	 * @param p
	 * @throws RemoteException
	 */
	public void leave(Player p) throws RemoteException;
	 
	 /**
	 * Load a game at the server.
	 * @param opponent	opponent's name
	 * @param name		player's name
	 * @return			A Game object.
	 * @throws RemoteException
	 */
	 public Game loadGame(String opponent, String name) throws RemoteException;

	 
	 /**
	  * Get the players in an array for the list
	 * @return
	 * @throws RemoteException
	 */
	public Object[] getPlayers() throws RemoteException;
	 
	 /**
	  * End a game at the server.
	 * @param game
	 */
	public void remove(Game game) throws RemoteException;
	
	
}
