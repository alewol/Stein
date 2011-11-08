/**
 * 
 */
package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

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
	private Game game;
	private Client shell;
	private Display display;
	private String challenger;
	
	protected Gamer(String _name, Srv srv, Shell _shell, Display _display) throws RemoteException {
		name = _name;
		server = srv;
		shell = (Client) _shell;
		display = _display;
		server.register(this);
		System.out.println("Registered at Server.");
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
		display.asyncExec(new Runnable() {
			public void run() {
				try {
					shell.updateList();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
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
		challenger = attacker;
		display.asyncExec(new Runnable() {
			public void run() {
				try {
					game = server.loadGame(name, challenger);
					shell.LoadGamePane();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void gameUpdate() throws RemoteException {
		display.asyncExec(new Runnable() {
			public void run() {
				try {
					shell.gameButtonsSwitch(true);
					shell.statusUpdate();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
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
		game.withdraw(name);
		server.remove(game);
	}
	
	@Override
	public void endGame() throws RemoteException {
		display.asyncExec(new Runnable() {
			public void run() {
				try {
					shell.opponentLeft();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
