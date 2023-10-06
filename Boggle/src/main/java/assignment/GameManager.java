package assignment;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GameManager implements BoggleGame {
    private char [][] board;
    private ArrayList<Point> finalPath;
    private ArrayList<Point> lastSuccessfulPath;
    private int numPlayers;
    private int [] playerScore;
    private HashSet<String> wordList;
    private SearchTactic tactic;
    private BoggleDictionary dict;

    @Override
    public void newGame(int size, int numPlayers, String cubeFile, BoggleDictionary dict) throws IOException {

        if(size <= 0) {
            System.err.println("Grid size must be positive");
            return;
        }
        if(numPlayers <= 0) {
            System.err.println("Must be at least one player");
            return;
        }

        this.board = new char[size][size];
        this.numPlayers = numPlayers;
        this.playerScore = new int[numPlayers];
        this.wordList = new HashSet<>();
        this.tactic = SEARCH_DEFAULT;
        this.dict = dict;
        finalPath = new ArrayList<>();
        lastSuccessfulPath = new ArrayList<>();
        // use cubefile to populate the board with random cubes and letters
        char[] flatBoard = new char[size*size];

        Scanner sc = new Scanner(new File(cubeFile));
        for (int i = 0; i < size * size; i++) {
            if(!sc.hasNextLine()) {
                System.err.println("Not enough cubes in the cube File");
                return;
            }
            String cube = sc.nextLine();
            flatBoard[i] = cube.charAt((int) (Math.random() * cube.length()));
        }
        sc.close();

        for(int i=0;i<size*size;i++) {
            int s = (int)(Math.random() * (flatBoard.length - i)) + i;
            char temp = flatBoard[i];
            flatBoard[i] = flatBoard[s];
            flatBoard[s] = temp;
        }

        int index = 0;
        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)
                board[i][j] = Character.toLowerCase(flatBoard[index++]);
    }

    @Override
    public char[][] getBoard() {
        return board;
    }

    @Override
    public int addWord(String word, int player) {
        if(word == null || player < 0 || player >= numPlayers) return 0;
        if(word.length() < 4 || wordList.contains(word) || !dict.contains(word) || !isOnBoard(word)) return 0;

        lastSuccessfulPath = (ArrayList<Point>) finalPath.clone();
        wordList.add(word);
        playerScore[player] += word.length()-3;
        return word.length()-3;
    }

    @Override
    public void setGame(char[][] board) {

        int sz = board.length;
        for(char[] row : board) {
            if (row.length != sz) {
                System.err.println("Board must be square!");
                return;
            }
        }
        if(sz == 0) {
            System.err.println("Board must have positive dimensions");
            return;
        }

        this.board = board.clone();
        //sets all players score to zero
        Arrays.fill(playerScore, 0);
        wordList.clear();
        finalPath.clear();
        lastSuccessfulPath.clear();
        tactic = SEARCH_DEFAULT;
    }

    @Override
    public List<Point> getLastAddedWord() {
        return lastSuccessfulPath;
    }

    private void searchWordOnBoard(int x, int y, boolean[][] explored, String currWord, ArrayList<String> foundWords) {

        explored[x][y] = true;
        if(!foundWords.contains(currWord + board[x][y]) && dict.contains(currWord + board[x][y])) foundWords.add(currWord + board[x][y]);

        for(int dx = -1;dx <= 1; dx++)
            for (int dy = -1; dy <= 1; dy++)
                if(x + dx >= 0 && y + dy >= 0 && x + dx < board.length && y + dy < board[0].length
                        && !explored[x+dx][y+dy] && dict.isPrefix(currWord + board[x][y]))
                            searchWordOnBoard(x + dx, y + dy, explored, currWord + board[x][y], foundWords);

        explored[x][y] = false;

    }

    @Override
    public Collection<String> getAllWords() {
        ArrayList<String> allWords = new ArrayList<>();
        if(tactic == SearchTactic.SEARCH_BOARD) {
            for(int i=0;i<board.length;i++)
                for(int j=0;j<board[i].length;j++)
                    searchWordOnBoard(i, j, new boolean[board.length][board[0].length], "", allWords);
        }
        else { // SEARCH_DICTIONARY
            for(String word : dict)
                if(isOnBoard(word))
                    allWords.add(word);
        }
        return allWords;
    }
    @Override
    public void setSearchTactic(SearchTactic tactic) {
        this.tactic = tactic;
    }

    @Override
    public int[] getScores() {
        return playerScore;
    }

    boolean searchWordExists(int x, int y, boolean[][] explored, String word, int index, ArrayList<Point> path) {
        path.add(new Point(x, y));
        if(index == word.length()-1) return true;
        explored[x][y] = true;
        for(int dx = -1; dx <= 1; dx++) {
           for(int dy = -1; dy <= 1; dy++) {
               if(x+dx < board.length && x + dx >= 0 && y + dy < board[0].length && y + dy >= 0
                       && !explored[x+dx][y+dy] && board[x+dx][y+dy] == word.charAt(index+1)) {
                   if(searchWordExists(x+dx, y+dy, explored, word, index+1, path)) return true;
               }
           }
       }
        explored[x][y] = false;
       path.remove(path.size()-1);
       return false;
    }

    public boolean isOnBoard(String word) {
        if(word.length() < 1) return false;
        for(int i=0;i<board.length;i++) {
            for(int j=0;j<board[i].length;j++) {
                if(board[i][j] == word.charAt(0)) {
                    finalPath.clear();
                    if(searchWordExists(i, j, new boolean[board.length][board[0].length], word, 0, finalPath))
                        return true;
                }
            }
        }
        finalPath.clear();
        return false;
    }
    public int getNumPlayers(){
        return numPlayers;
    }

    public boolean wordUsed(String word) {
        return wordList.contains(word);
    }
}
