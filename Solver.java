package mwong.myprojects.eightpuzzle;

import java.util.Iterator;
import java.util.Scanner;

import mwong.myprojects.utilities.Deque;
import mwong.myprojects.utilities.Stopwatch;

/****************************************************************************
 *  @author   Meisze Wong
 *            www.linkedin.com/pub/macy-wong/46/550/37b/
 *
 *  Compilation: java Solver.java
 *  Execution: java Solver
 *  Dependencies : Board.java, Deque.java, Direction.java
 *
 *  Solver is the puzzle8 (3-by-3 boards) slider puzzle solver
 *  use pattern database 
 *
 ****************************************************************************/

public class Solver {
    private static Scanner scanner;
    private static final int SIZE_PUZZLE = 9;
    private int steps;
    private Deque<Direction> solutionMove;
    private Board initial;

    /**
     * Initializes Solver with the 3x3 Board and find a solution using pattern database.
     * 
     * @param initial the Board object
     */
    public Solver(Board initial) { 
        solutionMove = new Deque<Direction>();
        if (!initial.isSolvable()) {
            steps = -1;
            return;
        }
        this.initial = initial;
        if (initial.isGoal()) {
            steps = 0;
            return;
        }
        
        Board move = initial.solutionMove(Direction.NONE);
        Direction dir = initial.getDirection(move);
        solutionMove.addLast(dir);
        steps = 1;
        Board curr = move;
        
        while (!curr.isGoal()) {
            move = curr.solutionMove(dir);
            dir = curr.getDirection(move);
            solutionMove.addLast(dir);
            steps++;
            curr = move;
        }
    }           
    
    /**
     * Returns the minimum number of moves to solve initial board; -1 if no solution.
     * 
     * @return minimum number of moves to solve initial board; -1 if no solution
     */
    protected int moves() { 
        return steps;
    }
    
    /**
     * Print the minimum number of moves to the goal state.
     */
    protected void solutionSummary() {
        System.out.println("Minimum number of moves = " + steps);
    }      

    /**
     * Print the list of direction of moves to the goal state.
     */
    protected void solutionList() {
        Iterator<Direction> it = solutionMove.iterator();
        int count = 0;
        while (it.hasNext()) {
            System.out.print((++count) + " : " + it.next() + " ");
            if (count % 10 == 0 && it.hasNext()) {
                System.out.println();
            }
        }
        System.out.println("\n");
    }     

    /**
     * Print all boards of moves to the goal state.
     */
    protected void solutionDetail() {
        Iterator<Direction> it = solutionMove.iterator();
        int count = 0;
        System.out.println("Step : " + (count++));
        System.out.println(initial);
        Board next = initial;
        while (it.hasNext()) {
            Direction dir = it.next();
            System.out.println("Step : " + (count++) + "\t" + dir);
            next = next.shift(dir);
            System.out.println(next);
        }
    }
    
    /**
     *  test client to demonstrate the 8puzzle on console.
     *  
     *  @param args main function standard arguments
     */
    public static void main(String[] args) { 
        scanner = new Scanner(System.in, "UTF-8");        
        new Board();
        
        do {
            System.out.println("Enter " + SIZE_PUZZLE + " number from 0 to "
                    + (SIZE_PUZZLE - 1) + " :"); 
            byte[] blocks = new byte[SIZE_PUZZLE];            
            boolean[] used = new boolean[SIZE_PUZZLE];
            int inputCount = 0;
            while (inputCount < SIZE_PUZZLE) {
                int value = scanner.nextInt();
                if (value < 0 || value >= SIZE_PUZZLE) {
                    System.out.println("Invalid number, try again.");
                } else if (used[value]) {
                    System.out.println(value + " already entered, try again.");
                } else {
                    blocks[inputCount++] = (byte) value;
                    used[value] = true;
                }
            }
            
            Board initial = new Board(blocks);
            if (initial.isSolvable()) {
                Stopwatch sw = new Stopwatch();
                Solver solver = new Solver(initial);
                System.out.println("Total Time " + sw.currentTime());
                solver.solutionSummary();
                
                if (solver.moves() > 0) {
                    System.out.println("If you want to view the details:");
                    System.out.print("Enter 1 for list of moves, 2 for display each move, ");
                    System.out.println("other number to skip.");
                    int value = scanner.nextInt();
                    if (value == 1) {
                        solver.solutionList();
                    } else if (value == 2) {
                        solver.solutionDetail();
                    } else {
                        System.out.println();
                    }
                } else {
                    System.out.println();
                }
            } else {
                System.out.println("No solution possible\n");
            }
            System.out.println("Ready to exit? (If not, it will prompt to "
                    + "enter another data set.)");
            char value = scanner.next().charAt(0);
            if (value == 'Y' || value == 'y') {
                System.out.println("Goodbye!");
                return;
            }
        } while (true);
    } 
}
