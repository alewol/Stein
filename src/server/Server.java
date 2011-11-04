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

public class Server extends UnicastRemoteObject implements Srv {

	public Hashtable<String,Player> users;
	public Hashtable<String,Play> games;
	
	protected Server() throws RemoteException, MalformedURLException {

		System.out.println("Started " + SERVICE_NAME);
		
		users = new Hashtable<String,Player>();
		games = new Hashtable<String,Play>();
	}

	/**
	 * @param args
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

	/**
	 * (non-Javadoc)
	 * @see base.Srv#getUsers()
	 */
	@Override
	public Object[] getPlayers(){
		Object[] u = users.values().toArray();
		return u;
	}
	
	/*
	 * (non-Javadoc)
	 * @see base.Srv#register(java.lang.String)
	 */
	@Override
	public void register(Player p) throws RemoteException {
		users.put(p.name(), p);
		System.out.println(p.name() + " joined.");
	}

	@Override
	public Game createGame(String opponent, String name) throws RemoteException {
		Play game = new Play(users.get(opponent), users.get(name));
		games.put(game.title(), game);
		System.out.println(game.title());
		return game;
	}

	@Override
	public Game getGame(String opponent, String name) throws RemoteException {
		String[] players = {opponent, name};
		Arrays.sort(players);
		String title = players[0] + players[1];
		System.out.println(title);
		Play game = games.get(title);
		return game;
	}

}
