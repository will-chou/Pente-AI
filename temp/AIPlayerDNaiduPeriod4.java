package aiplayer;

 
import pente.*;
import java.util.*;
/*****************************************************************************
 * 
 * @author Dharma Naidu  Time : 2 - 3 hours spent improving
 * Third prototype.  I debugged the previous code, and made it so I could
 * actually place tiles correctly if I went first.  I also added code to 
 * prevent placement of blocks at "dead ends".  I added comments to try and
 * make the code more readable, and I increased the value of captures and 
 * protecting from captures.  I also added (but have not implemented) a method 
 * that looks for a chain that is not in a row.  Finally, I added some 
 * randomness to the code by adding equally weighted squares to a list to 
 * choose from.  Overall lots of work done.
 ****************************************************************************/
public class AIPlayerDNaiduPeriod4 extends AIPlayer {

	// Change these to match your AIPlayer info
	private static String name = "Dharma Naidu Mark 3";
	private static String iconFile = "amber.png";
	Random rando = new Random();
	
	public AIPlayerDNaiduPeriod4(int id) {
		super(iconFile, name, id);
	}

	/****
	 * WEIGHTING SYSTEM OF COPY ARRAY
	 * If win possible : 10000
	 * if stop win possible : 9000
	 * if we got nothing : spots ranked by closest to center
	 */
	@Override
	public Location makeMove(int[][] idArray, int moveCount) {
		if(moveCount == 1) {
			return new Location(idArray.length / 2, idArray[0].length / 2);
		}
		if(moveCount >= 2) {
			int[][] copy = new int[idArray.length][idArray[0].length];	
			copy = defaultCopy(copy);
			for(int row = 0; row < idArray.length; row++) {
				for(int col = 0; col < idArray[0].length; col++) {
					if(idArray[row][col] == 0) {
						if(longestRow(true, idArray, row, col) == 4)
							copy[row][col] = 10000;
						else if(longestRow(false, idArray, row, col) == 4) {
							copy[row][col] = 9500;
						}
						else if(longestRow(true, idArray,row, col) == 3) {
							copy[row][col] = 9000;
						}
						else if(longestRow(false, idArray,row, col) == 3) {
							copy[row][col] = 8500;
						}
						else {
							if(longestRow(true, idArray, row, col) != 0)
								copy[row][col] = longestRow(true, idArray, row, col) * 1000;
						}	
						
						if(capturePossible(true, idArray, row, col)) {
							copy[row][col] = copy[row][col] + 3000;
						}
						if(capturePossible(false, idArray, row, col)) {
							copy[row][col] = copy[row][col] + 1000;
						}
							
					}
				}
			}
			if(moveCount == 3) {
				for(int i = 0; i < idArray.length; i++) {
					for(int x = 0; x < idArray[0].length; x++) {
						if(Math.abs(i - 8) < 4 || Math.abs(x - 8) < 4) {
							copy[i][x] = 0;
						}
					}
				}
			}
			
			int biggest = findLargest(copy, idArray);
			ArrayList<Location> possible = new ArrayList<Location>();
			for(int row = 0; row < copy.length; row++) {
				for(int col = 0; col < copy[0].length; col++) {
					if(copy[row][col] == biggest && idArray[row][col] == 0) {
						possible.add(new Location(row, col));
					}
				}
			}
			return possible.get(rando.nextInt(possible.size()));
		}
		
		return null;
		
	}
	private int findLargest(int[][] copy, int[][] idArray) {
		int biggest = copy[0][0];
		for(int row = 0; row < copy.length; row++) {
			for(int col = 0; col < copy[0].length; col++) {
				if(copy[row][col] > biggest && idArray[row][col] == 0)
					biggest = copy[row][col];
			}
		}
		return biggest;
	}
	/**
	 * If "me" is true, then looking for capture.  Otherwise, looking to protect
	**/
	private boolean capturePossible(boolean me, int[][] idArray, int row, int col) {
		int row2 = row;
		int col2 = col;
		
		for(int i = 1; i <=8; i++) {
			boolean capture = true;
			row2 = row;
			col2 = col;
			row2 = changeRow2(i, row2);
			col2 = changeCol2(i, col2);
			if(me == true) {
				for(int c = 0; c < 2; c++) {
					if(isInBounds(idArray, row2, col2) && idArray[row2][col2] != getID() && idArray[row2][col2] != 0) {
						row2 = changeRow2(i, row2);
						col2 = changeCol2(i, col2);
					}
					else {
						capture = false;
					}
				}
				if(capture == true) {
					if(isInBounds(idArray, row2, col2) && idArray[row2][col2] == getID())
						return true;
				}
					
			}
			else {
				for(int c = 0; c < 2; c++) {
					if(isInBounds(idArray, row2, col2) && idArray[row2][col2] == getID()) {
						row2 = changeRow2(i, row2);
						col2 = changeCol2(i, col2);
					}
					else {
						capture = false;
					}
				}
				if(isInBounds(idArray, row2, col2) && capture == true) {
					if(idArray[row2][col2] != getID() && idArray[row2][col2] != 0)
						return true;
				}
			}
			
		}
		return false;
	}
	private int[][] defaultCopy(int[][] idArray) {
		for(int i = 0; i < idArray.length; i++) {
			for(int x = 0; x < idArray[0].length; x++) {
				idArray[i][x] = 15 - (int)Math.sqrt(Math.pow(i - 9, 2) + Math.pow(x - 9, 2));
			}
		}
		return idArray;
	}
	/**
	 * 
	 * @param idArray
	 * @param row
	 * @param col
	 * @return the longest row that already exists (if it can be made into a 5 in a row).  4 is max
	 */
	private int longestRow(boolean me, int[][] idArray, int row, int col) {
		int longestCount = 0;
		int count = 0;
		int row2 = row;
		int col2 = col;
		
		for(int i = 1; i <=8; i++) {
			count = 0;
			row2 = row;
			col2 = col;
			row2 = changeRow2(i, row2);
			col2 = changeCol2(i, col2);
			while(isInBounds(idArray, row2, col2)) {
				if(me) {
					if(idArray[row2][col2] == getID()) {
						count++;
						row2 = changeRow2(i, row2);
						col2 = changeCol2(i, col2);
					}
					else {
						break;
					}
				}
				else {
					if(idArray[row2][col2] != getID() && idArray[row2][col2] != 0) {
						count++;
						row2 = changeRow2(i, row2);
						col2 = changeCol2(i, col2);
					}
					else {
						break;
					}
				}
			}
			if(me){
				if(count > longestCount && longestPossibility(idArray, row, col, i) >= 6)
					longestCount = count;
			}
			else
				if(count > longestCount)
					longestCount = count;
		}
		return longestCount;
		
	}
	/**
	 * 
	 * @param idArray
	 * @param row
	 * @param col
	 * @param change : which direction to go in. in a grid :
	 * 1 2 3
	 * 8 * 4
	 * 7 6 5
	 * @return the longest row of your pieces that can be made sandwiched between 2 enemy pieces
	 */
	private int longestPossibility(int[][] idArray, int row, int col, int change) {
		int count = 0;
		int row2 = row;
		int col2 = col;
		int inverseChange = change + 4;
		if(inverseChange > 8)
			inverseChange -= 8;

			while(isInBounds(idArray, row2, col2)) {
				if(idArray[row2][col2] == getID() || idArray[row2][col2] == 0) {
					count++;
					row2 = changeRow2(change, row2);
					col2 = changeCol2(change, col2);
				}
				else
					break;
				if(count >= 8)
					break;
			}
			row2 = row;
			col2 = col;
			while(count < 8 && isInBounds(idArray, row2, col2)) {
				if(idArray[row2][col2] == getID() || idArray[row2][col2] == 0) {
					count++;
					row2 = changeRow2(inverseChange, row2);
					col2 = changeCol2(inverseChange, col2);
				}
				else
					break;
				if(count >= 8)
					break;
			}
			return count;
	}
	
	
	
