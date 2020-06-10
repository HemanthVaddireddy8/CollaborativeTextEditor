package gui;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import client.Client;


/**
 * OpenDocumentView represents the view that clients see when opening an
 * existing document
 */
public class OpenDocumentDialog extends JOptionPane {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new OpenDocumentDialog as a JOptionPane that shows the client
	 * what documents are on the server. When clients make a choice, that is
	 * sent as an "open" message to the server.
	 * 
	 * @param documentNames
	 *            list of names of the documents
	 * @param client the client that is making the open document choice
	 */
	public OpenDocumentDialog(ArrayList<String> documentNames, Client client) {

		// if names is null, an error message is shown
		//we don't actually use this code...
		if (documentNames == null) {
			JOptionPane.showMessageDialog(null,
					"There is no document on the server yet", "Error",
					JOptionPane.ERROR_MESSAGE);
		} 
		else {
			//otherwise, the gui will switch to a JOption in which the client could select a document to open
			Object[] documentsOnServer = new Object[documentNames.size()];
			for (int i = 0; i < documentNames.size(); i++) {
				documentsOnServer[i] = documentNames.get(i);
			}
			// s is the name of the document that the client wants to open
			String s = (String) JOptionPane.showInputDialog(null,
					"Choose a document:\n", "Open a document dialog",
					JOptionPane.PLAIN_MESSAGE, icon, documentsOnServer,
					documentsOnServer[0]);

			// send the message to the server.
			if (s != null) {
				client.sendMessageToServer("open " + s);
			}
		}
	}
}
