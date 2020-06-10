package server;

import static org.junit.Assert.assertEquals;

import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OurThreadClassTest {

	// Test strategy:
	//
	// ***************************************************************************************************************
	// For the OurThreadClass, we want to test if it handles the input messages
	// from the client correctly by
	// 1. testing the handleRequest(string input) method
	// 2. testing if the field alive is set to false and its fields out and in
	// are closed after a client sends "bye" message.
	//
	//
	// For OurThread.handleRequest(String input):
	//
	// Partition the input space according to different types of the message
	// from the client.
	// 1. bye message.
	// 2. look message:
	// a. there is not yet any document stored on server
	// b. there is document stored on server
	// 3. new message:
	// a. user does not enter a name
	// b. user enters a duplicate name
	// c. user enters a valid name
	// 4. open message:
	// a. there is no document stored on the server with a name that the user
	// enters
	// b. there is one document stored on the server with a name that the user
	// enters
	// 5. change message:
	// a. insertion: insert a character at a valid/invalid(larger than current
	// text length) position or with a old/new version number
	// b. removal: remove at valid/invalid(larger than current text length)
	// positions with a old/new version number.
	// 6. other invalid messages that does not match our grammar.
	//
	// For all above cases, check:
	// 1. if the method returns the correct output;
	// 2. if documentMap is modified correctly;
	// 3. if the documentVersionMap is modified correctly;
	// 4. if the fields alive is modified correctly.
	//
	// We will also use eclEmma to check if we have cover all the cases in the
	// handleRequest(input).


	private final String error1 = "Error: Document already exists.";
	private final String error2 = "Error: No such document.";
	private final String error3 = "Error: No documents exist yet.";
	private final String error4 = "Error: Insert at invalid position.";
	private final String error5 = "Error: You must enter a name when creating a new document.";
	private final String error6 = "Error: Invalid arguments"; 

	/**
	 * Testing if two maps are equals:
	 *  by testing if they have the same key-value pairs.
	 * 
	 * @param map1  the one map that we are testing equality on
	 * @param map2   another map  that we are testing equality on
	 */
	public void equals(Map<String, StringBuffer> map1,
			Map<String, StringBuffer> map2) {
		assertEquals(map1.size(), map2.size());
		for (Object key : map1.keySet().toArray()) {
			assertEquals(true, map2.containsKey(key));
			int length1 = ((StringBuffer) map1.get(key)).length();
			int length2 = ((StringBuffer) map2.get(key)).length();
			if (length1 != 0 && length2 != 0) {
				// if the client adds something to the text.
				assertEquals(
						true,
						map2.get(key).toString()
								.equals(map1.get(key).toString()));
			}
		}
	}

	// "bye message" with thread alive
	@Test
	public void testForBye1() {
		// create a thread object.
		Socket socket = new Socket();
		Map<String, StringBuffer> map = new HashMap<String, StringBuffer>();
		Map<String, Integer> versions = new HashMap<String, Integer>();
		Server server = new Server(8888, map, versions);
		Map<String, StringBuffer> correctMap = Collections
				.synchronizedMap(new HashMap<String, StringBuffer>());
		OurThreadClass t = new OurThreadClass(socket, server);
		String result = t.handleRequest("bye");
		assertEquals("bye", result);
		equals(correctMap, server.getDocumentMap());
		assertEquals(false, t.getAlive());

	}

	// "bye" message with thread dead
	@Test(expected = RuntimeException.class)
	public void testForBye2() {
		// create a thread object.
		Socket socket = new Socket();
		Map<String, StringBuffer> map = new HashMap<String, StringBuffer>();
		Map<String, Integer> versions = new HashMap<String, Integer>();
		Server server = new Server(8886, map, versions);
		OurThreadClass t = new OurThreadClass(socket, server);
		t.handleRequest("bye");
		t.handleRequest("bye");
	}

	// Testing :
	// "look" message with all three cases:
	// a. there is not yet any document stored on server
	// b. there is document stored on server
	@Test
	public void testLook() {
		Socket socket = new Socket();
		Map<String, StringBuffer> map = new HashMap<String, StringBuffer>();
		Map<String, Integer> versions = new HashMap<String, Integer>();
		Server server = new Server(6666, map, versions);
		Map<String, StringBuffer> correctMap = Collections
				.synchronizedMap(new HashMap<String, StringBuffer>());
		OurThreadClass t = new OurThreadClass(socket, server);

		String result1 = t.handleRequest("look");
		assertEquals(error3, result1);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		t.handleRequest("new a");
		correctMap.put("a", new StringBuffer());
		String result2 = t.handleRequest("look");
		assertEquals("alldocs a", result2);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());
	}

	// Testing
	// "new" messages:
	// a. user does not enter a name
	// b. user enters a duplicate name
	// c. user enters a valid name
	@Test
	public void testNew() {
		Socket socket = new Socket();
		Map<String, StringBuffer> map = new HashMap<String, StringBuffer>();
		Map<String, Integer> versions = new HashMap<String, Integer>();
		Server server = new Server(4442, map, versions);
		Map<String, StringBuffer> correctMap = Collections
				.synchronizedMap(new HashMap<String, StringBuffer>());
		OurThreadClass t = new OurThreadClass(socket, server);

		String result1 = t.handleRequest("new a");
		correctMap.put("a", new StringBuffer());
		correctMap.put("a", new StringBuffer());
		assertEquals("new a", result1);
		assertEquals(true, t.getAlive());
		Integer a=1;
		assertEquals(server.getDocumentVersionMap().get("a"), a);
		equals(correctMap, server.getDocumentMap());

		String result2 = t.handleRequest("new a");
		assertEquals(error1, result2);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		String result3 = t.handleRequest("new ");
		assertEquals(error5, result3);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

	}

	// 4. open message:
	// a. there is no document stored on the server with a name that the user
	// enters
	// b. there is one document stored on the server with a name that the user
	// enters

	@Test
	public void testOpen() {
		Socket socket = new Socket();
		Map<String, StringBuffer> map = new HashMap<String, StringBuffer>();
		Map<String, Integer> versions = new HashMap<String, Integer>();
		Server server = new Server(4443, map, versions);
		Map<String, StringBuffer> correctMap = Collections
				.synchronizedMap(new HashMap<String, StringBuffer>());
		OurThreadClass t = new OurThreadClass(socket, server);

		String result1 = t.handleRequest("open a");
		assertEquals(error2, result1);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		t.handleRequest("new a");
		correctMap.put("a", new StringBuffer());
		correctMap.put("a", new StringBuffer());

		String result2 = t.handleRequest("open a");
		assertEquals("open a 1 ", result2);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		String result3 = t.handleRequest("open b");
		assertEquals(error2, result3);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());
	}

	// Testing:
	// 1. "change" message with the insert at valid position with new version number
	// 2. "change" message with the remove at valid position with new version number
	// 3. "change" message with insert at invalid position but new version number
	// 4. "change" message with remove at invalid position but new version number
	// 5. "change" message with the insert at valid position but with invalid name and new version number
	// 6. "change" message with the remove at valid position but with invalid name and new version number
	// 7. "change" message with the remove at (in)valid position but with an old version number
	// 8. "change" message with the insert at (in)valid position but with an old version number
	@Test
	public void testChange() {
		Socket socket = new Socket();
		Map<String, StringBuffer> map = new HashMap<String, StringBuffer>();
		Map<String, Integer> versions = new HashMap<String, Integer>();
		Server server = new Server(4446, map, versions);
		Map<String, StringBuffer> correctMap = Collections
				.synchronizedMap(new HashMap<String, StringBuffer>());
		OurThreadClass t = new OurThreadClass(socket, server);

		t.handleRequest("new a");
		String result1 = t.handleRequest("change a name 1 insert h 0");
		correctMap.put("a", new StringBuffer("h"));
		assertEquals("change a name 2 0 1 h", result1);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		String result2 = t.handleRequest("change a name 2 insert c 1");
		correctMap.remove("a");
		correctMap.put("a", new StringBuffer("hc"));
		assertEquals("change a name 3 1 1 hc", result2);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		String result3 = t.handleRequest("change a name 3 remove 0 1");
		correctMap.remove("a");
		correctMap.put("a", new StringBuffer("c"));
		assertEquals("change a name 4 0 -1 c", result3);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());
		Integer a=4;
		assertEquals(server.getDocumentVersionMap().get("a"), a);
		
		String result4 = t.handleRequest("change a name 4 insert 1 2");
		assertEquals(error4, result4);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		String result5 = t.handleRequest("change B name 1 remove 1 1");
		assertEquals(error2, result5);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		String result6 = t.handleRequest("change B name 2 insert 1 p");
		assertEquals(error2, result6);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

	}

	// message that does not match our grammar
	// Contains invalid character/ word.
	@Test
	public void grammarMatchTest() {
		Socket socket = new Socket();
		Map<String, StringBuffer> map = new HashMap<String, StringBuffer>();
		Map<String, Integer> versions = new HashMap<String, Integer>();
		Server server = new Server(4448, map, versions);
		Map<String, StringBuffer> correctMap = Collections
				.synchronizedMap(new HashMap<String, StringBuffer>());
		OurThreadClass t = new OurThreadClass(socket, server);

		String result1 = t.handleRequest("new a   bc");
		assertEquals(error6, result1);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		String result2 = t.handleRequest("open a&b");
		assertEquals(error6, result2);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		t.handleRequest("new a");
		t.handleRequest("change a 1 1 1 insert h 0 ");
		correctMap.put("a", new StringBuffer("h"));

		String result5 = t.handleRequest("look happy");
		assertEquals(error6, result5);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		String result6 = t.handleRequest(" Bye");
		assertEquals(error6, result6);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());
	}

	// Some overall test
	@Test
	public void overallTest1() {
		Socket socket = new Socket();
		Map<String, StringBuffer> map = new HashMap<String, StringBuffer>();
		Map<String, Integer> versions = new HashMap<String, Integer>();
		Server server = new Server(4447, map, versions);
		Map<String, StringBuffer> correctMap = Collections
				.synchronizedMap(new HashMap<String, StringBuffer>());
		OurThreadClass t = new OurThreadClass(socket, server);

		String result1 = t.handleRequest("look");
		assertEquals(error3, result1);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		String result2 = t.handleRequest("open a ");
		assertEquals(error2, result2);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());

		String result3 = t.handleRequest("new a221 ");
		assertEquals("new a221", result3);
		assertEquals(true, t.getAlive());
		correctMap.put("a221", new StringBuffer());
		equals(correctMap, server.getDocumentMap());
		
		String result4 = t.handleRequest("change a221 name 1 insert hermione 0");
		correctMap.put("a221", new StringBuffer("hermione"));
		assertEquals("change a221 name 2 0 8 hermione", result4);
		assertEquals(true, t.getAlive());
		equals(correctMap, server.getDocumentMap());	
		Integer a=2;
		assertEquals(server.getDocumentVersionMap().get("a221"), a);
	}

}
