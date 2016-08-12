package mwong.myprojects.utilities;

/****************************************************************************
 *  @author   Meisze Wong
 *            www.linkedin.com/pub/macy-wong/46/550/37b/
 *
 *  Compilation: javac Stopwatch.java
 *
 *  An immutable data type Stopwatch with start, stop and reset features
 *
 ****************************************************************************/

public class Stopwatch { 

    private long startTime;
    private double stopTime;
    private boolean active;

    /**
     * Initializes a new stop watch.
     */
    public Stopwatch() {
        reset();
    } 

    /**
     * Reset the stop watch to 0.0s.
     */
    public void reset() {
        startTime = System.currentTimeMillis();
        stopTime = 0.0;
        active = false;
    }

    /**
     * Start the stop watch.
     * 
     * @return double the starting time in seconds
     */
    public double start() {
        startTime = (long) (System.currentTimeMillis() - stopTime);
        active = true;
        return stopTime / 1000.0;
    }

    /**
     * Stop the stop watch without reset.
     * 
     * @return double the stopped time in seconds
     */
    public double stop() {
        stopTime = System.currentTimeMillis() - startTime;
        return stopTime / 1000.0;
    }
   
    /**
     *  Returns the current time of stop watch.
     *  
     *  @return current time of stopwatch in second
     */
    public double currentTime() {
        long now = System.currentTimeMillis();
        return (now - startTime) / 1000.0;
    }
    
    /**
     *  Returns the boolean represent the active state of stop watch.
     *  
     *  @return boolean represent the active state of stop watch
     */
    public boolean status() {
        return active;
    }
} 
