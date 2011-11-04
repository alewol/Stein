/**
 * 
 */
package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import base.Game;
import base.Player;

/**
 * @author 490193
 *
 */
public class Play extends UnicastRemoteObject implements Game {

	private static final long serialVersionUID = -4068485307729874645L;
	private Player opponent;
	private int opponentWins;
	private Player owner;
	private int ownerWins;
	private int draws;
	private String title;
	private String owM = null;
	private String opM = null;
	private boolean ongoingRound;
	private boolean gameOver;

	protected Play(Player _opponent, Player _owner) throws RemoteException {
		opponent = _opponent;
		owner = _owner;
		setTitle();
		opponent.challenge(owner.name());
		ongoingRound=true;
	}

	@Override
	public String owner() throws RemoteException {
		return owner.name();
	}

	@Override
	public String opponent(String name) throws RemoteException {
		if (owner.name().equals(name))
			return opponent.name();
		return owner.name();
	}
	
	public void setTitle() throws RemoteException {
		String[] players = {opponent.name(), owner.name()};
		Arrays.sort(players);
		title = players[0] + players[1];
		System.out.println("Game created: " +title);
	}

	@Override
	public String title() throws RemoteException {
		return title;
	}

	@Override
	public void move(String name, String move) throws RemoteException {
		ongoingRound=true;
		if (owner.name().equals(name))
			owM = move;
		if (opponent.name().equals(name))
			opM = move;

		// If both moves made, calculate result
		if (owM != null && opM != null) { // Owner might win!
			if ((owM.equals("Rock") && opM.equals("Scissors")) ||
					(owM.equals("Scissors") && opM.equals("Paper")) ||
					(owM.equals("Paper") && opM.equals("Rock")))
				ownerWins++;
			else // But if he didn't and the players made the same move, it's a draw
				if (owM.equals(opM))
					draws++;
				else // Or, the opponent win
					opponentWins++;
			
			// Round finished
			ongoingRound=false;
			owM = opM = null;
		}
	}

	@Override
	public boolean amIstupid(String name) throws RemoteException {
		System.out.println(name);
		return owner.name().equals(name);
	}

	@Override
	public boolean finished() throws RemoteException {
		return !ongoingRound;
	}

	@Override
	public String wins(String name) throws RemoteException {
		if (owner.name().equals(name))
			return ""+ownerWins;
		return ""+opponentWins;
	}

	@Override
	public String draws(String name) throws RemoteException {
		return ""+draws;
	}

	@Override
	public String defeats(String name) throws RemoteException {
		if (owner.name().equals(name))
			return ""+opponentWins;
		return ""+ownerWins;
	}

	@Override
	public void withdraw() throws RemoteException {
		gameOver=true;
	}
	
	@Override
	public boolean canceled() throws RemoteException {
		return gameOver;
	}

	@Override
	public void endTimes() {
		System.out.println(title + " terminated.");
		owner = opponent = null;
	}

}
