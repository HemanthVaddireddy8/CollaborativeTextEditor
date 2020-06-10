package gui;

import handlers.ConnectViewThread;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import client.Client;

@SuppressWarnings("all")
/**
 * Interface that prompts clients for the server address
 * and starts the connection
 * rep invariant: host and port can not be null. 
 */
public class ConnectView extends JPanel implements ActionListener {

	private final MainWindow frame;
	// can be null
	private final JLabel serverAddressLabel;
	private final JLabel hostLabel;
	private final JTextField host;
	private final JLabel portLabel;
	private final JTextField port;
	private final JButton connectButton;
	private final JLabel lblWelcome;
	private Client client;
	private boolean DEBUG;

	/**
	 * Creates the ConnectView frame which has 3 JLabels: serverAddress,
	 * portLabel and hostLabel; 2 JTextfields host and port for the user to
	 * enter the host and port information; 1 button "connect" to connect and
	 * switch to WelcomeView. The client of the ConnectView is the same client
	 * as the frame being passed in.
	 * 
	 * @param frame
	 *            the MainWindow that calls the ConnectView.
	 */
	public ConnectView(MainWindow frame) {
		this.frame = frame;
		serverAddressLabel = new JLabel("Enter the server address:");
		serverAddressLabel.setForeground(Color.WHITE);
		hostLabel = new JLabel("Host:");
		host = new JTextField();
		host.addActionListener(this);
		hostLabel.setForeground(Color.WHITE);
		portLabel = new JLabel("Port:");
		portLabel.setForeground(Color.WHITE);
		port = new JTextField();
		port.addActionListener(this);
		connectButton = new JButton("Connect");
		lblWelcome = new JLabel("Welcome To Real time Editor");
		connectButton.addActionListener(this);
		setBackground(Color.decode("#262F3E"));
		this.client = frame.getClient();

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);

		layout.setHorizontalGroup(layout
				.createParallelGroup()
				.addComponent(serverAddressLabel)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup()
												.addComponent(hostLabel)
												.addComponent(portLabel))
								.addGroup(
										layout.createParallelGroup()
												.addComponent(host, 100, 150,
														Short.MAX_VALUE)
												.addComponent(port, 100, 150,
														Short.MAX_VALUE)))
				.addComponent(connectButton));
		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addComponent(serverAddressLabel)
				.addGroup(
						layout.createParallelGroup()
								.addComponent(hostLabel)
								.addComponent(host, GroupLayout.PREFERRED_SIZE,
										25, GroupLayout.PREFERRED_SIZE))
				.addGroup(
						layout.createParallelGroup()
								.addComponent(portLabel)
								.addComponent(port, GroupLayout.PREFERRED_SIZE,
										25, GroupLayout.PREFERRED_SIZE))
				.addComponent(connectButton));

	}

	/**
	 * When enter or connect button is pressed, check for valid inputs. If
	 * inputs are valid, ConnectViewThread is started to handle the switching to
	 * WindowView. If invalid input is detected, a error window "invalid input"
	 * pops up and the host and port fields are cleared.
	 * 
	 * @param e
	 *            the event that connect button or enter is pressed
	 */
	public void actionPerformed(ActionEvent e) {
		String hostInput = host.getText().trim();
		String portInput = port.getText().trim();
		String portRegex = "\\d\\d?\\d?\\d?\\d?";
		try {
			hostInput = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (hostInput.length() != 0 && portInput.matches(portRegex)) {
			try {
				client = new Client(Integer.parseInt(portInput), hostInput,
						frame);
				frame.setClient(client);
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "Invalid arguments",
						"Error", JOptionPane.ERROR_MESSAGE);

			}
			client.setMainWindow(frame);
			if (DEBUG) {
				System.out
						.println("I am here, the client setMainWindow. ConnectView");
			}
			// start a new ConnectViewThread thread that takes care of switching
			// the connect view to the window view
			// and also start the client so that client begins to listens for
			// server updates.
			ConnectViewThread thread = new ConnectViewThread(this);
			// start the thread
			thread.start();

		} else {
			JOptionPane.showMessageDialog(null, "Invalid arguments", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Returns the client field
	 * 
	 * @return client the private field of ConnectView class, a single client
	 */
	public Client getClient() {
		return client;
	}
}
