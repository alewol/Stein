package base;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Game extends Remote {

	/**
	 * Return the owner's name
	 * @return	owner's name
	 * @throws RemoteException
	 */
	public String owner() throws RemoteException;
		
	/**
	 * Returns the name of the game
	 * @return	game name
	 * @throws RemoteException
	 */
	public String title() throws RemoteException;
		
	/**
	 * Return the opponent's name
	 * @param name	
	 * @return	opponent's name
	 * @throws RemoteException
	 */
	public String opponent(String name) throws RemoteException;
		
	/**
	 * 
	 * Check's for who made the move, and who wins the current move.
	 * Then update the status on both players
	 * 
	 * @param name
	 * @param move
	 * @throws RemoteException
	 */
	public void move(String name, String move) throws RemoteException;
	
	/**
	 * Check's if the player is the owner of the game
	 * @param name
	 * @return
	 * @throws RemoteException
	 */
	public boolean amIstupid(String name) throws RemoteException;
	
	/**
	 * Returns the number of wins
	 * @param name	player's name
	 * @return	count of wins
	 * @throws RemoteException
	 */
	public int wins(String name) throws RemoteException;
	
	/**
	 * Returns the number of draws
	 * @param name	player's name
	 * @return	count of draws
	 * @throws RemoteException
	 */
	public int draws(String name) throws RemoteException;
	
	/**
	 * Returns the number of defeats
	 * @param name	player's name
	 * @return	count of defeats
	 * @throws RemoteException
	 */
	public int defeats(String name) throws RemoteException;
	
	/**
	 * Closing the game for the actual player
	 * @param name	player's name
	 * @throws RemoteException
	 */
	public void withdraw(String name) throws RemoteException;

}