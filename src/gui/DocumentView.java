package gui;

import handlers.Encoding;
import handlers.MessageSwingWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

import client.Client;
import debug.Debug;

/**
 * Class representing the interface of the editor
 */
public class DocumentView extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = Debug.DEBUG;
	private JFrame frame;
	private JMenuBar menu;
	private JMenu file, edit;
	private JMenuItem newfile, open, exit, copy, cut, paste; 
	private JLabel documentNameLabel;
	private String documentName, documentText;
	private JTextArea area;
	private JScrollPane scrollpane;
	private DefaultCaret caret;
	private TextDocumentListener documentListener;
	private final Client client;
	private final String username;
	private int currentVersion;
	private boolean sent = false; //used in cursor managing
	
	// Rep invariant:
	// documentText can be null

	/**
	 * Creates a new DocumentView; Used for debugging/testing purposes
	 */
	public DocumentView(MainWindow frame) {
		this.frame = frame;
		this.client = null;
		this.username = "";
		documentNameLabel = new JLabel("You are editing document: ");
		createLayout();
	}

	/**
	 * Creates a new DocumentView with the MainWindow, documentName, and the
	 * text of the document.
	 * 
	 * @param client
	 * @param documentName
	 */
	public DocumentView(MainWindow frame, String documentName, String text) {
		if (DEBUG) {
			System.out.println("Is making a document View.");
		}
		this.frame = frame;
		this.client = frame.getClient();
		this.documentName = documentName;
		this.username = frame.getUsername();
		documentText = Encoding.decode(text);
		documentNameLabel = new JLabel("<html><B>"+documentName+"</B></html>");
		createLayout();
	}

	/**
	 * Initializes components, defines the layout, and adds the listeners
	 */
	private void createLayout() {
		
		menu = new JMenuBar();
		file = new JMenu("File");
		edit = new JMenu("Edit");
		menu.add(file);
		menu.add(edit);

		newfile = new JMenuItem("New");
		newfile.addActionListener(new NewFileListener());
		file.add(newfile);
		
		copy = new JMenuItem("Copy");
		copy.addActionListener(new CopyListener());
		edit.add(copy);
		
		cut = new JMenuItem("Cut");
		cut.addActionListener(new CutListener());
		edit.add(cut);
		
		paste = new JMenuItem("Paste");
		paste.addActionListener(new PasteListener());
		edit.add(paste);

		open = new JMenuItem("Open");
		open.addActionListener(new OpenFileListener());
		file.add(open);

		exit = new JMenuItem("Exit");
		exit.addActionListener(new ExitFileListener());
		file.add(exit);
		frame.setJMenuBar(menu);
		
		caret = new DefaultCaret();
		area = new JTextArea(25, 65);
		area.setLineWrap(true);
		area.setText(documentText);
		area.setWrapStyleWord(true);
		
		
		area.setCaret(caret);
		documentListener = new TextDocumentListener();
		area.getDocument().addDocumentListener(documentListener); 
		
		scrollpane = new JScrollPane(area);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(documentNameLabel)
				.addComponent(scrollpane));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(documentNameLabel)
				.addComponent(scrollpane));
	}


	/**
	 * Class representing the DocumentListener for the document in the GUI.
	 */
	private class TextDocumentListener implements DocumentListener {
		/**
		 * Sends an edit message to the server for an insertUpdate
		 */
		public void insertUpdate(DocumentEvent e) {
			synchronized (area) {
				int changeLength = e.getLength();
				int offset = e.getOffset();
				int insert = caret.getDot();
				String message;
				try {
					String addedText = area.getDocument().getText(offset,
							changeLength);
					String encodedText = Encoding.encode(addedText);					
					currentVersion=client.getVersion();
					message = "change " + documentName + " "+username+" "+ currentVersion+ " insert " + encodedText
							+ " " + insert;
					if(DEBUG){
						System.out.println(message);
					}
					sent = true;
	            	MessageSwingWorker worker = new MessageSwingWorker(client,
							message, sent);
					worker.execute(); 
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		/**
		 * Sends an edit message to the server for a removeUpdate
		 */
		public void removeUpdate(DocumentEvent e) {
			synchronized (area) {
				int changeLength = e.getLength();				
				currentVersion=client.getVersion();			
				int offset = e.getOffset();
				int endPosition = offset + changeLength;
				String message = "change " + documentName +" "+username+" " +currentVersion+" remove " + offset
						+ " " + endPosition;

				if(DEBUG){
					System.out.println(message);
				}
				sent = true;
            	MessageSwingWorker worker = new MessageSwingWorker(client,
						message, sent);
				client.updateVersion(currentVersion+1);
				worker.execute();
			}
		}
	
		public void changedUpdate(DocumentEvent e) {
			// Plain text components do not fire these events
		}
	}


	
	/**
	 * Manages the cursor given the current cursor position,
	 * the position the edit was made, and the length of the change
	 * @param currentPos
	 * @param pivotPosition
	 * @param amount
	 */
	private void manageCursor(int currentPos, int pivotPosition, int amount) {
		if(DEBUG){
			System.out.println("first position: "+caret.getDot());
			System.out.println("pivot: "+pivotPosition);
			System.out.println("amount: "+amount);
		}

		if (currentPos >= pivotPosition) {
			if (currentPos <= pivotPosition + Math.abs(amount)) {
				caret.setDot(pivotPosition);
			} else {
				caret.setDot(amount+currentPos);
			}
		}
		else{
			caret.setDot(currentPos);
		}
		if(DEBUG){
			System.out.println("caret moved to: "+caret.getDot());
		}
	}

	/**
	 * Decodes and updates the document with the text from the server,
	 * also manages the cursor using editPosition and editLength.
	 * @param updatedText encoded text
	 * @param editPosition the offset of the change message sent from the server
	 * @param editLength the length of the change sent from the server
	 * @param version the version of the edit
	 */
	public void updateDocument(String updatedText, int editPosition,
			int editLength, String username, int version) {
		documentText = Encoding.decode(updatedText);
		int pos = caret.getDot();
		synchronized (area) {
			if(this.username!=null && !this.username.equals(username)){
			area.getDocument().removeDocumentListener(documentListener);
			area.setText(documentText);
			area.getDocument().addDocumentListener(documentListener);
			manageCursor(pos, editPosition, editLength);
			}
			else if(this.username!=null && this.username.equals(username)) {
				//check if version matches up
				if(currentVersion<version-1){
					area.getDocument().removeDocumentListener(documentListener);
					area.setText(documentText);
					area.getDocument().addDocumentListener(documentListener);
					caret.setDot(editPosition+editLength);
				}
				
			}

		}
	}
	
	/** Class representing a Listener on the New button in the JMenu */
	private class NewFileListener implements ActionListener {
		
		/** Sends a "new" message to the server when a client creates a new document
		 * with the document being the user input  
		 */
		public void actionPerformed(ActionEvent e) {
			String newDocumentName = JOptionPane.showInputDialog(
					"Enter a new document name", "");
			// If the client does not click on "cancel", it need to send the message to the server.
			if (newDocumentName !=null){
			    String message = "new " + newDocumentName;
            	MessageSwingWorker worker = new MessageSwingWorker(client,
						message, true);
				worker.execute();
			}
		}
	}

	/** Class representing a Listener on the Open button in the JMenu */
	private class OpenFileListener implements ActionListener {
		/**
		 * Sends a "look" message to the server, which will respond by showing a
		 * dialog with the documents on the server.
		 */
		public void actionPerformed(ActionEvent e) {
			// send message to client, get documentNames
			client.sendMessageToServer("look");
		}
	}

	
	/** Class representing a Listener on the Exit button in the JMenu */
	private class ExitFileListener implements ActionListener {
		/**
		 * Shows a JOptionPane asking the user to confirm an exit. If they
		 * confirm, the gui will close and the client will be disconnected from
		 * the server.
		 */
		public void actionPerformed(ActionEvent e) {
			int n = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to quit?", "Exit",
					JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				if(!client.getSocket().isClosed()) {
					client.sendMessageToServer("bye");
					}
				System.exit(0);
			}
		}
	}
	/** Class representing a listener on the Copy button in the JMenu*/
	private class CopyListener implements ActionListener {
		
		/** Copies the selected text from the text area. */
		public void actionPerformed(ActionEvent e) {
			area.copy();
		}
	}
	/** Class representing a listener on the Paste button in the JMenu */
	private class PasteListener implements ActionListener {
		
		/** Pastes the cut/copied text from the text area */
		public void actionPerformed(ActionEvent e) {
			area.paste();
		}
	}
	
	/** Class representing a listener on the Cut button in the JMenu */
	private class CutListener implements ActionListener {
		
		/** Cuts the selected text from the text area*/
		public void actionPerformed(ActionEvent e) {
			area.cut();
		}
	}
}
