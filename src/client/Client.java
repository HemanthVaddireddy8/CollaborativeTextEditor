package client;

import gui.MainWindow;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import debug.Debug;

/**
 * Client class receives message from the server and send the message to be
 * processed by ClientActionListener.
 */
public class Client {

	private static final boolean DEBUG = Debug.DEBUG;
	private String nameOfDocument;
	private String textOfDocument;
	private int versionOfDocument;
	private String userName;
	private ClientActionListener actionListener;
	private Socket socket;
	private int port;
	private String host;
	private PrintWriter out;
	private MainWindow mainWindow;

	/**
	 * Constructor
	 * 
	 * @param port
	 *            the port client is connected to
	 * @param host
	 *            client is connected to
	 * @param main
	 *            the MainWindow frame that calls the client
	 */
	public Client(int port, String host, MainWindow main) {
		this.port = port;
		this.host = host;
		mainWindow = main;

	}

	/**
	 * Start with a welcomeView and starts an action listener that listens for
	 * server updates.
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		socket = new Socket(host, port);
		//mainWindow.switchToWelcomeView();
		mainWindow.openUsernameDialog();
		// also, start listening for the server socket.
		if (DEBUG){System.out.println("Client connected to the server. ");}
		actionListener = new ClientActionListener(this, socket);
		actionListener.run();
		out = new PrintWriter(socket.getOutputStream());
		

	}

	/**
	 * Set the filed mainWindow to be the frame
	 * 
	 * @param frame
	 *            a MainWindow
	 */
	public void setMainWindow(MainWindow frame) {
		this.mainWindow = frame;
	}

	/**
	 * Send the message to the server by doing out.println(message)
	 * 
	 * @param message
	 *            to send to the server
	 * 
	 */
	public void sendMessageToServer(String message) {
		if (DEBUG) {System.out.println("we got to Client Send message");}
		try {
			out = new PrintWriter(socket.getOutputStream());
			if (DEBUG) {System.out.println("socket is" + socket.getLocalPort());}
			out.write(message + "\n");
			out.flush();
		} catch (IOException e) {
			mainWindow.openErrorView(e.getMessage());
		}
	}
	
	/**
	 * Sets the userName of the Client to the specified name and 
	 * opens the welcome view for the client
	 * @param name
	 */
	public void setUsername(String name){
		System.out.println("setting username");
		userName = name;
		mainWindow.setUsername(name);
		mainWindow.switchToWelcomeView();
	}
	
	/**
	 * Returns the client's username
	 * @return userName
	 */
	public String getUsername(){
		return userName;
	}

	/**
	 * 
	 * @return the private field nameOfDocument
	 */
	public String getDocumentName() {
		return nameOfDocument;
	}

	/**
	 * @return the private field textOfDocument
	 */
	public String getText() {
		return textOfDocument;
	}
 
	/**
	 * 
	 * @return the current version
	 */
	public int getVersion(){
		 return versionOfDocument;
	}
	/**
	 * 
	 * @return socket, the private filed socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * 
	 * @return the private field mainWindow
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}

	//*********************Back-end processing methods:*****************************
	/**
	 * The mutator method that changes the nameOfDocument to be name. Called
	 * when the client receives new/open message from the server
	 * 
	 * @param name the string that is the new name of the document
	 */
	public void updateDocumentName(String name) {
		System.out.println("updating documentName");
		nameOfDocument = name;
	}

	/**
	 * The mutator method that changes the textOfDocument to be text. Called
	 * when the client receives new/open/change message from the server.
	 * @param text  the String that is the new text of the document.
	 */
	public void updateText(String text) {
		textOfDocument = text;
	}

	/**
	 * The mutator method that changes the version number
	 * @param newVersion the new version number of the document
	 */
	public void updateVersion(int newVersion) {
		versionOfDocument = newVersion;
	}

}

