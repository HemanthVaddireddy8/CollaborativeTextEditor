package gui;

import handlers.WelcomeViewThread;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.Client;


/**
 * WelcomeView represents the welcome screen presented to clients after
 * connecting to the server
 */
@SuppressWarnings("all")
public class WelcomeView extends JPanel implements ActionListener {
	private final MainWindow frame;
	private JLabel welcomeLabel;
	private JLabel createNewLabel;
	private JTextField documentName;
	private JButton createNewButton, openDocumentButton;
	private Client client;

	/**
	 * Creates a new WelcomeView with the top-level frame and the Client
	 * 
	 * @param frame
	 * @param client
	 */
	public WelcomeView(MainWindow frame, Client client) {
		this.frame = frame;
		this.client = client;
		welcomeLabel = new JLabel("Welcome to the Collaborative Text Editor.");
		System.out.println("I am currently making a welcome view.");
		createNewLabel = new JLabel("Enter a new document name:");
		documentName = new JTextField();
		documentName.addActionListener(this);
		createNewButton = new JButton("Create");
		createNewButton.addActionListener(this);
		
		openDocumentButton = new JButton("Open Existing Document");
		openDocumentButton.addActionListener(this);

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(welcomeLabel).addComponent(createNewLabel)
				.addComponent(documentName, 100, 150, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup()
						.addComponent(createNewButton).addComponent(openDocumentButton))
				);
		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addComponent(welcomeLabel)
				.addComponent(createNewLabel)
				.addComponent(documentName, GroupLayout.PREFERRED_SIZE, 25,
						GroupLayout.PREFERRED_SIZE)
				.addGroup(layout.createParallelGroup()
						.addComponent(createNewButton).addComponent(openDocumentButton)));

	}

	/**
	 * Listener for the documentName field. Checks to make sure the documentName
	 * is valid and then starts a new WelcomeView thread that sends a "new"
	 * message to the server
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == createNewButton || e.getSource() == documentName) {
			String newDocumentName = documentName.getText().trim();
			if (newDocumentName.matches("[\\w\\d]+")) {
				WelcomeViewThread thread = new WelcomeViewThread(client, "new "	+ newDocumentName);
				thread.start();
			} else {
				JOptionPane.showMessageDialog(null,
						"Document name cannot be empty and must only contain letters and digits.",
						"Invalid document name", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (e.getSource() == openDocumentButton){
			client.sendMessageToServer("look");
		}
	}

	/**
	 * Returns the client of the WelcomeView
	 * 
	 * @return client the private field client
	 */
	public Client getClient() {
		return client;

	}
}
