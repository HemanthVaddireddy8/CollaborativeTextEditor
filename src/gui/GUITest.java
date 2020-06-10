package gui;

public class GUITest {
/*
 * Testing strategy for GUI:
 * 
 * GUI is tested manually by clicking on all buttons available to test all the views available and 
 * enter valid and invalid input in the TextField to make sure error messages are displayed appropriately. 
 * 
 * 
 * **ConnectView Test
 * The first window that pops ups when Client_main runs  
 * 
 * When valid input is entered =>i.e. Host: localhost; Port: 4444 if you are running on local computer
 * => user can either press "connect" button or press enter => switch to UserName View
 * 
 * If port entered is 4 digits integer but not right host 
 * => Error message is the string in the host textfields and both host and port textfields are cleared
 * If right host is entered but not connected to the right port or computer not connected to network
 * => Error message "Connection Refused" and textfields are cleared
 * 
 * Other invalid input: (for example if either of the field is empty or port not 4 digit integer) 
 * => Error message "Invalid Arguments" pops up and textfields are cleared
 * 
 * **UserName Test
 * If valid input (without space and only digits and alphabet) are entered in the textfield=> switch to WelcomeView
 * If invalid input are entered => Error message "invalid argument"
 * If the username was used before => Error message "Username is not available"
 * 
 * **WelcomeView Test
 * WelcomeView shows up after the client successfully connects to the server for the user to enter new document name or open an existing document.
 * *************************
 * Test for "Create" button 
 * **************************
 * If the new document name is empty or contains invalid characters
 * => error dialogue with "Document name cannot be empty and must only contain letters and digits" pops up
 * If the document name is legal (contains only letters and digits without spaces)
 * => user can press "create" button or press enter to switch to DocumentView.
 * 
 * *******************************************
 * Test for "Open Existing Document" button 
 * ********************************************
 * click on ""Open Existing Document" button => open a document dialogue pops up.
 * => use the scrollview to select the document to be opened
 * => press "create" button or press enter to switch to DocumentView of the document selected
 * 
 * If there is no document on the server yet
 * => pops up error message "No document exists yet"
 * 
 * **DocumentView Test
 * DocumentView pops up with the name of the document at the upper left hand corner.
 * * "File" button Test
 * User press "file" button, a drop down list with "new", "open" and "exit" option opens up.
 * ****************
 * user press "New" 
 * ****************
 * "enter a new document name" window pops up and can do the following
 *  1. user type in a valid document name and press enter or "OK"=> a new DocumentView opens up that replaces the old one
 *  2. user type in an invalid document name (a name with space) and press enter or "OK"=> "invalid document name" error dialogue pops up.
 *  3. user enter a name that already exist and press enter or "OK" => "document already exist" error pops up.
 *  4. user press cancel => return to the original DocumentView
 *  
 * ****************
 * user press "open" 
 * ****************
 *  "open a document dialogue" pops up with a scroll bar that provides a list of available document on the server.
 *  user can select a document name on the scroll bar and click enter or "OK" button
 *  =>DocumentView with the name of the document the client chose opens up and replaces the old DocumentView.
 *  user press cancel => return to the original DocumentView.
 *  
 *****************
 * user press "exit" 
 * ****************
 * "are you sure you want to exit" window pops up.
 * 1. user click yes => DocumentView disappears 
 * 2. user click no => pop up disappears and DocumentView remain unchanged
 * 
 * * * "Edit" button Test
 * User press "edit" button, a drop down list with "copy", "cut" and "paste" option opens up.
 * ****************
 * Test for "Copy" button
 * ****************
 * user can highlight some texts in the textfield and press "copy"
 * => the scroll-list disappears after clicking, the text remain highlighted
 * The keyboard short cut "control+c" also works the same way as "copy" button
 * ****************
 * Test for "Cut" button
 * ****************
 * user can highlight some texts in the textfield and press "Cut"
 * => the scroll-list disappears after clicking, the text highlighted disappears from the screen
 * The keyboard short cut "control+x" also works the same way as "cut" button
 * ***************
 * Test for "Paste" button 
 * ****************
 * user can place the cursor at a location and press "paste"
 * =>the previous copied text appears at the location of the cursor
 * The keyboard short cut "control+v" also works the same way as "paste" button
 * 
 * * editting TextField test
 * user type on the TextField
 * =>add:the letters being typed appears on the screen.
 * =>add:user can change the place of the cursor and next letter being typed appears at the place where the cursor was moved to
 * =>delete:user can delete letter by pressing space bar or a group of letters by selecting a string of letters and press delete
 * => the user could also press enter to go to a new line.
 * =>concurrency (use thread.sleep(500) to increase latency and using multiple computers):  
 * when more than one user opens the same document, the letters being typed or deleted on one document appears or disappears from the other opened document
 * two users try to edit the same document at the same time => the addition and deletion appears at the correct places.
 * if one user tries to delete while another user tries to insert at the same position
 * =>the user inserting will get an error message "invalid insert position."
 * 
 */
    
    
    
    
}
