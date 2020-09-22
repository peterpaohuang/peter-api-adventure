package student.adventure;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AdventureGameTest {
    /*
     * Testing Strategy
     *
     * Test goToDirection with null input
     * Test goToDirection with valid orientation but not available in current room
     * Test goToDirection with valid orientation that is available in current room
     * Test getCurrentRoom with no past rooms
     * Test getCurrentRoom after arbitrary non-zero rooms added
     * Test getCurrentRoom with no room with index 0
     * Test addToInventory with null input
     * Test addToInventory with empty String
     * Test addToInventory with valid String, but does not match any items available in room
     * Test addToInventory with valid String that matches an item available in room
     * Test removeFromInventory with null input
     * Test removeFromInventory with empty String
     * Test removeFromInventory with valid String, but does not match any items in inventory
     * Test removeFromInventory with valid String that matches an item available in inventory
     */

    private Game game;

    @Before
    public void init() throws IOException {
        String filepath = "src/main/resources/sleepDreamRooms.json";
        File file = new File(filepath);
        game = new ObjectMapper().readValue(file, Game.class);
        game.initializeGame(1);
    }

    @Test
    public void testGoToDirectionWithNullInput() {
        assertEquals(game.goToDirection(null), false);
    }
    @Test
    public void testGoToDirectionWithNotAvailableValidOrientation() {
        assertEquals(game.goToDirection(Orientation.NORTH), false);
    }
    @Test
    public void testGoToDirectionWithAvailableValidOrientation() {
        assertEquals(game.goToDirection(Orientation.WEST), true);
    }

    @Test
    public void testGetCurrentRoomWithNoPastRooms() {
        assertEquals(game.getCurrentRoom().getIndex(), 0);
    }
    @Test
    public void testGetCurrentRoomWithRoomsAdded() {
        game.goToDirection(Orientation.WEST);
        game.goToDirection(Orientation.EAST);
        assertEquals(game.getCurrentRoom().getIndex(), 2);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testGetCurrentRoomWithNoRoomIndexZero() throws IOException{
        String filepath = "src/main/resources/test/missingZeroIndexRoomFileTest.json";
        File file = new File(filepath);
        Game specialGame = new ObjectMapper().readValue(file, Game.class);

        specialGame.getCurrentRoom();
    }

    @Test
    public void testAddToInventoryWithNullInput() {
        assertEquals(game.addToInventory(null), false);
    }
    @Test
    public void testAddToInventoryWithEmptyString() {
        assertEquals(game.addToInventory(""), false);
    }
    @Test
    public void testAddToInventoryWithNoMatchValidString() {
        assertEquals(game.addToInventory("Not an item"), false);
    }
    @Test
    public void testAddToInventoryWithMatchValidString() {
        assertEquals(game.addToInventory("sleep"), true);
    }

    @Test
    public void testRemoveFromInventoryWithNullInput() {
        assertEquals(game.removeFromInventory(null), false);
    }
    @Test
    public void testRemoveFromInventoryWithEmptyString() {
        assertEquals(game.removeFromInventory(""), false);
    }
    @Test
    public void testRemoveFromInventoryWithItemNotInInventory
    @Test
    public void testRemoveFromInventoryWithNoMatchValidString() {
        assertEquals(game.removeFromInventory("foo"), false);
    }
    @Test
    public void testRemoveFromInventoryWithMatchValidString() {
        game.addToInventory("sleep");
        assertEquals(game.removeFromInventory("sleep"), true);
    }
}
