package assignment;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
public class DictionaryTest {
    private GameManager game;
    private GameDictionary myDict;
    private String cubeFile;
    private TrieNode testNode;
    private  TrieNode parentNode;
    private GameDictionary.Iter myIter;

    @Before
    public void constructor() throws IOException {
        game = new GameManager();
        myDict = new GameDictionary();
        cubeFile = "cubes.txt";
    }
    @Test
    public void testLoadDictionary() throws IOException {
        //we create a custom board with a limited dictionary and ensure the word is not in the dictinoary
        myDict.loadDictionary("testDictionary.txt");
        game.newGame(4, 2, cubeFile, myDict);
        //although node is on the board, it is not in the dictionary so no points should be given
        char[][] newBoard = {
                {'z', 'n', 'z', 'z'},
                {'z', 'o', 'z', 'z'},
                {'z', 'd', 'z', 'z'},
                {'z', 'e', 'z', 'z'}
        };
        game.setGame(newBoard);
        assertEquals(game.addWord("node", 0), 0);

        myDict.loadDictionary("words.txt");
        game.newGame(4,2, cubeFile, myDict);
        char[][] board = {
                {'z', 'n', 'z', 'z'},
                {'t', 'o', 'n', 'e'},
                {'z', 'd', 'z', 's'},
                {'z', 'e', 'z', 'z'}
        };
        game.setGame(board);
        assertEquals(game.addWord("node", 0), 1);
        assertEquals(game.addWord("tone", 0), 1);
        assertEquals(game.addWord("tones", 0), 2);

        myDict.loadDictionary("words.txt");
        game.newGame(4,2, cubeFile, myDict);
        char[][] goodBoard = {
                {'t', 'e', 'm', 'p'},
                {'m', 'a', 'e', 'r'},
                {'i', 'n', 'l', 's'},
                {'t', 'e', 's', 't'}
        };
        game.setGame(goodBoard);
        assertEquals(game.addWord("temper", 0), 3);
        assertEquals(game.addWord("mines", 0), 1);
        assertEquals(game.addWord("meals", 0), 2);
    }
    @Test
    public void testIsPrefix(){
        String [] randomPrefixes = {"re", "dis", "de", "ex", "in", "inter", "post", "tele", "tri", "up"};
        for(int i=0;i<randomPrefixes.length;i++){
            assertEquals(myDict.isPrefix(randomPrefixes[i]), true);
        }
        String [] randomInvalidPrefixes = {"mx", "sop", "sa", "lm", "jw", "km", "pl", "me", "ti", "pu"};
        for(int i=0;i<randomInvalidPrefixes.length;i++){
            assertEquals(myDict.isPrefix(randomInvalidPrefixes[i]), false);
        }
        String [] randomPrefixes2 = {"intra", "de", "pre", "sub", "uni"};
        for(int i=0;i<randomPrefixes2.length;i++){
            assertEquals(myDict.isPrefix(randomPrefixes2[i]), true);
        }
        String [] randomInvalidPrefixes2 = {"nit", "mops", "steer", "pullup", "la"};
        for(int i=0;i<randomInvalidPrefixes2.length;i++){
            assertEquals(myDict.isPrefix(randomInvalidPrefixes2[i]), false);
        }
    }
    @Test
    public void testContains(){
        String [] validWords = {"sunny", "water", "light", "enter", "tone"};
        for(int i=0;i<validWords.length;i++){
            assertTrue(myDict.contains(validWords[i]));
        }
        String [] nonValidWords = {"job", "hsbg", "8932", "njs*&", "caml"};
        for(int j=0;j<nonValidWords.length;j++){
            assertFalse(myDict.contains(nonValidWords[j]));
        }
        String [] validWords2 = {"pond", "slap", "emphasis", "telephone", "subpar"};
        for(int i=0;i<validWords2.length;i++){
            assertTrue(myDict.contains(validWords2[i]));
        }
        String [] nonValidWords2 = {"cat", "sle", "8764", "*^%$#@*&", "sun"};
        for(int j=0;j<nonValidWords2.length;j++){
            assertFalse(myDict.contains(nonValidWords2[j]));
        }
    }
    @Test
    public void testIterator(){
        assertEquals(myIter.constructString(testNode), "string");
        assertEquals(myIter.constructString(testNode), "four");
        assertEquals(myIter.constructString(testNode), "water");
        assertEquals(myIter.nextChild(testNode), parentNode);
        assertEquals(myIter.hasNext(), true);
        assertEquals(myIter.next(), "parent");
    }
}
