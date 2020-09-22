package student.adventure;

import org.junit.Before;
import org.junit.Test;
import student.server.AdventureException;
import student.server.AdventureService;
import student.server.Command;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AdventureServiceTest {
    /*
    * TESTING STRATEGY
    *
    * Test reset method with no game instances !
    * Test reset method with arbitrary number of game instances !
    *
    * Test newGame method with no game instances
    *
    * Test getGame with invalid instance id
    * Test getGame with valid instance id
    *
    * Test destroyGame with invalid instance id
    * Test destroyGame with valid instance id
    *
    * Test executeCommand with invalid instance id
    * Test executeCommand with valid instance id but null command
    * Test executeCommand with valid instance id but empty command attributes
    * Test executeCommand with valid instance id but invalid commandName
    * Test executeCommand with valid instance id and 'GO' commandName but invalid direction
    * Test executeCommand with valid instance id and 'TAKE' commandName but invalid item
    * Test executeCommand with valid instance id and 'DROP' commandName but invalid item
    * Test executeCommand with valid instance id and 'EXAMINE' commandName but unnecessary non-empty commandValue
    * Test executeCommand with valid instance id and 'HISTORY' commandName but unnecessary non-empty commandValue
    * Test executeCommand with valid instance id and 'GO' commandName and valid direction
    * Test executeCommand with valid instance id and 'TAKE' commandName and valid item
    * Test executeCommand with valid instance id and 'DROP' commandName and valid item
    * Test executeCommand with valid instance id and 'EXAMINE' commandName and null commandValue
    * Test executeCommand with valid instance id and 'HISTORY' commandName and null commandValue
    * Test executeCommand with move to winning room
    *
    * Test fetchLeaderboard with no rows in table
    * Test fetchLeaderboard with arbitrary records in ascending order (TEST MANUALLY)
    */

    AdventureService service;

    @Before
    public void init() {
        service = new DreamAdventureService();
    }

    @Test(expected = NullPointerException.class)
    public void testResetWithNoGameInstances() {
        service.reset();
        service.getGame(1);
    }
    @Test(expected = NullPointerException.class)
    public void testResetWithArbitraryNumGameInstances() throws AdventureException, IOException {
        service.newGame();
        service.newGame();
        service.reset();
        service.getGame(1);
    }

    @Test
    public void testNewGameWithNoGameInstances() throws AdventureException, IOException {
        service.newGame();
        assertEquals(service.getGame(1).getMessage(),
                "You are comfortably asleep in a dorm room in Florida Avenue Residences");
    }

    @Test(expected = NullPointerException.class)
    public void testGetGameWithInvalidInstanceId() {
        assertEquals(service.getGame(1), null);
    }
    @Test
    public void testGetGameWithValidInstanceId() throws AdventureException, IOException {
        service.newGame();
        assertEquals(service.getGame(1).getMessage(),
                "You are comfortably asleep in a dorm room in Florida Avenue Residences");
    }

    @Test
    public void testDestroyGameWithInvalidInstanceId() {
        assertEquals(service.destroyGame(1),false);
    }
    @Test
    public void testDestroyGameWithValidInstanceId() throws AdventureException, IOException {
        service.newGame();
        assertEquals(service.destroyGame(1),true);
    }

    @Test(expected = NullPointerException.class)
    public void testExecuteCommandWithInvalidInstanceId() {
        service.executeCommand(1, null);
    }
    @Test(expected = NullPointerException.class)
    public void testExecuteCommandWithValidInstanceIdNullCommand() throws AdventureException, IOException {
        service.newGame();
        service.executeCommand(1, null);
    }
    @Test
    public void testExecuteCommandWithValidInstanceIdEmptyCommandAttributes() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("GO", "");
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(),"I can't understand 'GO '!");
    }
    @Test
    public void testExecuteCommandWithValidInstanceIdInvalidCommandName() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("BLAHHH", "DSKLFJSEOFIJSD");
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(),"I can't understand 'BLAHHH DSKLFJSEOFIJSD'!");
    }
    @Test
    public void testExecuteCommandWithGoCommandInvalidDirection() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("GO", "NORTH");
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(),"I can't go 'NORTH'!");
    }
    @Test
    public void testExecuteCommandWithTakeCommandInvalidItem() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("TAKE", "WORLD");
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(),"There is no 'WORLD' in the room!");
    }
    @Test
    public void testExecuteCommandWithDropCommandInvalidItem() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("DROP", "WORLD");
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(),"You don't have 'WORLD'!");
    }
    @Test
    public void testExecuteCommandWithExamineCommandUnnecessaryCommandValue() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("EXAMINE", "");
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(),
                "You are comfortably asleep in a dorm room in Florida Avenue Residences");
    }
    @Test
    public void testExecuteCommandWithHistoryCommandUnnecessaryCommandValue() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("HISTORY", "");
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(),"Your past rooms: Florida Avenue Residences");
    }
    @Test
    public void testExecuteCommandWithGoCommandValidDirection() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("GO", "WEST");
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(),
                "It's 2016. You are sprinting across a dark parking lot as clowns with red noses chase you");
    }
    @Test
    public void testExecuteCommandWithTakeCommandWithValidItem() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("TAKE", "SLEEP");
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(), "'sleep' has been added to your inventory!");
    }
    @Test
    public void testExecuteCommandWithDropoCommandWithValidItem() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("TAKE", "SLEEP");
        service.executeCommand(1, command);
        command = new Command("DROP", "SLEEP");
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(), "'sleep' has been removed to your inventory!");
    }
    @Test
    public void testExecuteCommandWithExamineCommandNullCommandValue() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("EXAMINE", null);
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(),
                "You are comfortably asleep in a dorm room in Florida Avenue Residences");
    }
    @Test
    public void testExecuteCommandWithHistoryCommandNullCommandValue() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("HISTORY", null);
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(),
                "Your past rooms: Florida Avenue Residences");
    }
    @Test
    public void testExecuteCommandWithWithWinningRoom() throws AdventureException, IOException {
        service.newGame();
        Command command = new Command("GO", "WEST");
        service.executeCommand(1, command);
        command = new Command("GO", "SOUTH");
        service.executeCommand(1, command);
        command = new Command("GO", "SOUTH");
        service.executeCommand(1, command);
        assertEquals(service.getGame(1).getMessage(),
                "Your alarm clock blares and you wake up. You Win!");
    }

    @Test
    public void testFetchLeadershipWithNoRecords() {
        service.reset();
        Map<String, Integer> emptyMap = new LinkedHashMap<>();
        assertEquals(service.fetchLeaderboard(), emptyMap);
    }
}
