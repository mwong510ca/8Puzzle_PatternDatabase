package mwong.myprojects.eightpuzzle;

import java.util.Iterator;

import mwong.myprojects.utilities.RandomizedQueue;

/****************************************************************************
 *  @author   Meisze Wong
 *            www.linkedin.com/pub/macy-wong/46/550/37b/
 *
 *  Compilation: javac BoardPD8.java
 *  Dependencies : Direction.java, RandomizedQueue.java, PD8Combo.java
 *  
 *  A mutable data type for 8Puzzle board using pattern database
 *
 ****************************************************************************/

public class Board { 
    private static final byte N = 3;
    private static final int SIZE = 9;
    
    private static PatternDatabase patterns;
    private boolean isSolvable;
    private int pdIdx, pdVal, zeroX, zeroY;
             
    /**
     * Initializes pd8Combo pattern database object if not exists.
     */
    public Board() {
        if (patterns == null) {
        	patterns = new PatternDatabase();
        }
    }
    
    /**
     * Initializes a 3-by-3 board, where blocks starts from top row then left to right order.
     * 
     * @param blocks a byte array of tiles
     */
    public Board(byte[] blocks) {
        this();
        byte[] tiles = new byte[SIZE];
        isSolvable = true;
        System.arraycopy(blocks, 0, tiles, 0, SIZE);
        setPriorities(tiles);
    } 
    
    // Initializes a board with pattern index with zero's positions
    private Board(int pdIdx, int zeroX, int zeroY) {
        this.pdIdx = pdIdx;
        this.pdVal = patterns.getPDvalue(pdIdx);
        this.zeroX = zeroX;
        this.zeroY = zeroY;
    } 
    
    /**
     * Returns the column number of the zero space.
     * 
     * @return column number of the zero space
     */
    protected int getZeroX() {
        return zeroX;
    }

    /**
     * Returns the row number of the zero space.
     * 
     * @return row number of the zero space
     */
    protected int getZeroY() {
        return zeroY;
    }

    // review the given tiles, determine the solvable status,
    // position of zero space.
    private void setPriorities(byte[] tiles) {
        // calculate the horizontal invert distance
        // to determine the solvable state of the board
        for (int i = 0; i < SIZE; i++) {
            if (tiles[i] == 0) {
                zeroX = i % N;
                zeroY = i / N;
                break;
            }
        }
        
        pdIdx = patterns.getKeyIndex(tiles);
        if (pdIdx == -1) {
            isSolvable = false;
        } else {
            pdVal = patterns.getPDvalue(pdIdx);
        }
    }
    
    /**
     * Returns the boolean represent this board is the goal board.
     * 
     * @return boolean represent this board is the goal board
     */
    public boolean isGoal() {
        return pdVal == 0;
    }                

    /**
     * Returns the boolean represent this board is solvable.
     * 
     * @return boolean represent this board is solvable
     */
    public boolean isSolvable() {
        return isSolvable;
    }                
    
    /**
     * Returns the boolean represent this board equal y.
     * 
     * @param y the other board
     * @return boolean represent this board equal y
     */
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        return this.pdIdx == that.pdIdx;
    }        
    
    /**
     * Returns a board of move toward to the goal state.
     * 
     * @param direction the given Direction of previous move
     * @return board of move toward to the goal state
     */
    public Board solutionMove(Direction direction) {
        Iterator<Board> it = this.neighbors(direction).iterator();
        Board bestMove = it.next();
        while (it.hasNext()) {
            Board otherMove = it.next();
            if (otherMove.pdVal < bestMove.pdVal) {
                bestMove = otherMove;
            }
        }
        return bestMove;
    } 
          
    /**
     * Returns an independent iterator over all neighboring boards in random order.
     * 
     * @param direction the given Direction of previous move
     * @return independent iterator over all neighboring boards in random order
     */
    public Iterable<Board> neighbors(Direction direction) {
        RandomizedQueue<Board> allNeighbors = new RandomizedQueue<Board>();
        // move right
        if (direction != Direction.LEFT && zeroX != N-1) {
            allNeighbors.enqueue(shift(Direction.RIGHT));
        }
        // move down
        if (direction != Direction.UP && zeroY != N-1) {
            allNeighbors.enqueue(shift(Direction.DOWN));
        }
        // move left
        if (direction != Direction.RIGHT && zeroX != 0) {
            allNeighbors.enqueue(shift(Direction.LEFT));
        }
        // move up
        if (direction != Direction.DOWN && zeroY != 0) {
            allNeighbors.enqueue(shift(Direction.UP));
        }
        return allNeighbors;
    } 
        
    /**
     * Returns a neighbor board with given direction.
     * 
     * @param direction the given Direction of previous move
     * @return a neighbor board with given direction 
     */
    protected Board shift(Direction direction) {
        int nextIdx;
        switch (direction) {
        // RIGHT
        case RIGHT:   
            nextIdx = patterns.getLink(pdIdx, direction);
            return new Board(nextIdx, zeroX + 1, zeroY);
        // DOWN
        case DOWN:
            nextIdx = patterns.getLink(pdIdx, direction);
            return new Board(nextIdx, zeroX, zeroY + 1);
        // LEFT
        case LEFT:    
            nextIdx = patterns.getLink(pdIdx, direction);
            return new Board(nextIdx, zeroX - 1, zeroY);
        // UP
        case UP:     
            nextIdx = patterns.getLink(pdIdx, direction);
            return new Board(nextIdx, zeroX, zeroY - 1);
        default:    
            return null;
        }
    }
    
    /**
     * Returns the direction of move to "that" board.
     * 
     * @param that the other board
     * @return direction of move to "that" board
     */
    public Direction getDirection(Board that) {
        if (this.zeroX == that.zeroX) {
            if (this.zeroY - 1  == that.zeroY) {
                return Direction.UP;
            }
            if (this.zeroY + 1  == that.zeroY) {
                return Direction.DOWN;
            }
            return null;
        }
        if (this.zeroY == that.zeroY) {
            if (this.zeroX - 1  == that.zeroX) {
                return Direction.LEFT;
            }
            if (this.zeroX + 1  == that.zeroX) {
                return Direction.RIGHT;
            }
            return null;
        }
        return null;
    }

    /**
     * Returns a string representation of the board.
     * 
     * @return a string representation of the board
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int[] tiles = patterns.idx2combo(pdIdx);
        int idx = 0;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                s.append(String.format("%2d ", tiles[idx++]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}
