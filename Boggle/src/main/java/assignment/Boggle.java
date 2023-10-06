package assignment;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Boggle {
    public static boolean allFalse(ArrayList<Boolean> arr) {
        //determines if all players have ended their turn yet
        for (boolean x : arr) if (x) return false;
        return true;
    }

    private static void printPoints(GameManager game) {
        System.out.println("Points scored: ");
        for (int i = 0; i < game.getNumPlayers(); i++)
            System.out.print("Player " + i + ": " + game.getScores()[i] + ", ");
        System.out.println("\n\n");
    }

    public static void printBoard(GameManager game, boolean correctWord) {
        for (int i = 0; i < game.getBoard().length; i++) {
            for (int j = 0; j < game.getBoard()[i].length; j++) {
                if (correctWord && game.getLastAddedWord().contains(new Point(i, j)))
                    System.out.print(Character.toUpperCase(game.getBoard()[i][j]) + " ");
                else
                    System.out.print(game.getBoard()[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {

        String wantsToExit;
        do {
            GameManager game = new GameManager();
            GameDictionary dictionary = new GameDictionary();
            dictionary.loadDictionary("words.txt");
            final int size = 4;
            final int numPlayers;

            Scanner sc = new Scanner(System.in);
            System.out.print("Please enter number of players: ");
            numPlayers = Integer.parseInt(sc.nextLine());

            game.newGame(size, numPlayers, "cubes.txt", dictionary);

            //all players start with a turn
            //HashMap<Integer, Boolean> playerStatus = new HashMap<Integer, Boolean>();
            ArrayList<Boolean> playerStatus = new ArrayList<>();
            for (int i = 0; i < numPlayers; i++) playerStatus.add(true);

            boolean correctWord = false, validInput;
            while (!allFalse(playerStatus)) {
                for (int player = 0; player < numPlayers; player++) {

                    if (!playerStatus.get(player)) continue;
                    printPoints(game);

                    do {
                        printBoard(game, correctWord);
                        //prompts player to enter word or end turn
                        System.out.print("\nPlayer " + player + ", Enter word or *** to end turn:  ");
                        String inputWord = sc.nextLine();
                        inputWord = inputWord.toLowerCase();
                        if (inputWord.equals("***")) {
                            playerStatus.set(player, false);
                            validInput = true;
                            correctWord = false;
                            continue;
                        }
                        int score = game.addWord(inputWord, player);
                        if (score > 0) {
                            validInput = true;
                            correctWord = true;
                            System.out.println(score + " points added to player " + player);
                        } else {
                            validInput = false;
                            correctWord = false;
                            if (game.wordUsed(inputWord)) System.out.println("Word was already found, try again");
                            else if (inputWord.length() <= 3)
                                System.out.println("Word must be at least 4 letters, try again.");
                            else if (!game.isOnBoard(inputWord))
                                System.out.println("Word is not on the board, try again.");
                            else if (!dictionary.contains(inputWord))
                                System.out.println("Word is not in dictionary, try again.");
                        }
                    } while (!validInput);
                }
            }

            System.out.println("\n\n\nComputer playing...");
            int computerScore = 0;
            for (String word : game.getAllWords()) {
                if (word.length() > 3 && !game.wordUsed(word)) {
                    System.out.print(word + "\t");
                    computerScore += word.length() - 3;
                }
            }
            // print final
            System.out.println("\n\n\nFinal point count: ");
            printPoints(game);
            System.out.println("Computer: " + computerScore + "\n\n");

            System.out.print("Do you want to play again? Enter y for yes, or anything else for no: ");
            wantsToExit = sc.nextLine();


        } while (wantsToExit.equals("y"));
    }
}
