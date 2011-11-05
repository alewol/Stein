package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Hashtable;

import base.Game;
import base.Player;
import base.Srv;

/**
 * Server Object.
 * 
 * <P>The Stein Server keeps lists of games and players and allows a
 * {@link Player} to register, see {@link #register(Player)}. The
 * registered players can then    
 *  
 * @author runar & alex
 * @version 1.0
 */
public class Server extends UnicastRemoteObject implements Srv {

	private static final long serialVersionUID = 4333568576579343572L;
	
	/**
	 * players - list of registered players
	 */
	public Hashtable<String,Player> players;
	/**
	 * games - list of initiated games 
	 */
	public Hashtable<String,Play> games;
	
	
	/**
	 * Constructor for the Server class. Initiates players and games with
	 * no elements.
	 * 
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	protected Server() throws RemoteException, MalformedURLException {
		System.out.println("Started " + SERVICE_NAME);
		players = new Hashtable<String,Player>();
		games = new Hashtable<String,Play>();
	}

	/**
	 * Create a Server instance, bind to SERVICE_NAME and start the Action thread.
	 *   
	 * @param args	No arguments expected or handled.
	 */
	public static void main(String[] args) {
		try {
			Server s = new Server();
			LocateRegistry.createRegistry(1099);
			Naming.rebind(SERVICE_NAME, s);
			new Action(s).run();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see base.Srv#getPlayers()
	 */
	@Override
	public Object[] getPlayers(){
		Object[] u = players.values().toArray();
		return u;
	}
	
	/* (non-Javadoc)
	 * @see base.Srv#register(base.Player)
	 */
	@Override
	public void register(Player p) throws RemoteException {
		players.put(p.name(), p);
		System.out.println(p.name() + " joined.");
	}

	/* (non-Javadoc)
	 * @see base.Srv#createGame(java.lang.String, java.lang.String)
	 */
	@Override
	public Game loadGame(String opponent, String name) throws RemoteException {
		String title = title(opponent, name);
		Play game = games.get(title);
		if (game == null) {
			game = new Play(players.get(opponent), players.get(name));
			games.put(title, game);
			System.out.println("New game: " +title);
		}
		return game;
	}
	
	@Override
	public void endGame(Game game) throws RemoteException {
		games.remove(game.title());
	}
	
	/**
	 * Get the title for the game between two players.
	 * @param o		opponent's name
	 * @param n		player's name
	 * @return		the game title
	 */
	private String title(String o, String n) {
		String[] players = {o, n};
		Arrays.sort(players);
		return players[0] + players[1];
	}

}
