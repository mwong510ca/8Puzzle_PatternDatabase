package mwong.myprojects.eightpuzzle;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

import mwong.myprojects.utilities.Stopwatch;

/****************************************************************************
 *  @author   Meisze Wong
 *            www.linkedin.com/pub/macy-wong/46/550/37b/
 *
 *  Compilation: javac PD8Combo.java
 *  Execution:   java PD8Combo
 *  Dependencies: Stopwatch.java, Direction.java
 *
 *  A immutable data type of all combinations of 8puzzle pattern database
 *
 ****************************************************************************/

public class PatternDatabase {
    private static final String DIRECTORY = "database";
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String FILEPATH = DIRECTORY + SEPARATOR + "pd8.db";
    private static final int SIZE_COMBO = 181440;
    private static final int[] PARTIAL = {0, 0x07, 0x003F, 0x01FF, 0x0FFF, 0x007FFF};       
    
    private static HashMap<Integer, Integer> keyMap; 
    private static byte[] initCombo = {1, 2, 3, 4, 5, 6, 7, 8, 0};
    // compress the 8puzzle into a 32 bit key
    // use the key index link to 4 direction and number of move from initial board 
    private static int[] keys; 
    private static int[][] links; 
    private static byte[] patternValue; 
    private static boolean ready2use = false;
       
    /**
     * Initializes the PD8Combo object.
     */
    public PatternDatabase() {
        if (ready2use) {
            return;
        }
              
        // if database file exists, load from file
        // otherwise re-generate and save in file
        try {
            Stopwatch stopwatch = new Stopwatch();
            loadFile();
            System.out.println("Pattern Database 8puzzle - load from data file successed : " 
                    + stopwatch.currentTime() + "s");
        } catch (Exception ex) {
            (new File(FILEPATH)).delete();
            genPattern();
            ready2use = true;
            try {
                Stopwatch stopwatch = new Stopwatch();
                saveFile();
                System.out.println("Pattern Database 8puzzle - save data set in file successed : " 
                        + stopwatch.currentTime() + "s");       
            } catch (Exception ex2) {
                if ((new File(FILEPATH)).exists()) {
                    (new File(FILEPATH)).delete();
                }
            }
        }    
    }
       
    // load the database pattern from file
    private void loadFile() throws Exception { 
        keyMap = new HashMap<Integer, Integer>(); 
        keys = new int[SIZE_COMBO];
        links = new int[SIZE_COMBO][4];
        patternValue = new byte[SIZE_COMBO];
        
        InputStream is = new FileInputStream(FILEPATH);
        DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
        dis.read(patternValue);
        for (int i = 0; i < SIZE_COMBO; i++) {
            keys[i] = dis.readInt();
            keyMap.put(keys[i], i);
            links[i][Direction.RIGHT.getValue()] = dis.readInt();
            links[i][Direction.DOWN.getValue()] = dis.readInt();
            links[i][Direction.LEFT.getValue()] = dis.readInt();
            links[i][Direction.UP.getValue()] = dis.readInt();
        }                                                     
        dis.close();
    }
    
    // save the database pattern in file
    private void saveFile() throws Exception {
        if (!(new File(DIRECTORY)).exists()) {
            (new File(DIRECTORY)).mkdir();
        }
        if ((new File(FILEPATH)).exists()) {
            (new File(FILEPATH)).delete();
        }
        FileOutputStream fos = new FileOutputStream(FILEPATH);
        DataOutputStream dos = new DataOutputStream(fos);
        dos.write(patternValue);
        dos.flush();
        for (int i = 0; i < SIZE_COMBO; i++) {
            dos.writeInt(keys[i]);
            dos.writeInt(links[i][Direction.RIGHT.getValue()]);
            dos.writeInt(links[i][Direction.DOWN.getValue()]);
            dos.writeInt(links[i][Direction.LEFT.getValue()]);
            dos.writeInt(links[i][Direction.UP.getValue()]);
            dos.flush();
        }
        dos.close();
    }
       
    // compress the tiles combo with space in to 32 bits key
    private int combo2key(byte[] combo) {
        int zeroPos = 0;
        int key = 0;
              
        for (int i = 0; i < 9; i++) {
            if (combo[i] == 0) {
                zeroPos = i;
            } else {
                key = (key << 3) | (combo[i] - 1);
            }
        }
        return (key << 4) | zeroPos;
    }
       
    /**
     *  Returns the set of 8 tiles of the puzzle with space.
     *  
     *  @param idx the given key index of pattern
     *  @return a integer array of 8 tiles of puzzle with space
     */
    protected int[] idx2combo(int idx) {
        int zeroPos = keys[idx] & 0x000F;
        int key = keys[idx] >> 4;
        
        int[] combo = {-1, -1, -1, -1, -1, -1, -1, -1, -1};
        combo[zeroPos] = 0;
        for (int i = 8; i >= 0; i--) {
            if (combo[i] == -1) {
                combo[i] = (key & PARTIAL[1]) + 1;
                key = key >> 3;
            }
        }
        return combo;
    }
       
    /**
     *  Returns the index of compress pattern.
     *  
     *  @param combo the given sequence of 8 tiles with space
     *  @return index of compress pattern, -1 if no such pattern
     */
    protected int getKeyIndex(byte[] combo) {
        int key = combo2key(combo);
        if (keyMap.containsKey(key)) {
            return keyMap.get(key);
        } else {
            return -1;
        }
    }
       
    /**
     *  Returns the index of pattern after the move.
     *  
     *  @param index the given index of the pattern
     *  @param dir the given direction of the move
     *  @return index of pattern after the move
     */
    protected int getLink(int index, Direction dir) {
        return links[index][dir.getValue()];
    }
              
