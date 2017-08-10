package aiplayer;

 /*William Chou
 * P.4 APCS
 * 5/25/2015
 * 
 * Length: 3 hours
 * 
 * I spent 3 hours on this lab
 * over the weekend.  While fixing
 * some of the problems, there
 * are still many that I am currently
 * debugging.  Sometimes, my program
 * makes an illegal move, and I am
 * trying to figure out what it is.
 * I have focused on the offensive
 * aspect so far, trying to get 5 
 * in a row, and not so much the
 * defensive side.  After fixing 
 * some of the bugs, I will move on
 * to the defensive side.  Overall,
 * I feel that I made much progress.
 */

 
 
import pente.*;
import java.util.*;

public class AIPlayerWChouPeriod4 extends AIPlayer {

    // Change these to match your AIPlayer info
    private static String name = "William Chou";
    private static String iconFile = "emerald.png";
    
    int[] horizontal = {1, 1, 0, -1, -1, -1, 0, 1};
    int[] vertical = {0, 1, 1, 1, 0, -1, -1, -1};
    int opponentID;
    
    public AIPlayerWChouPeriod4(int id) {
        super(iconFile, name, id);
    }
    
    public boolean isInBounds(int[][] array, int row, int col) {
        return row >= 0 && row < array.length && col >= 0 && col < array[0].length;
    }
    
