package student.adventure;

import com.fasterxml.jackson.databind.ObjectMapper;
import student.server.AdventureException;
import student.server.AdventureService;
import student.server.Command;
import student.server.GameStatus;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DreamAdventureService implements AdventureService {
    private Map<Integer, Game> gameInstances;
    private final static String DATABASE_URL = "jdbc:sqlite:src/main/resources/adventure.db";
    private final static String TABLE_NAME = "leaderboard_ytp2";
    private static Connection dbConnection;

    public DreamAdventureService() {
        gameInstances = new HashMap<Integer, Game>();
        try {
            dbConnection = DriverManager.getConnection(DATABASE_URL);
            Statement stmt = dbConnection.createStatement();
            stmt.execute("CREATE TABLE " + TABLE_NAME + " ( name VARCHAR(50), score INTEGER )");
        } catch(Exception e) { }
    }

    @Override
    public void reset() {
        gameInstances.clear();
        try {
            Statement stmt = dbConnection.createStatement();
            String deleteAllRecords = "DELETE FROM " + TABLE_NAME;
            stmt.execute(deleteAllRecords);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public int newGame() throws AdventureException, IOException {
        File file = new File("src/main/resources/sleepDreamRooms.json");
        Game newGame = new ObjectMapper().readValue(file, Game.class);

        if (gameInstances.keySet().toArray().length == 0) {
            int instanceId = 1;
            newGame.initializeGame(instanceId);
            gameInstances.put(instanceId, newGame);
            return instanceId;
        } else {
            // Code below is derived from:
            // https://www.geeksforgeeks.org/stream-max-method-java-examples/
            Integer newInstanceId = gameInstances.keySet().stream().max(Integer::compare).get() + 1;
            newGame.initializeGame(newInstanceId);
            gameInstances.put(newInstanceId, newGame);
            return newInstanceId;
        }
    }

    @Override
    public GameStatus getGame(int id) {
        return gameInstances.get((Integer) id).getGameStatus();
    }

    @Override
    public boolean destroyGame(int id) {
        if (gameInstances.containsKey((Integer) id)) {
            gameInstances.remove((Integer) id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void executeCommand(int id, Command command) {
        Game game = gameInstances.get((Integer) id);
        game.input(command);
        if (game.getGameStatus().getCommandOptions().isEmpty()) {
            // game has finished
            try {
                Statement stmt = dbConnection.createStatement();
                String insertNewRecord = "INSERT INTO " + TABLE_NAME + " (name, score) VALUES ('" +
                        command.getPlayerName() + "', " + game.getPastRooms().size() + ")";
                stmt.execute(insertNewRecord);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public Map<String, Integer> fetchLeaderboard() {
        Map<String, Integer> leaderboard = new LinkedHashMap<String, Integer>();

        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, score FROM " + TABLE_NAME + " ORDER BY score ASC");
            // Code below is derived from:
            // https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html#retrieve_rs
            while (rs.next()) {
                leaderboard.put(rs.getString("name"), rs.getInt("score"));
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        return leaderboard;
    }
}