    /**
     *  Returns the pattern value of the index.
     *  
     *  @param index the given index of the pattern
     *  @return number of the pattern value of the index
     */
    protected int getPDvalue(int index) {
        return patternValue[index];
    }
       
    /**
     *  Generate the 8puzzle pattern database.
     */
    protected void genPattern() {
        keyMap = new HashMap<Integer, Integer>(); 
        keys = new int[SIZE_COMBO];
        links = new int[SIZE_COMBO][4];
        for (int i = 0; i < SIZE_COMBO; i++) {
            Arrays.fill(links[i], -1);
        }
        patternValue = new byte[SIZE_COMBO];
              
        int initKey = combo2key(initCombo);
        int ctKeyIdx = 0;
        keys[ctKeyIdx] = initKey;
        keyMap.put(initKey, ctKeyIdx++);
              
        byte moves = 0;
              
        patternValue[0] = 0;
        int top = 0, top2 = 0, end = 1, end2 = 1;
        System.out.println("Screen full pattern : 1 2 3");
        System.out.println("                      4 5 6");
        System.out.println("                      7 8 0");
        System.out.print("moves : 0 \tcount : " + ctKeyIdx + "\tscanned : 1 \t end at : ");
        Stopwatch stopwatch = new Stopwatch();
        System.out.println(stopwatch.currentTime());
        while (true) {
            moves++;
            top = top2;
            end = end2;
            top2 = end2;
                     
            for (int i = top; i < end; i++) { 
                int zero = keys[i] & 0x000F;
                int key = keys[i] >> 4;
            
                // space Right, tile left
                if (zero % 3 < 2) {
                    int nextKey = (key << 4) | (zero + 1);
                    if (keyMap.containsKey(nextKey)) {
                        links[i][Direction.RIGHT.getValue()] = keyMap.get(nextKey);
                    } else {
                        int nextKeyIdx = ctKeyIdx++;
                        keys[nextKeyIdx] = nextKey;
                        keyMap.put(nextKey, nextKeyIdx);
                        links[i][Direction.RIGHT.getValue()] = nextKeyIdx;
                        patternValue[nextKeyIdx] = moves;
                        end2++;
                    }
                }
                                   
                // space down, tile up
                if (zero < 6) {
                    int base = key >> ((8 - zero) * 3);
                    int leftover = key & PARTIAL[5 - zero];
                    int self = (key >> ((5 - zero) * 3)) & PARTIAL[1];
                    int rotKey = (key >> ((6 - zero) * 3)) & PARTIAL[2];
                    
                    int nextKey = (((((((base << 3) | self) << 6) | rotKey) << ((5 - zero) * 3)) 
                            | leftover) << 4) | (zero + 3);
                    if (keyMap.containsKey(nextKey)) {
                        links[i][Direction.DOWN.getValue()] = keyMap.get(nextKey);
                    } else {
                        int nextKeyIdx = ctKeyIdx++;
                        keys[nextKeyIdx] = nextKey;
                        keyMap.put(nextKey, nextKeyIdx);
                        links[i][Direction.DOWN.getValue()] = nextKeyIdx;
                        patternValue[nextKeyIdx] = moves;
                        end2++;
                    }
                }
                                   
                // space left, tile right
                if (zero % 3 > 0) {
                    int nextKey = (key << 4) | (zero - 1);
                    if (keyMap.containsKey(nextKey)) {
                        links[i][Direction.LEFT.getValue()] = keyMap.get(nextKey);
                    } else {
                        int nextKeyIdx = ctKeyIdx++;
                        keys[nextKeyIdx] = nextKey;
                        keyMap.put(nextKey, nextKeyIdx);
                        links[i][Direction.LEFT.getValue()] = nextKeyIdx;
                        patternValue[nextKeyIdx] = moves;
                        end2++;
                    }                            
                }
                                   
                // space up, tile down 
                if (zero > 2) {
                    int base = key >> ((11 - zero) * 3);
                    int leftover = key & PARTIAL[8 - zero];
                    int self = (key >> ((10 - zero) * 3)) & PARTIAL[1];
                    int rotKey = (key >> ((8 - zero) * 3)) & PARTIAL[2];
                                          
                    int nextKey = (((((((base << 6) | rotKey) << 3) | self) << ((8 - zero) * 3))
                            | leftover) << 4) | (zero - 3);
                    if (keyMap.containsKey(nextKey)) {
                        links[i][Direction.UP.getValue()] = keyMap.get(nextKey);
                    } else {
                        int nextKeyIdx = ctKeyIdx++;
                        keys[nextKeyIdx] = nextKey;
                        keyMap.put(nextKey, nextKeyIdx);
                        links[i][Direction.UP.getValue()] = nextKeyIdx;
                        patternValue[nextKeyIdx] = moves;
                        end2++;
                    }
                }
            }
            if ((end2 ^ top2) != 0) {
                System.out.println("moves : " + moves + "\tcount : " + (end2 - top2) 
                        + "\tscanned : " + ctKeyIdx + "\t end at : " + stopwatch.currentTime());
            } else {
                System.out.println();
                break;
            }
        }
        System.out.println("PD8Combo - generate data set completed");       
    }
       
    /**
     *  test client create the 8puzzle pattern database.
     *  
     *  @param args main function standard arguments
     */
    public static void main(String[] args) {
        PatternDatabase pd = new PatternDatabase();
        System.out.println(pd.getPDvalue(0));
    }
}
