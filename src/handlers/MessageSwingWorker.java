package handlers;

import javax.swing.SwingWorker;

import client.Client;


/**
 * MessageSwingWorker represents a SwingWorker class that sends messages to the server
 * in a background thread. 
 * @author computerjunky28
 *
 */
@SuppressWarnings("unused")
public class MessageSwingWorker extends SwingWorker<Void, Void>{
	private Client client;
	private String message;
	private boolean sent;
	
	/**
	 * Constructs a new instance of a MessageSwingWorker with the client and the message
	 * @param client
	 * @param message
	 * @param sent - boolean representing the client has sent this message
	 */
	public MessageSwingWorker(Client client, String message, boolean sent){
		this.client = client;
		this.message = message;
		this.sent = sent;
	}
	
	/**
	 * Connects with the server in the background.
	 */
	protected Void doInBackground() {
		client.sendMessageToServer(message);
		done();
		return null;
	}
	/**
	 * Repaints the GUI after connecting with the server and the actions
	 * have been completed.
	 */
	@Override
	protected void done() {
		client.getMainWindow().repaint();
		sent = false;
		
	}
	

}
