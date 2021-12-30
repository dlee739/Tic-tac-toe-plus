
public class Layout {
	private String boardLayout; // string representation of board layout
	private int score; // score of the current layout
	
	public Layout(String boardLayout, int score) { // constructor
		this.boardLayout = boardLayout;
		this.score = score;
	}
	
	public String getBoardLayout() {
		return boardLayout;
	}
	
	public int getScore() {
		return score;
	}
}
