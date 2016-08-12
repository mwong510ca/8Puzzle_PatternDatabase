Beyond the lecture of the priority queue, A* algorithm with manhattan heuristic is not able to solve all boards of 15 slide puzzle due to out of memory.  Pattern database seems the best solution for it.  Here is the demonstrate version of pattern database on 8 slide puzzle. 

Since the pattern set for 8 slide puzzle is not very large.  I build the full pattern and solve the puzzle straight forward.

Deque.java - a data type of double-ended queue

RandomizedQueue.java - a data type of queue and access item in random order

Stopwatch.java - a data type of stop watch with start, stop and reset features.

Direction.java - enum class of direction of 4 moves for slide puzzle

PatternDatabase.java - a data type of 8 puzzle pattern database generator
Start from the goal state, move a step at a time until it access of possible moves to generate the pattern database.  It save in a data file for future use.
                                                Time
Generate the full pattern                       0.268s
Save the data file with pattern generation      6.321s
Load the pattern database from a data file      0.213s

Board.java:  The data type for 8 Puzzle using pattern database.
It cache the pattern database value of minimum moves from goal state, generate the board of best moves and neighbor boards.

Solver.java
A 8puzzle solution solver to solve 8 puzzle using pattern database with test client.

