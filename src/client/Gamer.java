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
 * Gamer Object.
 * 
 * The {@link Player} implementation for the Stein {@link Client}.
 * 
 * @author alex & runar
 * @version 1.0
 */
public class Gamer extends UnicastRemoteObject implements Player {

	private static final long serialVersionUID = 489787298622517492L;

	private String name;
	private Srv server;
	private Game game;
	private Client shell;
	private Display display;
	private String challenger;
	
	/**
	 * <P>Create a new Gamer instance by the given name, on the given {@link Server},
	 * using the given Shell on the given Display.
	 * 
	 * @param _name				The name of the Gamer ({@Player}) created.
	 * @param srv				The Stein {@link Server} for this Gamer
	 * @param _shell			The GUI this Gamer is using.
	 * @param _display			The display the given Shell is using. 
	 * @throws RemoteException
	 */
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

	/* (non-Javadoc)
	 * @see base.Player#name()
	 */
	@Override
	public String name() throws RemoteException {
		return name;
	}

	/* (non-Javadoc)
	 * @see base.Player#announce()
	 */
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

	/* (non-Javadoc)
	 * @see base.Player#newGame(java.lang.String)
	 */
	@Override
	public void newGame(String opponent) throws RemoteException {
		game = server.loadGame(opponent, name);
		System.out.println(game.title());
	}

	/* (non-Javadoc)
	 * @see base.Player#agressive()
	 */
	@Override
	public boolean agressive() throws RemoteException {
		return game.amIstupid(name);
	}

	/* (non-Javadoc)
	 * @see base.Player#opponent()
	 */
	@Override
	public String opponent() throws RemoteException {
		if (game != null)
			return game.opponent(name);
		return null;
	}
	
	/* (non-Javadoc)
	 * @see base.Player#move(java.lang.String)
	 */
	@Override
	public void move(String move) throws RemoteException {
		game.move(name, move);
	}

	/* (non-Javadoc)
	 * @see base.Player#challenge(java.lang.String)
	 */
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
	
	/* (non-Javadoc)
	 * @see base.Player#gameUpdate()
	 */
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

	/* (non-Javadoc)
	 * @see base.Player#wins()
	 */
	@Override
	public int wins() throws RemoteException {
		return game.wins(name);
	}

	/* (non-Javadoc)
	 * @see base.Player#draws()
	 */
	@Override
	public int draws() throws RemoteException {
		return game.draws(name);
	}

	/* (non-Javadoc)
	 * @see base.Player#defeats()
	 */
	@Override
	public int defeats() throws RemoteException {
		return game.defeats(name);
	}

	/* (non-Javadoc)
	 * @see base.Player#withdraw()
	 */
	@Override
	public void withdraw() throws RemoteException {
		game.withdraw(name);
	}
	
	/* (non-Javadoc)
	 * @see base.Player#endGame()
	 */
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
		server.remove(game);
	}

}
