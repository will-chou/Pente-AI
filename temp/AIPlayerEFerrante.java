package aiplayer;
import pente.*;

public class AIPlayerEFerrante extends AIPlayer {

	// Change these to match your AIPlayer info
	private static String name = "Mr. Ferrante Random";
	private static String iconFile = "white.png";
	
	public AIPlayerEFerrante(int id) {
		super(iconFile, name, id);
	}

	@Override
	public Location makeMove(int[][] idArray, int moveCount) {

		// If we are Player 1 and this is our first move, play in the center of the board
		if (moveCount == 1) {
			return new Location(idArray.length/2, idArray[0].length/2);
		}
		// If we are Player 1 and this is our second move, play randomly outside the center box
		else if (moveCount == 3) {
			int row = 0, col = 0;
			do {
				row = (int)(Math.random()*idArray.length);
				col = (int)(Math.random()*idArray[0].length);				
			} while (!isOutsideStartBox(idArray, row, col));
			return new Location(row, col);
		}
		// Otherwise, move in a random empty location
		boolean done = false;
		while (!done) {
			int row = (int)(Math.random()*idArray.length);
			int col = (int)(Math.random()*idArray[0].length);
			if (idArray[row][col] == 0) {
				//System.out.println(getName() + " placing piece at (" + row + ", " + col + ")");
				return new Location(row, col);
			}
		}
		return null;
	}

	public boolean isOutsideStartBox(int[][] idArray, int row, int col) {
		return Math.abs(row - idArray.length/2) > 2 && Math.abs(col - idArray[0].length/2) > 2;
	}
}
