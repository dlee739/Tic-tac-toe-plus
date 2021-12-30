
public class Dictionary {
	/*
	 * Global Constant variable (TA approved)
	 */
	private final int COLLISION = 1;
	private final int NO_COLLISION = 0;
	private final int KEY_NOT_FOUND = -1;
	
	private final int X = 33; // x for hash function
	
	/*
	 * Instance Variable
	 */
	private Node[] nodeTable; // hash table
	private int hashSize; // M
	
	public Dictionary(int size) { // constructor
		this.hashSize = size;
		this.nodeTable = new Node[size]; // array of nodes
		for (int i = 0; i < size; i++) { // nodeTable initialized so that entries contain null
			nodeTable[i] = null;
		}
	}
	
	// polyHash(key) polynomial hash function that returns the index of hash table based on key
	// note: Includes compression
	private int polyHash(String key) {
		int len = key.length(); // length of key
		int n = (int) key.charAt(0); // decimal representation of char at index 0 using ASCII
		for (int i = 1; i < len; i++) {
			n = (n * X + (int) key.charAt(i)) % hashSize; // polynomial hash code with compression
		}
		return n;
	}
	
	public int put(Layout data) throws DictionaryException {
		Node newnode = new Node(data); // new node obj containing data
		String boardLayout = data.getBoardLayout(); // string representation of current board
		int hashKey = polyHash(boardLayout); // get hash key
		Node curnode = nodeTable[hashKey]; // current node
		
		if (curnode == null) { // entry is empty: no collision
			nodeTable[hashKey] = newnode; // hashtable entry set to newnode
			return NO_COLLISION;
		}

		Node prev = null; // previous node
		String curnodeBoardLayout; // current node's layout
		while (curnode != null) { // collision occurred
			curnodeBoardLayout = curnode.getLayout().getBoardLayout(); // current node's 
																	   // layout
			if (curnodeBoardLayout.equals(boardLayout)) { // if both keys (board layouts) 
														  // are same
				throw new DictionaryException("Dictionary already contains the same key"); 
				// exception is thrown
			}
			// update curnode
			prev = curnode;
			curnode = curnode.getNext();
		}
		prev.setNext(newnode); // link new node to the end of entry
		
		return COLLISION;
	}
	
	public void remove(String boardLayout) throws DictionaryException {
		int hashKey = polyHash(boardLayout); // get hash key
		Node curnode = nodeTable[hashKey]; // current node
		Node prev = null; // previous node
		String curnodeBoardLayout; // current node's layout
		
		while (curnode != null) { // entry exists
			curnodeBoardLayout = curnode.getLayout().getBoardLayout(); // current node's layout
			if (curnodeBoardLayout.equals(boardLayout)) { // if both keys (board layouts) 
				  										  // are same
				if (prev == null) { // no previous node; first entry to be removed
					nodeTable[hashKey] = curnode.getNext(); // first entry linked to next node
				} else { 
					prev.setNext(curnode.getNext()); // previous node and next node linked
				}
				return;
			}
			// update curnode
			prev = curnode;
			curnode = curnode.getNext();
		}
		// executed only if the entry is empty, or does not contain boardLayout
		throw new DictionaryException("Dictionary does not contain the key");
	}
	
	public int getScore(String boardLayout) {
		int hashKey = polyHash(boardLayout); // get hash key
		Node curnode = nodeTable[hashKey]; // current node
		String curnodeBoardLayout; // current node's layout
		
		while(curnode != null) { // entry is not empty
			curnodeBoardLayout = curnode.getLayout().getBoardLayout(); // current node's layout
			if (curnodeBoardLayout.equals(boardLayout)) { // node with same key found
				return curnode.getLayout().getScore(); // current node's score is returned
			}
			// update curnode
			curnode = curnode.getNext();
		}
		
		return KEY_NOT_FOUND; // key not found
	}
}
