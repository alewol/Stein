/**
 * 
 */
package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import base.Game;
import base.Player;
import base.Srv;

/**
 * @author 490193
 *
 */
public class Gamer extends UnicastRemoteObject implements Player {

	private static final long serialVersionUID = 489787298622517492L;

	private String name;
	private Srv server;
	private boolean listHasChanged;
	private Game game;
	private boolean challengeReceived = false;

	private String challenger;
	
	protected Gamer(String _name, Srv srv) throws RemoteException {
		name = _name;
		server = srv;
		server.register(this);
		listHasChanged = true;
		System.out.println("regd!");
	}

	/* (non-Javadoc)
	 * @see base.Player#Hello()
	 */
	@Override
	public String Hello() throws RemoteException {
		return "Hello " + name;
	}

	@Override
	public String name() throws RemoteException {
		return name;
	}

	@Override
	public void announce() throws RemoteException {
		listHasChanged = true;
	}

	@Override
	public boolean isChanged() throws RemoteException {
		if (!listHasChanged) return false;
		listHasChanged = false;
		return true;
	}

	@Override
	public void newGame(String opponent) throws RemoteException {
		game = server.loadGame(opponent, name);
		System.out.println(game.title());
	}

	@Override
	public boolean agressive() throws RemoteException {
		return game.amIstupid(name);
	}

	@Override
	public String opponent() throws RemoteException {
		if (game != null)
			return game.opponent(name);
		return null;
	}
	
	@Override
	public void move(String move) throws RemoteException {
		game.move(name, move);
	}

	@Override
	public void challenge(String attacker) throws RemoteException {
		challengeReceived = true;
		challenger = attacker;
	}

	@Override
	public boolean isChallenged() throws RemoteException {
		if (!challengeReceived) return false;
		game = server.loadGame(name, challenger);
		challengeReceived = false;
		return true;
	}
	
	@Override
	public boolean roundFinished() throws RemoteException {
		if (game != null)
			return game.finished();
		return false;
	}

	@Override
	public String wins() throws RemoteException {
		return game.wins(name);
	}

	@Override
	public String draws() throws RemoteException {
		return game.draws(name);
	}

	@Override
	public String defeats() throws RemoteException {
		return game.defeats(name);
	}

	@Override
	public void withdraw() throws RemoteException {
		if (game != null) game.withdraw();
		game = null;
	}

	@Override
	public boolean gotLonely() throws RemoteException {
		if (game != null) {
			if (game.canceled()) {
				game.endTimes();
				game = null;
				return true;
			}
		}
		return false;
	}

}
