package server;

import java.rmi.RemoteException;

import base.Player;

/**
 * Watches the given Server for game action.
 * 
 * @author 490193
 * 
 */
public class Action extends Thread {
	
	private Server server;
	
	public Action(Server srv){
		System.out.println("Started Action.");
		server = srv;
		this.setDaemon(true);
	}
	
	public void run(){
		try {
			int userCount = 0;
			while(true){
				Thread.sleep(100);
				Object[] list = server.getPlayers();
				if(userCount != list.length){
					userCount = list.length;
					
					if(list.length != 0){
						for(int i=0; i<list.length; i++){
							Player p = (Player)list[i];
							p.announce();
						}
					}
				}
			}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}

}
