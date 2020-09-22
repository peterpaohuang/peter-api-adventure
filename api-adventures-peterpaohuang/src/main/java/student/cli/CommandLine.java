package student.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import student.adventure.*;
import student.server.AdventureState;
import student.server.Command;
import student.server.GameStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A command line implementation to interface with Game class
 * Implements AdventureGameInterface for basic functionality scaffold
 * @author Peter Pao-Huang
 * @version 1.0
 */
public class CommandLine {
    private Game game;

    /**
     * Deserializes JSON to Game POJO
     * Checks for invalid Game POJO
     * @throws IOException when Game POJO is invalid
     */
    public CommandLine(String filepath) throws IOException {
        File file = new File(filepath);
        game = new ObjectMapper().readValue(file, Game.class);
        game.initializeGame(1);
    }

    public void play() throws IOException {
        printGameStatus();

        boolean shouldContinue = true;
        while (shouldContinue) {
            if (game.getGameStatus().getCommandOptions().isEmpty()) {
                shouldContinue = false;
                continue;
            }

            System.out.print("> ");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String userInput = reader.readLine();
            if (userInput.equals("exit") || userInput.equals("quit")) {
                shouldContinue = false;
            } else {
                Command command = parseInput(userInput);
                game.input(command);
                printGameStatus();
            }
        }
    }

    private void printGameStatus() {
        GameStatus status = game.getGameStatus();
        if (status.isError() || status.getCommandOptions().isEmpty()) {
            System.out.println(status.getMessage());
        } else {
            System.out.println(status.getMessage());
            System.out.println("Possible commands:");
            printDirections();
            printInventory();
            printItems();
        }
    }

    /**
     * Displays the items in the user's inventory
     */
    private void printInventory() {
        List<String> inventory = game.getGameStatus().getCommandOptions().get(Command.DROP);

        for (int i = 0; i < inventory.size(); i++) {
            if (i == 0) {
                System.out.print("drop ");
            }

            System.out.print(inventory.get(i));

            if (i != inventory.size() - 1) {
                System.out.print(", ");
            } else {
                System.out.println();
            }
        }
    }

    /**
     * Displays items available in the current room
     */
    private void printItems() {
        List<String> itemsAvailable = game.getGameStatus().getCommandOptions().get(Command.TAKE);

        for (int i = 0; i  < itemsAvailable.size(); i++) {
            if (i == 0) {
                System.out.print("take " );
            }

            System.out.print(itemsAvailable.get(i));

            if (i != itemsAvailable.size() - 1) {
                System.out.print(", ");
            } else {
                System.out.println();
            }
        }
    }

    /**
     * Display possible directions in current room
     */
    private void printDirections() {
        List<String> directionsAvailable = game.getGameStatus().getCommandOptions().get(Command.GO);

        for (int i = 0; i < directionsAvailable.size(); i++) {
            if (i == 0) {
                System.out.print("go ");
            }

            System.out.print(directionsAvailable.get(i));

            if (i != directionsAvailable.size() - 1) {
                System.out.print(", ");
            } else {
                System.out.println();
            }
        }
    }

    /**
     * Parses raw string input from command line to standard, usable tuple of (command, action)
     * @param userInput raw string input from command line
     * @return array size two of parsed strings in form (command, action)
     */
    public Command parseInput(String userInput) {
        // code below is derived from:
        // https://stackoverflow.com/questions/2932392/
        String cleanedInput = userInput.trim().replaceAll(" +", " ").toLowerCase();
        String[] separatedInput = cleanedInput.split(" ");
        String commandName = separatedInput[0];

        // code below is derived from:
        // https://stackoverflow.com/questions/7899525/
        String commandValue = "";
        if (separatedInput.length > 1) {
            String[] separatedAction = Arrays.copyOfRange(separatedInput, 1, separatedInput.length);
            commandValue = String.join(" ", separatedAction);
        }

        Command command = new Command(commandName, commandValue);

        return command;
    }
}
