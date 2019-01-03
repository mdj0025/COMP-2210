import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;



public class Scrabble implements WordSearchGame {
   private NavigableSet<String> lexicon;
   private List<Integer> path;
   private List<Integer> actualPath;
   private int length;
   private String[][] board;
   private boolean[][] visited;
   private final int maxNeighbors = 8;
   
   
   private String wordSoFar; //Keeps track of word being built
   private ArrayList<Position> path2; //Path for all words. Like x,y better than row-major.
   String currentWord;
   int root;
   boolean lexiconLoaded;
   private SortedSet<String> allWords;
   private int[][] grid;
   private int order;
   private int minLength;
   /** This is the constructor for the Scrabble class.
    *
    */
   
   public Scrabble() {
      lexicon = new TreeSet<String>();
      path = new ArrayList<Integer>();
      allWords = new TreeSet<String>();
      actualPath = new ArrayList<Integer>();
      String[][] board = new String[][]{{"E", "E", "C", "A"},
                                             {"A", "L", "E", "P"},
                                             {"H", "N", "B", "O"},
                                             {"Q", "T", "T", "Y"}};
   }
   /**
    * Loads the lexicon into a data structure for later use. 
    * 
    * @param fileName A string containing the name of the file to be opened.
    * @throws IllegalArgumentException if fileName is null
    * @throws IllegalArgumentException if fileName cannot be opened.
    */
  
   public void loadLexicon(String fileName) {
      if (fileName == null) {
         throw new IllegalArgumentException();
      }
      
      lexicon = new TreeSet<String>();
      lexiconLoaded = false;
      try {
         Scanner s = 
            new Scanner(new BufferedReader(new FileReader(new File(fileName))));
         while (s.hasNext()) {
            String str = s.next();
            lexicon.add(str.toUpperCase());
            s.nextLine();
         }
         lexiconLoaded = true;
      }
      catch (Exception e) {
         throw new IllegalArgumentException();
      }
   }   
    
/**
    * Stores the incoming array of Strings in a data structure that will make
    * it convenient to find words.
    * 
    * @param letterArray This array of length N^2 stores the contents of the
    *     game board in row-major order. Thus, index 0 stores the contents of board
    *     position (0,0) and index length-1 stores the contents of board position
    *     (N-1,N-1). Note that the board must be square and that the strings inside
    *     may be longer than one character.
    * @throws IllegalArgumentException if letterArray is null, or is  not
    *     square.
    */
   public void setBoard(String[] letterArray) {
      if (letterArray == null) {
         throw new IllegalArgumentException();
      }
      
      if (Math.sqrt(letterArray.length) % 1 != 0) {
         throw new IllegalArgumentException();
      }
      
      int length = letterArray.length;
      root = (int)Math.sqrt(length);
      
      board = new String[root][root];
      int i = 0;
      int j = 0;
      for (String s: letterArray) {
         if (j == root) {
            i++;
            j = 0;
         }
         board[i][j] = s;
         j++; 
         
      }
      int height = root;
      int width = root;
   }
   
/**
    * Creates a String representation of the board, suitable for printing to
    *   standard out. Note that this method can always be called since
    *   implementing classes should have a default board.
    */
   public String getBoard() {
      String board2 = Arrays.deepToString(board);
      return board2;
   }
   
