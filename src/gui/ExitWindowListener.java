package gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import client.Client;
import debug.Debug;

/**
 * Class representing a window listener on the gui.
 * @author computerjunky28
 *
 */
public class ExitWindowListener implements WindowListener {
	private final Client client;
	private final static boolean VERBOSE = Debug.VERBOSE;
	
	public ExitWindowListener(Client client){
		this.client = client;
	}
	
	
	@Override
	public void windowOpened(WindowEvent paramWindowEvent) {
		
	}


	/**
	 * Send a "bye" message to the server while closing the window.
	 */
	@Override
	public void windowClosing(WindowEvent paramWindowEvent) {
		if(VERBOSE){
		System.out.println("sending bye");
		}
		if(client != null && !client.getSocket().isClosed()){
		client.sendMessageToServer("bye");
		System.exit(0);
		}
	}


	@Override
	public void windowClosed(WindowEvent paramWindowEvent) {	
	}

	@Override
	public void windowIconified(WindowEvent paramWindowEvent) {	
	}

	@Override
	public void windowDeiconified(WindowEvent paramWindowEvent) {	
	}

	@Override
	public void windowActivated(WindowEvent paramWindowEvent) {
	}

	@Override
	public void windowDeactivated(WindowEvent paramWindowEvent) {
	}

}
