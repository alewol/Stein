package base;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Player extends Remote {

	public String Hello() throws RemoteException;
	public String name() throws RemoteException;
	public void announce() throws RemoteException;
	public void newGame(String opponent) throws RemoteException;
	public boolean agressive() throws RemoteException;
	public String opponent() throws RemoteException;
	public void move(String move) throws RemoteException;
	public void challenge(String attacker) throws RemoteException;
	public String wins() throws RemoteException;
	public String draws() throws RemoteException;
	public String defeats() throws RemoteException;
	
	public void withdraw() throws RemoteException;
	public void endGame() throws RemoteException;

	public void gameUpdate() throws RemoteException;
}