   /**
    * Retrieves all valid words on the game board, according to the stated game
    * rules.
    * 
    * @param minimumWordLength The minimum allowed length (i.e., number of
    *     characters) for any word found on the board.
    * @return java.util.SortedSet which contains all the words of minimum length
    *     found on the game board and in the lexicon.
    * @throws IllegalArgumentException if minimumWordLength < 1
    * @throws IllegalStateException if loadLexicon has not been called.
    */
   public SortedSet<String> getAllValidWords(int minimumWordLength) {
      if (!lexiconLoaded) {
         throw new IllegalStateException("Load Lexicon");
      }
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException("Invalid Number");
      }
      path2 = new ArrayList<Position>();
      allWords = new TreeSet<String>();
      currentWord = "";
      for (int i = 0; i < root; i++) {
         for (int j = 0; j < root; j++) {
            currentWord = board[i][j];
            
            if (isValidWord(currentWord) && currentWord.length() >= minimumWordLength) {
               allWords.add(currentWord);
            }
            
            if (isValidPrefix(currentWord)) {
               Position temp = new Position(i, j);
               path2.add(temp);
               depthFirstSearch(i, j, minimumWordLength);
               path2.remove(temp);
            }
         }
      }
      return allWords;
   }
   
  /**
   * Computes the cummulative score for the scorable words in the given set.
   * To be scorable, a word must (1) have at least the minimum number of characters,
   * (2) be in the lexicon, and (3) be on the board. Each scorable word is
   * awarded one point for the minimum number of characters, and one point for 
   * each character beyond the minimum number.
   *
   * @param words The set of words that are to be scored.
   * @param minimumWordLength The minimum number of characters required per word
   * @return the cummulative score of all scorable words in the set
   * @throws IllegalArgumentException if minimumWordLength < 1
   * @throws IllegalStateException if loadLexicon has not been called.
   */
   
   public int getScoreForWords(SortedSet<String> words, int minimumWordLength) {
      int score = 0;
      
      for (String s: words) {
         if (isValidWord(s) && (s.length() >= minimumWordLength) && (!isOnBoard(s).isEmpty())) {
            score += (1 + (s.length() - minimumWordLength));
         }
      }
      return score;
   }
  
   /**
    * Determines if the given word is in the lexicon.
    * 
    * @param wordToCheck The word to validate
    * @return true if wordToCheck appears in lexicon, false otherwise.
    * @throws IllegalArgumentException if wordToCheck is null.
    * @throws IllegalStateException if loadLexicon has not been called.
    */
   
   public boolean isValidWord(String wordToCheck) {
      if (!(lexiconLoaded)) {
         throw new IllegalStateException("Load lexicon");
      }
      
      if (wordToCheck == null) {
         throw new IllegalArgumentException("Invalid word"); 
      }
      wordToCheck = wordToCheck.toUpperCase();
      return lexicon.contains(wordToCheck);
   }
   /**
    * Determines if there is at least one word in the lexicon with the 
    * given prefix.
    * 
    * @param prefixToCheck The prefix to validate
    * @return true if prefixToCheck appears in lexicon, false otherwise.
    * @throws IllegalArgumentException if prefixToCheck is null.
    * @throws IllegalStateException if loadLexicon has not been called.
    */
   
   public boolean isValidPrefix(String prefixToCheck) {
      if (!lexiconLoaded) {
         throw new IllegalStateException("Load lexicon");
      }
      if (prefixToCheck == null) {
         throw new NoSuchElementException();
      }
      prefixToCheck = prefixToCheck.toUpperCase();
      String value = lexicon.ceiling(prefixToCheck);
      boolean isTrue = false;
      if (value != null) {
         return value.startsWith(prefixToCheck);
      }
      return isTrue;
   }
   /**
    * Determines if the given word is in on the game board. If so, it returns
    * the path that makes up the word.
    * @param wordToCheck The word to validate
    * @return java.util.List containing java.lang.Integer objects with  the path
    *     that makes up the word on the game board. If word is not on the game
    *     board, return an empty list. Positions on the board are numbered from zero
    *     top to bottom, left to right (i.e., in row-major order). Thus, on an NxN
    *     board, the upper left position is numbered 0 and the lower right position
    *     is numbered N^2 - 1.
    * @throws IllegalArgumentException if wordToCheck is null.
    * @throws IllegalStateException if loadLexicon has not been called.
    */
   
   public List<Integer> isOnBoard(String wordToCheck) {
      if (!lexiconLoaded) {
         throw new IllegalStateException("Load lexicon");
      }
   
      if (wordToCheck == null) {
         throw new IllegalArgumentException("Invalid word");
      }
      
      path2 = new ArrayList<Position>();
      wordToCheck = wordToCheck.toUpperCase();
      currentWord = "";
      path = new ArrayList<Integer>();
     
      for (int i = 0; i < root; i++) {
         for (int j = 0; j < root; j++) {
            if (wordToCheck.equals(board[i][j])) {
               path.add(i * root + j);
               return path;
            }
            
            if (wordToCheck.startsWith(board[i][j])) {
               Position pos = new Position(i, j);
               path2.add(pos);
               currentWord = board[i][j];
               depthFirstSearch2(i, j, wordToCheck);
               if (!wordToCheck.equals(currentWord)) {
                  path2.remove(pos);
               }
                  
               else {
                  for (Position p : path2) {
                     path.add((p.x * root) + p.y);
                  }
                  return path;
               }
            }
         }
      }
      return path;     
   }
   
   private void depthFirstSearch(int x, int y, int min) {
      Position start = new Position(x, y);
      markAllUnvisited();
      markPathVisited();
      
      for (Position p : start.neighbors()) {
         if (!isVisited(p)) {
            visit(p);
            
            if (isValidPrefix(currentWord + board[p.x][p.y])) {
               currentWord += board[p.x][p.y];
               path2.add(p);
               
               if (isValidWord(currentWord) && currentWord.length() >= min) {
                  allWords.add(currentWord);
               }
               depthFirstSearch(p.x, p.y, min);
               path2.remove(p);
               int endIndex = currentWord.length() - board[p.x][p.y].length();
               currentWord = currentWord.substring(0, endIndex);
            }
         }
         
         
      }
      markAllUnvisited();
      markPathVisited();
   }
   
   private void depthFirstSearch2(int x, int y, String wordToCheck) {
      Position start = new Position(x, y);
      markAllUnvisited();
      markPathVisited();
      
      for (Position p: start.neighbors()) {
         if (!isVisited(p)) {
            visit(p);
            
            if (wordToCheck.startsWith(currentWord + board[p.x][p.y])) {
               currentWord += board[p.x][p.y];
               path2.add(p);
               depthFirstSearch2(p.x, p.y, wordToCheck);
               
               if (wordToCheck.equals(currentWord)) {
                  return;
               }
               
               else {
                  path2.remove(p);
                  
                  int endIndex = currentWord.length() - board[p.x][p.y].length();
                  currentWord = currentWord.substring(0, endIndex);
               }
               
            }
         }
      }
      markAllUnvisited();
      markPathVisited();
      
   }
   
   private void markAllUnvisited() {
      visited = new boolean[root][root];
      for (boolean[] row : visited) {
         Arrays.fill(row, false);
      }
   }
   
   private void markPathVisited() {
      for (int i = 0; i < path2.size(); i++) {
         visit(path2.get(i));
      }
   }
   
   private class Position {
      int x; 
      int y;
      
      public Position(int x, int y) {
         this.x = x;
         this.y = y;
      }
      
      @Override
      public String toString() {
         return "(" + x + ", " + y + ")";
      }
      
      public Position[] neighbors() {
         Position[] neighbors = new Position[maxNeighbors];
         int count = 0; 
         Position p;
         
         for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
               if (!((i == 0) && (j == 0))) {
                  p = new Position(x + i, y + j);
                  
                  if (isValid(p)) {
                     neighbors[count++] = p;
                  }
               }
            }
         }
         return Arrays.copyOf(neighbors, count);
      }
   }
   
   private boolean isValid(Position p) {
      return (p.x >= 0 && (p.x < root) && (p.y >= 0) && (p.y < root));
   }
   
   private boolean isVisited(Position p) {
      return visited[p.x][p.y];
   }
   
   private void visit(Position p) {
      visited[p.x][p.y] = true;
   }
   
}
   
