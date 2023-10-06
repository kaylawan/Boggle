
package assignment;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

// compile
// javac -cp /Users/kaylawan/.m2/repository/junit/junit/4.13.1/junit-4.13.1.jar:/Users/kaylawan/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:group12-Boggle.jar src/test/java/assignment/GameTest.java
//run
// java -cp /Users/kaylawan/.m2/repository/junit/junit/4.13.1/junit-4.13.1.jar:/Users/kaylawan/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:group12-Boggle.jar src/test/java/assignment/GameTest.java

public class GameTest {
    private GameManager game;
    private BoggleDictionary myDict;
    private int boardSize;
    private int numPlayers;
    private String cubeFile;

    @Before
    public void constructor() throws IOException {
        game = new GameManager();
        myDict = new GameDictionary();
        myDict.loadDictionary("words.txt");
        boardSize = 4;
        numPlayers = 2;
        cubeFile = "cubes.txt";
    }
    @Test
    public void testNewGame() throws IOException {
        game.newGame(boardSize, numPlayers, "customCubes.txt", myDict);
        String myCubes = "SADFMLPUWIHCJGRV";
        char [][] board = game.getBoard();
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                assertTrue(myCubes.toLowerCase().contains(""+board[i][j]));
            }
        }

        game.newGame(boardSize, numPlayers, cubeFile, myDict);
        String originalCubes = "";

        Scanner sc = new Scanner(new File(cubeFile));
        for (int i = 0; i < boardSize * boardSize; i++) {
            if(!sc.hasNextLine()) {
                System.err.println("Not enough cubes in the cube File");
                return;
            }
            String cube = sc.nextLine();
            originalCubes+=cube;
        }
        sc.close();

        char [][] randomBoard = game.getBoard();
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                assertTrue(originalCubes.toLowerCase().contains(""+randomBoard[i][j]));
            }
        }
    }
    @Test
    public void testGetBoard() throws IOException {
        game.newGame(boardSize, numPlayers, cubeFile, myDict);
        char [][] board = game.getBoard();
        assertEquals(board.length, boardSize);
        assertEquals(board[0].length, boardSize);
        //tests against a custom board
        char[][] newBoard = {
                {'t', 'e', 'm', 'p'},
                {'e', 'f', 'e', 'r'},
                {'i', 'j', 's', 's'},
                {'t', 'e', 's', 't'}
        };
        game.setGame(newBoard);
        assertArrayEquals(game.getBoard(), newBoard);
        char[][] board1 = {
                {'z', 'z', 'z', 'z'},
                {'z', 'z', 'z', 'z'},
                {'z', 'z', 'z', 'z'},
                {'z', 'z', 'z', 'z'}
        };
        game.setGame(board1);
        assertArrayEquals(game.getBoard(), board1);
        assertNotEquals(game.getBoard(), newBoard);
    }
    @Test
    public void testAddWord() throws IOException {
        game.newGame(boardSize, numPlayers, cubeFile, myDict);
        char[][] newBoard = {
                {'t', 'e', 'm', 'p'},
                {'e', 'f', 'e', 'r'},
                {'i', 'j', 's', 's'},
                {'t', 'e', 's', 't'}
        };
        game.setGame(newBoard);
        int testValue = game.addWord("test", 0);
        assertEquals(testValue, 1);
        int testsValue = game.addWord("tests", 0);
        assertEquals(testsValue, 2);
        int temperValue = game.addWord("temper", 0);
        assertEquals(temperValue, 3);
        int notOnBoard = game.addWord("listen", 0);
        assertEquals(notOnBoard, 0);
        int tooShort = game.addWord("set", 0);
        assertEquals(tooShort, 0);
    }
    @Test
    public void testGetLastAddedWord() throws IOException {
        game.newGame(boardSize, numPlayers, cubeFile, myDict);
        //no last added word because word is too short
        char[][] newBoard = {
                {'t', 'e', 'm', 'p'},
                {'m', 'a', 'e', 'r'},
                {'i', 'n', 'l', 's'},
                {'t', 'e', 's', 't'}
        };
        game.setGame(newBoard);

        game.addWord("not", 0);
        ArrayList<Point> tooShort = new ArrayList<>();
        assertArrayEquals(game.getLastAddedWord().toArray(), tooShort.toArray());

        game.addWord("test", 0);
        ArrayList<Point> testPoints = (ArrayList<Point>) game.getLastAddedWord();
        ArrayList<Point> expectedPoints = new ArrayList<>();
        expectedPoints.add(new Point(3, 0));
        expectedPoints.add(new Point(3, 1));
        expectedPoints.add(new Point(3, 2));
        expectedPoints.add(new Point(3, 3));
        assertArrayEquals(expectedPoints.toArray(), testPoints.toArray());

        //when a word not on the board is attempted to be added, the most recent last word will remain
        game.addWord("stink", 0);
        ArrayList<Point> notOnBoard = new ArrayList<>();
        assertArrayEquals(game.getLastAddedWord().toArray(), expectedPoints.toArray());
    }
    @Test
    public void testSetGame() throws IOException {
        game.newGame(boardSize, numPlayers, cubeFile, myDict);
        char[][] newBoard = {
                {'t', 'e', 'm', 'p'},
                {'m', 'a', 'e', 'r'},
                {'i', 'n', 'l', 's'},
                {'t', 'e', 's', 't'}
        };
        game.setGame(newBoard);
        assertArrayEquals(game.getBoard(), newBoard);
        char[][] xBoard = {
                {'x', 'x', 'x', 'x'},
                {'x', 'x', 'x', 'x'},
                {'x', 'x', 'x', 'x'},
                {'x', 'x', 'x', 'x'}
        };
        game.setGame(xBoard);
        assertArrayEquals(game.getBoard(), xBoard);
    }
    @Test
    public void testGetAllWords() throws IOException {
        game.newGame(boardSize, numPlayers, cubeFile, myDict);
        char[][] newBoard = {
                {'z', 'z', 'z', 'z'},
                {'z', 'e', 'z', 'z'},
                {'z', 's', 'z', 'z'},
                {'z', 't', 'z', 'z'}
        };
        game.setGame(newBoard);
        Collection<String> myWords = new ArrayList<>();
        myWords.add("zest");
        myWords.add("es");
        assertArrayEquals(game.getAllWords().toArray(), myWords.toArray());

        char[][] zBoard = {
                {'z', 'z', 'z', 'z'},
                {'z', 'z', 'z', 'z'},
                {'z', 'z', 'z', 'z'},
                {'z', 'z', 'z', 'z'}
        };
        game.setGame(zBoard);
        Collection<String> noWords = new ArrayList<>();
        assertArrayEquals(game.getAllWords().toArray(), noWords.toArray());
    }
    @Test
    public void testGetScores() throws IOException {
        game.newGame(boardSize, numPlayers, cubeFile, myDict);
        char[][] newBoard = {
                {'t', 'e', 'm', 'p'},
                {'m', 'a', 'e', 'r'},
                {'i', 'n', 'l', 's'},
                {'t', 'e', 's', 't'}
        };
        game.setGame(newBoard);
        game.addWord("test", 0);
        game.addWord("tests", 1);
        int[] myScores = {1, 2};
        assertArrayEquals(game.getScores(), myScores);
    }
}
