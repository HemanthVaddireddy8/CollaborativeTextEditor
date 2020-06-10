package client;
import gui.MainWindow;

import javax.swing.SwingUtilities;

/**
 * The entry point of the Client side of the Collaborative Editor program.
 *
 */
public class Client_main {
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	MainWindow main = new MainWindow();
				main.setVisible(true);
		    }
		});

	} 
}
