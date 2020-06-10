package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import client.Client;
import debug.Debug;

/**
 * The MainWindow of the GUI that is a subclass of JFrame. It is the top-level
 * container for the the connect page, the document page, the welcome page, and
 * the openDocument page
 * 
 * @author computerjunky28
 * 
 */
@SuppressWarnings("unused")
public class MainWindow extends JFrame {

	private static final boolean DEBUG = Debug.DEBUG;
	private static final long serialVersionUID = 1L;
	private WelcomeView welcomeView;
	private DocumentView documentView;
	private ConnectView connectView;
	private OpenDocumentDialog openDocumentDialog;
	private ArrayList<String> documentNames;
	private Client client;
	private String username;

	/**
	 * Creates a mainWindow, with the first screen being the connectView
	 */
	public MainWindow() {
		setTitle("Collaborative Text Editor");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(250, 250));
		setBackground(Color.decode("#262F3E"));
		connectView = new ConnectView(this);
		add(connectView, BorderLayout.CENTER);
		pack();
	}

	// mainWindow should get client from connectView after it connects

	/**
	 * Switched from the connectView to the WelcomeView
	 */
	public void switchToWelcomeView() {
		setVisible(false);
		getContentPane().remove(connectView);

		setPreferredSize(new Dimension(500, 250));
		setMinimumSize(new Dimension(500, 250));
		setMaximumSize(new Dimension(550, 250));
		welcomeView = new WelcomeView(this, client);
		add(welcomeView, BorderLayout.CENTER);

		if (DEBUG) {
			System.out.println("Hi. switching to welcome view");
		}
		setVisible(true);
	}

	/**
	 * Shows the dialog that prompts the user for an input name and sends the
	 * input to the server.
	 */
	public void openUsernameDialog() {
		String username = JOptionPane.showInputDialog("Enter a username", "");
		if(username==null){
			JOptionPane.showMessageDialog(null, "Please enter a valid username", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		else{
		client.sendMessageToServer("name " + username);
		}
	}
	
	public void setUsername(String name){
		this.username = name;
	}
	
	public String getUsername(){
		return username;
	}

	/**
	 * Switch to DocumentView from WelcomeView
	 */
	public void switchToDocumentView(String documentName, String documentText) {
		setVisible(false);
		removeAllViews();
		setPreferredSize(new Dimension(600, 500));
		setMinimumSize(new Dimension(600, 500));
		setMaximumSize(new Dimension(600, 500));
		documentView = new DocumentView(this, documentName, documentText);
		this.addWindowListener(new ExitWindowListener(client));
		getContentPane().add(documentView, BorderLayout.CENTER);
		getContentPane().validate();
		getContentPane().repaint();
		setVisible(true);
		if (DEBUG) {
			System.out.println("switching to document view");
		}
	}

	/**
	 * Removes all views from the contentPane
	 */
	private void removeAllViews() {
		if (welcomeView != null) {
			getContentPane().remove(welcomeView);
		}
		if (connectView != null) {
			getContentPane().remove(connectView);
		}
		if (documentView != null) {
			getContentPane().remove(documentView);
		}
	}

	/**
	 * Opens an openDocumentDialog that displays existing documents on the
	 * server.
	 * 
	 * @param documentNames
	 *            list of names of the documents
	 */
	public void displayOpenDocuments(ArrayList<String> documentNames) {
		if (DEBUG) {
			System.out.println("switching to open existing document view");
		}
		openDocumentDialog = new OpenDocumentDialog(documentNames, client);
	}

	/**
	 * Sends a command to the documentView to update the text of the document
	 * with the new document text.
	 * 
	 * @param documentText
	 *            text of the document
	 * @param editPosition
	 *            the position of the edit
	 * @param editLength
	 *            the length of the text inserted or removed
	 * @param version
	 *            the version of the document the edit was made on
	 */
	public void updateDocument(String documentText, int editPosition,
			int editLength, String username, int version) {
		if (DEBUG) {
			System.out.println("updating document");
		}
		if (documentView != null) {
			documentView.updateDocument(documentText, editPosition, editLength,
					username, version);
			getContentPane().repaint();
		}

	}

	/**
	 * Creates and shows a message dialog
	 * 
	 * @param error
	 *            the error message
	 */
	public void openVersionErrorView(String error) {
		int n = JOptionPane.showConfirmDialog(null, error, "Error",
				JOptionPane.ERROR_MESSAGE);
		client.sendMessageToServer("open " + client.getDocumentName());
		if (DEBUG) {
			System.out.println("sent message");
		}
	}

	/**
	 * Creates and shows a message dialog with the specified error message.
	 * 
	 * @param error
	 */
	public void openErrorView(String error) {
		JOptionPane.showMessageDialog(null, error, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Sets the client of the MainWindow
	 * 
	 * @param client
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * Returns the client of this frame
	 */
	public Client getClient() {
		return client;
	}

}
