package mwong.myprojects.eightpuzzle;

/****************************************************************************
 *  @author   Meisze Wong
 *            www.linkedin.com/pub/macy-wong/46/550/37b/
 *
 *  Compilation: javac Direction.java
 *
 *  Enum Direction for slide puzzle
 *
 ****************************************************************************/

/**
 *  Direction that can be used.
 *  <li>{@link #RIGHT}</li>
 *  <li>{@link #DOWN}</li>
 *  <li>{@link #LEFT}</li>
 *  <li>{@link #UP}</li>
 *  <li>{@link #NONE}</li>
 */
public enum Direction {
    RIGHT(0), DOWN(1), LEFT(2), UP(3), NONE(-1);
    private final int val;
    Direction(int val) { 
        this.val = val; 
    }
    
    /**
     *  Returns the value of current direction.
     *  
     *  @return value of current direction
     */
    public int getValue() { 
        return val; 
    }
    
    /**
     *  Returns the opposite direction of current direction.
     *  
     *  @return direction is the opposite of current direction
     */
    public Direction oppositeDirection() {
        switch(this) {
        case RIGHT : return LEFT;
        case DOWN : return UP;
        case LEFT : return RIGHT;
        case UP : return DOWN;
        default : return NONE;
        }
    } 

    /**
     *  Returns the symmetry direction of current direction.
     *  
     *  @return direction is the symmetry of current direction
     */
    public Direction symmetryDirection() {
        switch(this) {
        case RIGHT : return DOWN;
        case DOWN : return RIGHT;
        case LEFT : return UP;
        case UP : return LEFT;
        default : return NONE;
        }
    } 
}
       