    public int getOpponentID(int[][] array) {
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[0].length; j++) {
                if(array[i][j] != 0 && array[i][j] != getID()) {
                    return array[i][j];
                }
            }
        }
        return -1;
    }
    
    public int numberOfStones(int[][] array) {
        int count = 0;
        
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[0].length; j++) {
                if(array[i][j] != 0) {
                    count++;
                }
            }
        }
        
        return count;
    }

    @Override
    public Location makeMove(int[][] idArray, int moveCount) {
        //if first move, put in center
        if(moveCount == 1) {
            return new Location(9, 9);
        }
        //if second move
        else if(moveCount == 2) {
            int r = (int)(Math.random() * 8);
            return new Location(9 + horizontal[r], 9 + vertical[r]);
        }
        //third move
        else if(moveCount == 3) {
            int r = (int)(Math.random() * 8);
            return new Location(9 + 3*horizontal[r], 9 + 3*vertical[r]);
        }
        //fourth move
        else if(moveCount == 4) {
            for(int i = 0; i < idArray.length; i++) {
                for(int j = 0; j < idArray[0].length; j++) {
                    if(idArray[i][j] == getID()) {
                        return putNextToStone(idArray, i, j);
                    }
                }
            }
            return putNextToStone(idArray, 9, 9);
        }
        else {
            //defense
            opponentID = getOpponentID(idArray);
            System.out.println("Opponent id: " + opponentID);
            
            int maxOppLength = 1;
            ArrayList<Integer> oppList = new ArrayList<Integer>();
            
            for(int i = 0; i < idArray.length; i++) {
                for(int j = 0; j < idArray[0].length; j++) {
                    if(idArray[i][j] == opponentID && isEndStone(idArray, i, j, opponentID)) {
                        if(fourInARow(idArray, i, j, opponentID) != -1) {
                            System.out.println("D4 row " + i + " col " + j);
                            
                            int changeIndex = fourInARow(idArray, i, j, opponentID);
                            if(idArray[i + 4*horizontal[changeIndex]][j + 4*vertical[changeIndex]] != 0) {
                                int temp = changeIndex + 4;
                                if(temp >= 8)
                                    temp -= 8;
                                if(isInBounds(idArray, i + horizontal[temp], j + vertical[temp])
                                && idArray[i + horizontal[temp]][j + vertical[temp]] == 0) {
                                    return new Location(i + horizontal[temp], j + vertical[temp]);
                                }
                                return putNextToStone(idArray, i, j);
                            }
                            
                            return new Location(i + 4 * horizontal[fourInARow(idArray, i, j, opponentID)],
                            j + 4 * vertical[fourInARow(idArray, i, j, opponentID)]);
                        }
                        if(threeInARow(idArray, i, j, opponentID) != -1) {
                            System.out.println("D3 row " + i + " col " + j);
                
                            int changeIndex = threeInARow(idArray, i, j, opponentID);
                            if(idArray[i + 3*horizontal[changeIndex]][j + 3*vertical[changeIndex]] != 0) {
                                int temp = changeIndex + 4;
                                if(temp >= 8)
                                    temp -= 8;
                                if(isInBounds(idArray, i + horizontal[temp], j + vertical[temp])
                                && idArray[i + horizontal[temp]][j + vertical[temp]] == 0) {
                                    return new Location(i + horizontal[temp], j + vertical[temp]);
                                }
                                return putNextToStone(idArray, i, j);
                            }
                            
                            return new Location(i + 3 * horizontal[threeInARow(idArray, i, j, opponentID)],
                            j + 3 * vertical[threeInARow(idArray, i, j, opponentID)]);
                        }
                        if(twoInARow(idArray, i, j, opponentID) != -1) {
                            System.out.println("D2 row " + i + " col " + j);
                            
                            int changeIndex = twoInARow(idArray, i, j, opponentID);
                            if(idArray[i + 2*horizontal[changeIndex]][j + 2*vertical[changeIndex]] != 0) {
                                int temp = changeIndex + 4;
                                if(temp >= 8)
                                    temp -= 8;
                                if(isInBounds(idArray, i + horizontal[temp], j + vertical[temp])
                                && idArray[i + horizontal[temp]][j + vertical[temp]] == 0) {
                                    return new Location(i + horizontal[temp], j + vertical[temp]);
                                }
                                return putNextToStone(idArray, i, j);
                            }
                            
                            return new Location(i + 2 * horizontal[twoInARow(idArray, i, j, opponentID)],
                            j + 2 * vertical[twoInARow(idArray, i, j, opponentID)]);
                        }
                    }
                }
            }
            
            //             for(int i = 0; i < idArray.length; i++) {
            //                 for(int j = 0; j < idArray[0].length; j++) {
            //                     if(twoInARow(idArray, i, j, getID()) && 
            //                 }
            //             }
            
            //offense
            int maxLength = 1;
            ArrayList<Integer> list = new ArrayList<Integer>();
            
            for(int i = 0; i < idArray.length; i++) {
                for(int j = 0; j < idArray[0].length; j++) {
                    if(isEndStone(idArray, i, j, getID()) && idArray[i][j] == getID()) {
                        if(fourInARow(idArray, i, j, getID()) != -1) {
                            System.out.println("O4 row " + i + " col " + j);
                            
                            if(maxLength < 4) {
                                maxLength = 4;
                                list.clear();
                                list.add(i);
                                list.add(j);
                            }
                            
                        }
                        if(threeInARow(idArray, i, j, getID()) != -1) {
                            System.out.println("O3 row " + i + " col " + j);

                            if(maxLength < 3) {
                                maxLength = 3;
                                list.clear();
                                list.add(i);
                                list.add(j);
                            }
                            
                        }
                        if(twoInARow(idArray, i, j, getID()) != -1) {

                            if(maxLength < 2) {
                                maxLength = 2;
                                list.add(i);
                                list.add(j);
                            }

                        }
                    }
                }
            }
            
            if(maxLength >= 2) {
                System.out.println(maxLength);
                int newPoint = (int)(Math.random() * list.size());
                if(newPoint > 0 && newPoint % 2 != 0) {
                    newPoint -= 1;
                }
                if(maxLength == 4) {
                    int changeIndex = fourInARow(idArray, list.get(newPoint), list.get(newPoint + 1), getID());
                    if(idArray[list.get(newPoint) + 4*horizontal[changeIndex]][list.get(newPoint + 1) + 4*vertical[changeIndex]] != 0) {
                        int temp = changeIndex + 4;
                        if(temp >= 8)
                            temp -= 8;
                        if(isInBounds(idArray, list.get(newPoint) + horizontal[temp], list.get(newPoint + 1) + vertical[temp])
                        && idArray[list.get(newPoint) + horizontal[temp]][list.get(newPoint + 1) + vertical[temp]] == 0) {
                            return new Location(list.get(newPoint) + horizontal[temp], list.get(newPoint + 1) + vertical[temp]);
                        }
                        return putNextToStone(idArray, list.get(newPoint), list.get(newPoint + 1));
                    }
                    if(isInBounds(idArray, list.get(newPoint) + 4*horizontal[changeIndex], list.get(newPoint + 1) + 4*vertical[changeIndex])) {
                        return new Location(list.get(newPoint) + 4 * horizontal[changeIndex],
                        list.get(newPoint + 1) + 4 * vertical[changeIndex]);
                    }
                    else
                        return putNextToStone(idArray, list.get(newPoint), list.get(newPoint + 1));
                }
                if(maxLength == 3) {
                    int changeIndex = threeInARow(idArray, list.get(newPoint), list.get(newPoint + 1), getID());
                    if(idArray[list.get(newPoint) + 3*horizontal[changeIndex]][list.get(newPoint + 1) + 3*vertical[changeIndex]] != 0) {
                        int temp = changeIndex + 4;
                        if(temp >= 8)
                            temp -= 8;
                        if(isInBounds(idArray, list.get(newPoint) + horizontal[temp], list.get(newPoint + 1) + vertical[temp])
                        && idArray[list.get(newPoint) + horizontal[temp]][list.get(newPoint + 1) + vertical[temp]] == 0) {
                            return new Location(list.get(newPoint) + horizontal[temp], list.get(newPoint + 1) + vertical[temp]);
                        }
                        return putNextToStone(idArray, list.get(newPoint), list.get(newPoint + 1));
                    }
                    if(isInBounds(idArray, list.get(newPoint) + 3*horizontal[changeIndex], list.get(newPoint + 1) + 3*vertical[changeIndex])) {
                        return new Location(list.get(newPoint) + 3 * horizontal[changeIndex],
                        list.get(newPoint + 1) + 3 * vertical[changeIndex]);
                    }
                    else
                        return putNextToStone(idArray, list.get(newPoint), list.get(newPoint + 1));
                }
                if(maxLength == 2) {
                    int changeIndex = twoInARow(idArray, list.get(newPoint), list.get(newPoint + 1), getID());
                    if(idArray[list.get(newPoint) + 2*horizontal[changeIndex]][list.get(newPoint + 1) + 2*vertical[changeIndex]] != 0) {
                        int temp = changeIndex + 4;
                        if(temp >= 8)
                            temp -= 8;
                        if(isInBounds(idArray, list.get(newPoint) + horizontal[temp], list.get(newPoint + 1) + vertical[temp])
                        && idArray[list.get(newPoint) + horizontal[temp]][list.get(newPoint + 1) + vertical[temp]] == 0) {
                            return new Location(list.get(newPoint) + horizontal[temp], list.get(newPoint + 1) + vertical[temp]);
                        }
                        return putNextToStone(idArray, list.get(newPoint), list.get(newPoint + 1));
                    }
                    if(isInBounds(idArray, list.get(newPoint) + 2*horizontal[changeIndex], list.get(newPoint + 1) + 2*vertical[changeIndex])) {
                        return new Location(list.get(newPoint) + 2 * horizontal[changeIndex],
                        list.get(newPoint + 1) + 2 * vertical[changeIndex]);
                    }
                    else
                        return putNextToStone(idArray, list.get(newPoint), list.get(newPoint + 1));
                }
                
            }
            
            return putNextToStone(idArray, 9, 9);
            
        }
        
        //return null;
    }
    
    public int twoInARow(int[][] array, int row, int col, int id) {
        if(array[row][col] == id) {
            for(int i = 0; i < 8; i++) {
                if(isInBounds(array, row + horizontal[i], col + vertical[i]) && 
                array[row + horizontal[i]][col + vertical[i]] == id) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public int threeInARow(int[][] array, int row, int col, int id) {
        if(twoInARow(array, row, col, id) != -1) {
            if(array[row][col] == id) {
                for(int i = 0; i < 8; i++) {
                    if(isInBounds(array, row + horizontal[i], col + vertical[i]) &&
                    isInBounds(array, row + 2*horizontal[i], col + 2*vertical[i]) &&
                    array[row + horizontal[i]][col + vertical[i]] == id && 
                    array[row + 2*horizontal[i]][col + 2*vertical[i]] == id) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    public int fourInARow(int[][] array, int row, int col, int id) {
        if(threeInARow(array, row, col, id) != -1) {
            if(array[row][col] == id) {
                for(int i = 0; i < 8; i++) {
                    if(isInBounds(array, row + horizontal[i], col + vertical[i]) &&
                    isInBounds(array, row + 2*horizontal[i], col + 2*vertical[i]) &&
                    isInBounds(array, row + 3*horizontal[i], col + 3*vertical[i]) &&
                    array[row + horizontal[i]][col + vertical[i]] == id && 
                    array[row + 2*horizontal[i]][col + 2*vertical[i]] == id && 
                    array[row + 3*horizontal[i]][col + 3*vertical[i]] == id) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    public boolean isEndStone(int[][] array, int row, int col, int id) {
        int count = 0;
        for(int i = 0; i < 8; i++) {
            if(isInBounds(array, row + horizontal[i], col + vertical[i]) && 
            array[row + horizontal[i]][col + vertical[i]] == id)
                count++;
        }
        if(count > 1) {
            return false;
        }
        return true;
    }
    
    public Location putNextToStone(int[][] array, int row, int col) {
        int count = 0;
        int r = (int)(Math.random() * 8);
        
        while(!isInBounds(array, row + horizontal[r], col + vertical[r]) ||
        array[row + horizontal[r]][col + vertical[r]] != 0) {
            r = (int)(Math.random() * 8);
            count++;
            if(count > 20) {
                break;
            }
        }
        
        if(count > 8) {
            int randRow;
            int randCol;
            
            do {
                randRow = (int)(Math.random() * array.length);
                randCol = (int)(Math.random() * array[0].length);
            } while(!isInBounds(array, randRow, randCol) || array[randRow][randCol] != 0);
            
            return new Location(randRow, randCol);
        }
        
        return new Location(row + horizontal[r], col + vertical[r]);
    }
}
