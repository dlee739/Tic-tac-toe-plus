
public class Board implements BoardADT {
	/*
	 *  Constant variables (TA approved)
	 */
	private final char COMPUTER = 'c';
	private final char HUMAN    = 'h';
	private final char EMPTY = 'e';
	
	private final int HUMAN_WON = 0;
	private final int COMPUTER_WON = 3;
	private final int UNDECIDED = 1;
	private final int DRAW = 2;
	
	private final int size = 9973; // size of the hash table
	
	/*
	 * Instance Variables
	 */
	private char[][] theBoard;
	private int board_size;
	private int empty_positions;
	private int max_levels; 

	public Board(int board_size, int empty_positions, int max_levels) { // constructor
		this.board_size = board_size;
		this.empty_positions = empty_positions;
		this.max_levels = max_levels;
		theBoard = new char[board_size][board_size]; // 2D char array initialized 
		
		for (int i = 0; i < board_size; i++) { // board array initialized with EMPTY 
			for (int j = 0; j < board_size; j++) {
				theBoard[i][j] = EMPTY;
			}
		}
	}
	
	public Dictionary makeDictionary() {
		Dictionary dict = new Dictionary(size);
		return dict;
	}
	
	// convertToString() converts the 2D char array, theBoard, into its string 
	//		form and returns it.
	private String convertToString() {
		String str = ""; // String variable to be returned
		for (int i = 0; i < board_size; i++) { // iterate through theBoard
			for (int j = 0; j < board_size; j++) {
				str += String.valueOf(theBoard[j][i]); // add to string variable
			}
		}
		return str;
	}
	
	public int repeatedLayout(Dictionary dict) {
		String layout = convertToString(); // get layout in string form
		
		return dict.getScore(layout); // check through dictionary
	}
	
	public void storeLayout(Dictionary dict, int score) {
		String layout = convertToString(); // get layout in string form
		
		Layout newLayout = new Layout(layout, score); // new layout obj
		
		try { // try catch clause for dictionary put method
			dict.put(newLayout);
		} catch (DictionaryException e) {
			e.printStackTrace();
		} 
	}
	
	public void saveTile(int row, int col, char symbol) {
		theBoard[row][col] = symbol;
	}

	public boolean positionIsEmpty(int row, int col) {
		return theBoard[row][col] == EMPTY;
	}
	
	public boolean isComputerTile(int row, int col) {
		return theBoard[row][col] == COMPUTER;
	}
	
	public boolean isHumanTile(int row, int col) {
		return theBoard[row][col] == HUMAN;
	}
	
	// adjacentSymbols(symbol, row, col, rowC, colC) recursive method which returns 
	//		true if the selected line consists of selected symbols.
	// note: selected line can either be horizontal, vertical, or diagonal.
	//      row/col are incremented each time depending on the rowC(ounter) and 
	//			colC(ounter)
	// example: row = 0, col = 0, rowC = 1, colC = 0 -> recursively checks all 
	//		the rows by incrementing 1 to row each time
	private boolean adjacentSymbols(char symbol, int row, int col, int rowC, int colC) {
		// Base Case
		if (row == board_size || col == board_size) { // row & col out of range
			return true;
		}
		// Recursive Case
		return (theBoard[row][col] == symbol) && // current tile contains symbol
				adjacentSymbols(symbol, row + rowC, col + colC, rowC, colC); // recursive call
																			 // to next tile
	}
	
	public boolean winner(char symbol) {
		// Check horizontal match
		for (int i = 0; i < board_size; i++) {
			if (adjacentSymbols(symbol, i, 0, 0, 1)) {
				return true;
			}
		}
		
		// Check vertical match
		for (int i = 0; i < board_size; i++) {
			if (adjacentSymbols(symbol, 0, i, 1, 0)) {
				return true;
			}
		}
		
		// Check diagonal match
		if (adjacentSymbols(symbol, 0, 0, 1, 1) || // diagonal from top left to bottom right
				adjacentSymbols(symbol, 0, board_size - 1, 1, -1)) { // diagonal from 
			return true;
		}
		
		return false;
	}
	
	// slidable(symbol, row, col) determines whether the selected symbol (player) is 
	// 		able to slide at given tile.
	// note: slidable when there are no tiles (except the chosen symbol) adjacent 
	//			to the empty positions of the board.
	//		 row and col parameters indicate an empty position
	// visualization: 3 by 3 board
	// [?][?][?]
	// [?][e][?]
	// [?][?][?]
	// unknown tiles are checked only when they are inside the board limit (board size)
	
	private boolean slidable(char symbol, int row, int col) {
		for (int i = row - 1; i < row + 2; i++) { // top row to bottom row of given row
			if (i >= 0 && i < board_size) { // i is within the vertical range
				for (int j = col - 1; j < col + 2; j++) { // left col to right col of 
														  // given col
					if (j >= 0 && j < board_size) { // j is within the horizontal range
						if (theBoard[i][j] == symbol) { // matching symbol found
							return true;
						}
					}
				}
			}
		}
		// no matching symbol around the given position
		return false;
	}
	
	// countEmptyPostions() counts and returns the current number of empty positions
	// 		on the board.
	private int countEmptyPostions() {
		int count = 0; // counter variable
		for (int i = 0; i < board_size; i++) { // iterate through every tiles of theBoard
			for (int j = 0; j < board_size; j++) {
				if (theBoard[i][j] == EMPTY) { // increment count when empty tile is found
					count++;
				}
			}
		}
		return count;
	}
	
	public boolean isDraw(char symbol, int empty_positions) {
		if (winner(COMPUTER) || winner(HUMAN)) { // winner exists
			return false;
		} 
		
		int num_empty = countEmptyPostions(); // current number of empty tiles
		
		if (empty_positions == 0 && num_empty == 0) { // theBoard does not have empty tiles
			return true;
		}
		
		if (empty_positions > 0 && num_empty == empty_positions) { // condition to check 
																   // for slide is met
			for (int i = 0; i < board_size; i++) {
				for (int j = 0; j < board_size; j++) {
					if (positionIsEmpty(i, j) && slidable(symbol, i, j)) { // the player 
																		  // can still slide
						return false;
					}
				}
			}
		} else { // still has remaining tiles left to place
			return false;
		}
		// all conditions to declare draw have been met
		return true;
	}
	
	public int evaluate(char symbol, int empty_positions) {
		if (winner(COMPUTER)) { // computer won
			return COMPUTER_WON;
		} else if (winner(HUMAN)) { // human won
			return HUMAN_WON;
		} else if (isDraw(symbol, empty_positions)) { // game is drawn
			return DRAW;
		}
		return UNDECIDED; // game yet to be decided; continue playing
	}
	
}
