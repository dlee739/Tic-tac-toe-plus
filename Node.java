/*
 * Class Name: Node
 * Description: Node that can be linked with each other. Stores layout.
 * Application: Entry of hash table
 */

public class Node {
	private Node next; // next node
	private Layout lo; // layout item
	
	public Node(Layout layout) { // constructor
		this.next = null; // next node initialized to null
		this.lo = layout;
	}

	public Node getNext() {
		return this.next;
	}
	
	public void setNext(Node nextnode) {
		this.next = nextnode;
	}
	
	public Layout getLayout() {
		return this.lo;
	}
}
