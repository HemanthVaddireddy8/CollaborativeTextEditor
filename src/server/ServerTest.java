package server;


public class ServerTest {
	
    //Testing strategy:
	//
	// *************************************************************************************
	//
	// For the server, we want to test:
	// 1. If the server could successfully handles multiple client connection.
	// 2. If the server could handle different requests from different client by changing 
	// its documentMap and documentVersionMap correctly
	// 3. If the server returns the appropriate message back to the appropriate client through the socket.
	// 
	// Test manually:
	// 1. one client connection
	//     The client should try all the possible options: new, open, exit , copy, cut, paste. 
	//     Test if the client gets the right document text as stored on the server.
	// 2. multiple client connections
	//     a. Only one client is editing. Similar to case 1, test if all clients get the updated document text
	//        if working on the same document. Also test specially for editing the text. 
	//        See if the text are inserted in the correct order as client originally enters.
	//     b. Two or more clients are editing at the same time.
	//        Use thread.sleep(500).
	//        Test: 
	//        1. two clients are inserting characters at the same time(at same/different positions with same/different text).
	//           Test if the correct characters are inserted at the correct positions.
	//           Also test if the changes will show up in the GUI of all clients who opens the same document.
	//        2. two clients are deleting the same characters at the same time. Test if only those characters are deleted
	//            and the changes will show up in the GUI of all clients who opens the same document.
	//        3. two clients are deleting different characters at the same time. Test if only those characters are deleted
	//            and the changes will show up in the GUI of all clients who opens the same document.
	//        4. one client is inserting a character into the sequence that another client is going to delete at the same time.
	//           Test if only below 2 cases happen: 
	//           a. If user A's message is handled first, then user B would get an “insert at invalid position.” error. 
    //           b. If user B‘s message is handled first, then the character will be inserted by then deleted.
	//       


}