	private int longestChain(int[][] idArray, int row, int col) {
		int longestCount = 0;
		int count = 0;
		int row2 = row;
		int col2 = col;
		
		for(int i = 1; i <=8; i++) {
			count = 0;
			row2 = row;
			col2 = col;
			row2 = changeRow2(i, row2);
			col2 = changeCol2(i, col2);
			for(int d = 0; d < 5; d++) {
				if(isInBounds(idArray, row2, col2) && idArray[row2][col2] == getID()) {
					count++;
					row2 = changeRow2(i, row2);
					col2 = changeCol2(i, col2);
				}
				else {
					break;
				}
			}
			if(count > longestCount)
				longestCount = count;
		}
		return longestCount;
		
	}
	private int changeRow2(int num, int row2) {
		if(num == 1) {
			 return --row2;
		}
		else if(num == 2) {
			 return --row2;
		}
		else if(num == 3) {
			 return --row2;
		}
		else if(num == 4) {
			 return row2;
		}
		else if(num == 5) {
			 return ++row2;
		}
		else if(num == 6) {
			 return ++row2;
		}
		else if(num == 7) {
			 return ++row2;
		}
		else{
			 return row2;
		}
		
	}
	private int changeCol2(int num, int col2) {
		if(num == 1) {
			 return --col2;
		}
		else if(num == 2) {
			 return col2;
		}
		else if(num == 3) {
			 return ++col2;
		}
		else if(num == 4) {
			 return ++col2;
		}
		else if(num == 5) {
			 return ++col2;
		}
		else if(num == 6) {
			 return col2;
		}
		else if(num == 7) {
			 return --col2;
		}
		else{
			 return --col2;
		}
		
	}
	private boolean isInBounds(int[][] array, int row, int col) {
		return row >= 0 && row < array.length && col >= 0 && col < array[0].length;
		//
	}
}
