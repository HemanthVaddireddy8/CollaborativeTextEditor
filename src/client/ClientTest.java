
package client;

import static org.junit.Assert.*;
import gui.MainWindow;
import org.junit.Test;

public class ClientTest {

    
    //testing strategy:
	//
	// ********************************************************************************
	// 
    // 1. test if the client handles the messages from the server correctly
	//
    // Partition the input space according whether the input matches our
    // grammar for server-to-client message and also the starting word of the message.
    // i.e: private final String regex = "(error [123]: .+)|"
    // + "(alldocs .\\+)|(new [\\w|\\d]+)|(open .[\\w|\\d]+)|"
    // + "(change [\\w|\\d]+ (.+))";
	// Test for error, alldocs, new, open, change messages.
	//
    // For each message, test if the underlying document state changed. 
    // Also by hand testing for different GUIs.
	
	// 2.Also check for valid connection to the server 
	// and the initiation of a new main window GUI for the client side.
	// 
	// 3. We also test the methods getText() and getName() for the client.
	//
	

    
     MainWindow main= new MainWindow();
     Client client= new Client(4444, "localhost", main);
   //making a new action listener
     ClientActionListener clientActionListener=new ClientActionListener(client, client.getSocket());
      
       
    @Test
    public void testForError1(){
        String input= "Error: Document already exists.";
        clientActionListener.handleMessageFromServer(input);
        assertEquals(null, client.getDocumentName());
        assertEquals(null, client.getText());
        //GUI pops up with the message "error 1: Document already exists."
       
    }
    
    @Test
    public void testForError2(){
        String input= "Error: No such document.";
        clientActionListener.handleMessageFromServer(input);
        assertEquals(null, client.getDocumentName());
        assertEquals(null, client.getText());
        //GUI pops up with the message "error 2: No such document."
       
    }
    
    @Test
    public void testForError3(){
        String input=  "Error: No document exist yet.";
        clientActionListener.handleMessageFromServer(input);
        assertEquals(null, client.getDocumentName());
        assertEquals(null, client.getText());
      //GUI pops up with the message "error 3: No document exist yet."
    }
    

       @Test
        public void testNew(){
            String input=  "new abc";
            clientActionListener.handleMessageFromServer(input);
            assertEquals("abc", client.getDocumentName()); 
            assertEquals(null, client.getText());
        }
       
       @Test
       public void testOpen(){
           String input=  "open abc 4 bacg dgege vg";
           clientActionListener.handleMessageFromServer(input);
           assertEquals("abc", client.getDocumentName()); 
           assertEquals(4, client.getVersion()); 
           assertEquals("bacg dgege vg", client.getText()); 
       }
    
       @Test
       public void testChange(){
           String inputnew=  "new abc";
           clientActionListener.handleMessageFromServer(inputnew);
           String input=  "change abc name 1 1 1 test test";
           clientActionListener.handleMessageFromServer(input);
           assertEquals("abc", client.getDocumentName()); 
           assertEquals(1, client.getVersion()); 
           assertEquals("test test", client.getText()); 
       }
    
}
