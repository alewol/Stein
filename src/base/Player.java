package base;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Player Interface.
 * 
 * The Player represents the person participating in Stein games.
 * 
 * @author alex & runar
 * @version 1.0
 */
public interface Player extends Remote {

	/**
	 * Say Hello...
	 * @return "Hello " +  Player name
	 * @throws RemoteException
	 */
	public String Hello() throws RemoteException;
	
	/**
	 * Get Player name.
	 * @return Player name
	 * @throws RemoteException
	 */
	public String name() throws RemoteException;
	
	/**
	 * Announce this Player about {@link Server} changes.
	 * @throws RemoteException
	 */
	public void announce() throws RemoteException;
	
	/**
	 * Create a new {@link Game} between this Player and a given opponent (by opponent name).
	 * @param opponent
	 * @throws RemoteException
	 */
	public void newGame(String opponent) throws RemoteException;
	
	/**
	 * See if this Player the one starting the {@link Game}.
	 * @return
	 * @throws RemoteException
	 */
	public boolean agressive() throws RemoteException;
	
	/**
	 * Return the name of the opposing player in the current {@link Game}.
	 * @return
	 * @throws RemoteException
	 */
	public String opponent() throws RemoteException;
	
	/**
	 * Perform the given move in the current game.
	 * @param move
	 * @throws RemoteException
	 */
	public void move(String move) throws RemoteException;
	
	/**
	 * Receive a {@link Game} challenge from the given attacker (by name).
	 * @param attacker
	 * @throws RemoteException
	 */
	public void challenge(String attacker) throws RemoteException;
	
	/**
	 * Get number of wins for Player
	 * @return number of wins
	 * @throws RemoteException
	 */
	public int wins() throws RemoteException;
	
	/**
	 * Get number of draws for Player
	 * @return number of draws
	 * @throws RemoteException
	 */
	public int draws() throws RemoteException;
	
	/**
	 * Get number of defeats for Player
	 * @return number of defeats
	 * @throws RemoteException
	 */
	public int defeats() throws RemoteException;
	
	/**
	 * Withdraw this Player from the current game
	 * @throws RemoteException
	 */
	public void withdraw() throws RemoteException;
	
	/**
	 * End the current {@link Game} and remove this from the {@link Server}.
	 * @throws RemoteException
	 */
	public void endGame() throws RemoteException;

	/**
	 * Update Player about the {@link Game} status.
	 * @throws RemoteException
	 */
	public void gameUpdate() throws RemoteException;
}
