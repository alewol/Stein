package client;

import java.awt.Component;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import server.Server;

import base.Player;
import base.Srv;
import org.eclipse.wb.swt.SWTResourceManager;

public class Client extends Shell {

	private Registry registry;
	private Composite composite;
	private Text userName;
	private Label lblWelcomeToStein;
	private Text serverName;
	private static Display display;
	private Label lblStatus;
	private Label status;
	private static List list;
	private Composite composite_2;
	private Button btnPlay;
	private Button btnQuit;
	private Label lblChooseAPlayer;
	private String selected;
	private Srv server;
	private Player player;
	private Composite composite_1;
	private Label lblOutcome;
	private Button btnCancel;
	private Button btnRock;
	private Button btnScissors;
	private Button btnPaper;
	private Label lblWins;
	private Label lblDraws;
	private Label lblDefeats;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Client shell = new Client(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 * @throws RemoteException 
	 * @throws NotBoundException 
	 * @throws MalformedURLException 
	 */
	public Client(Display disp) throws RemoteException, MalformedURLException, NotBoundException {
		super(disp, SWT.SHELL_TRIM);
		display = disp;
		server = (Srv)Naming.lookup("SteinEngine");
		
		System.out.println("Started");
		
		
		list = new List(this, SWT.BORDER);
		list.setEnabled(true);
		list.setBounds(250, 7, 174, 245);
		createContents();

		status = new Label(this, SWT.NONE);
		status.setBounds(54, 237, 190, 15);
		status.setText("Ready");

		lblStatus = new Label(this, SWT.NONE);
		lblStatus.setBounds(10, 237, 38, 15);
		lblStatus.setText("Status: ");
		
		LoadWelcomePane();
		//LoadNewGamePane();
		//LoadGamePane();
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(450, 300);

	}

	private void LoadWelcomePane() {
		composite = new Composite(this, SWT.NONE);
		composite.setBounds(9, 10, 235, 221);

		sayWelcome(composite);

		userName = new Text(composite, SWT.BORDER);
		userName.setBounds(124, 122, 110, 21);
		userName.setText("per");

		serverName = new Text(composite, SWT.BORDER);
		serverName.setBounds(124, 95, 110, 21);
		serverName.setText("localhost");

		Label lblName = new Label(composite, SWT.NONE);
		lblName.setBounds(10, 125, 118, 15);
		lblName.setText("Your name");

		Label lblServer = new Label(composite, SWT.NONE);
		lblServer.setBounds(10, 98, 118, 15);
		lblServer.setText("Server");

		Button btnJoinGame = new Button(composite, SWT.NONE);
		btnJoinGame.setBounds(159, 149, 75, 25);
		btnJoinGame.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				String name = userName.getText();
				status.setText("Connecting");
				try {
					// Create a new Player
					player = new Gamer(name, server);
					
					// Start Thread to update player list
					updateList(player, list).start();
					
					// Dispose the register view
					composite.dispose();
					
					// Load the game view
					LoadNewGamePane();
					status.setText("Game on!"); 
				} 
				catch (Exception e) {
					errorPopup("Serious error! Contact the author of this lousy game!");
					display.dispose();
					e.printStackTrace();
				}
			}

			
		});
		btnJoinGame.setText("Join Game");
	}
	
	public void LoadNewGamePane()
	{	
		composite_2 = new Composite(this, SWT.NONE);
		composite_2.setBounds(9, 10, 235, 221);

		btnPlay = new Button(composite_2, SWT.NONE);
		btnPlay.setToolTipText("You have to choose a player!");
		btnPlay.setEnabled(false);
		btnPlay.setBounds(10, 196, 139, 25);
		btnPlay.setText("Can't play yet!");

		btnQuit = new Button(composite_2, SWT.NONE);
		btnQuit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				quit();
			}
		});
		btnQuit.setBounds(155, 196, 75, 25);
		btnQuit.setText("Quit");

		sayWelcome(composite_2);

		lblChooseAPlayer = new Label(composite_2, SWT.NONE | SWT.WRAP);
		lblChooseAPlayer.setAlignment(SWT.CENTER);
		lblChooseAPlayer.setFont(SWTResourceManager.getFont("Andy", 16, SWT.NORMAL));
		lblChooseAPlayer.setBounds(10, 98, 215, 50);
		lblChooseAPlayer.setText("Choose a player from the list!");


		// Choose a player from the list of players
		list.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				if (list.getSelection().length != 0)
					selected = list.getSelection()[0];
				else
					return;

				// Enable gameplay, when other user is selected.
				try {
					if (selected.equals(player.name())) {
						status.setText("Schizophrenia?");
						btnPlay.setEnabled(false);
						btnPlay.setText("Can't play yet!");
					} else {
						status.setText("Selected "+selected);
						btnPlay.setText("Play with "+selected);
						btnPlay.setEnabled(true);
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});

		// Start a game with the selected player
		btnPlay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					player.newGame(selected);
					LoadGamePane();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	/*
	 * Prepare the game board
	 */
	public void LoadGamePane() throws RemoteException {
		composite_2.dispose();
		
		if (player.agressive())
			status.setText("You challenged " + player.opponent());
		else
			status.setText("You got challenged by " + player.opponent());

		composite_1 = new Composite(this, SWT.NONE);
		composite_1.setBounds(10, 10, 230, 221);

		lblOutcome = new Label(composite_1, SWT.NONE);
		lblOutcome.setAlignment(SWT.CENTER);
		lblOutcome.setFont(SWTResourceManager.getFont("Cooper Black", 27, SWT.NORMAL));
		lblOutcome.setBounds(0, 84, 230, 90);

		btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				composite_1.dispose();
				try {
					player.withdraw();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				LoadNewGamePane();
			}
		});
		btnCancel.setBounds(155, 196, 75, 25);
		btnCancel.setText("Cancel");

		btnRock = new Button(composite_1, SWT.NONE);
		btnRock.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				gameButtonsSwitch(false);
				try {
					player.move("Rock");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRock.setBounds(0, 196, 75, 25);
		btnRock.setText("Rock");

		btnScissors = new Button(composite_1, SWT.NONE);
		btnScissors.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				gameButtonsSwitch(false);
				try {
					player.move("Scissors");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnScissors.setBounds(0, 0, 75, 25);
		btnScissors.setText("Scissors");

		btnPaper = new Button(composite_1, SWT.NONE);
		btnPaper.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				gameButtonsSwitch(false);
				try {
					player.move("Paper");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnPaper.setBounds(155, 0, 75, 25);
		btnPaper.setText("Paper");
		
		lblWins = new Label(composite_1, SWT.NONE);
		lblWins.setAlignment(SWT.CENTER);
		lblWins.setBounds(1, 58, 75, 15);
		lblWins.setText("0 WINS");
		
		lblDraws = new Label(composite_1, SWT.NONE);
		lblDraws.setAlignment(SWT.CENTER);
		lblDraws.setText("0 DRAWS");
		lblDraws.setBounds(77, 58, 75, 15);
		
		lblDefeats = new Label(composite_1, SWT.NONE);
		lblDefeats.setAlignment(SWT.CENTER);
		lblDefeats.setText("0 DEFEATS");
		lblDefeats.setBounds(153, 58, 75, 15);
	}
	
	/*
	 * Load the welcome label
	 * @param composite
	 */
	public void sayWelcome(Composite composite)
	{
		lblWelcomeToStein = new Label(composite, SWT.NONE);
		lblWelcomeToStein.setAlignment(SWT.CENTER);
		lblWelcomeToStein.setFont(SWTResourceManager.getFont("Andy", 22, SWT.NORMAL));
		lblWelcomeToStein.setBounds(21, 41, 193, 33);
		lblWelcomeToStein.setText("Welcome to Stein!");
	}
	
	/*
	 * Disable all buttons from game pane
	 */
	public void gameButtonsSwitch(boolean on){
		btnRock.setEnabled(on);
		btnPaper.setEnabled(on);
		btnScissors.setEnabled(on);
		btnCancel.setEnabled(on);
	}
	
	/*
	 * Display error message at crash.
	 */
	private void errorPopup(String message)
	{
		MessageBox b = new MessageBox(this, SWT.ICON_WARNING | SWT.OK);
		b.setText(Server.SERVICE_NAME);
		b.setMessage(message);
		b.open();
	}
	

	/*
	 * Leave the game, close the window
	 */
	public void quit()
	{
		// TODO leave server
//		try {
//			if (client != null)
//				client.Leave();
//		} 
//		catch (Exception e) { e.printStackTrace(); }
		this.close();
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private Thread updateList(final Player p, final List l) {
		return new Thread() {
			public void run() {
				while(true) {
					try {
						Thread.sleep(100);
						display.asyncExec(new Runnable() {
							public void run() {
								try {
									// Check if the user list has changed, update
									if (p.isChanged()) {
										l.removeAll();
										Object[] gamers = server.getPlayers();
										for(int i=0; i<gamers.length; i++){
											Player g = (Player)gamers[i];
											l.add(g.name());
										}
									}
									
									// Check if player is challenged, update
									if (p.isChallenged()) {
										LoadGamePane();
									}
									
									// Check if game round is ended, update
									if (p.roundFinished()) {
										gameButtonsSwitch(true);
										lblWins.setText(p.wins() + " wins");
										lblDraws.setText(p.draws() + " draws");
										lblDefeats.setText(p.defeats() + " defeats");
									}
									
									if (p.gotLonely()) {
										errorPopup(p.opponent() + " left the building.");
										composite_1.dispose();
										try {
											player.withdraw();
										} catch (RemoteException e1) {
											e1.printStackTrace();
										}
										LoadNewGamePane();
									}
										
								} catch (RemoteException e) {
									e.printStackTrace();
								}
							}
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
}
