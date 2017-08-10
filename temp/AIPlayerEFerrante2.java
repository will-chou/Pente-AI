package aiplayer;
import java.util.ArrayList;

import pente.AIPlayer;
import pente.Location;

public class AIPlayerEFerrante2 extends AIPlayer {

	// Change these to match your AIPlayer info
	private static String name = "Mr. Ferrante Cluster";
	private static String iconFile = "black.png";
	
	public AIPlayerEFerrante2(int id) {
		super(iconFile, name, id);
	}

	// Adding helper methods like this is a good idea
	public boolean isInBounds(int[][] array, int row, int col) {
		return row >= 0 && row < array.length && col >= 0 && col < array[0].length;
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
		
		boolean done = false;
		int counter = 0;

		// Otherwise try to move next to an existing piece of ours
		while (!done) {
			int row = (int)(Math.random()*idArray.length);
			int col = (int)(Math.random()*idArray[0].length);

			ArrayList<Location> list = new ArrayList<Location>();
			
			// If empty, see if there are any neighbors here that are ours
			if (idArray[row][col] == 0) {
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						if (!(i == 0 && j == 0) && isInBounds(idArray, row + i, col + j) && idArray[row + i][col + j] > 0) {
							if (idArray[row + i][col + j] == getID())
								list.add(new Location(row + i, col + j));
						}
					}
				}
			}

			if (list.size() > 0) {
				Location loc = new Location(row, col);
				//System.out.println(getName() + " found a neighbor to move next to at (" + loc.getRow() + ", " + loc.getCol() + ")");
				return loc;
			}

			if (counter++ > 400)
				done = true;
		}
		
		done = false;

		// If we don't find a piece of ours after 400 tries, choose a random empty location
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
