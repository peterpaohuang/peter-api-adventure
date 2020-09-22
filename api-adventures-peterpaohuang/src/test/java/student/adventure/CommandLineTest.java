package student.adventure;

import org.junit.Test;
import student.cli.CommandLine;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CommandLineTest {
    /*
     * These tests can be generalized to all interfaces
     * These tests target whether the interface includes functionality necessary to support Game
     *
     * Testing strategy
     *
     * CONSTRUCTOR TESTS
     * Test if accepts input filepath to Game dataset
     * Test if checks for incomplete dataset (shared test with AdventureLoadDataTest - will not repeat)
     *
     * Test invalid command word through parseInput
     * Test detection of command word go with valid direction through parseInput
     * Test detection of command word go with invalid direction through parseInput
     * Test detection of command word examine through parseInput
     * Test detection of command word history through parseInput
     * Test detection of command word take with item in room through parseInput
     * Test detection of command word take with item not in room through parseInput
     * Test detection of command word drop with item in inventory through parseInput
     * Test detection of command word drop with item not in inventory through parseInput
     * Test detection of exit word
     * Test detection of quit word
     *
     */

    @Test
    public void testAcceptsFilepathInput() throws IOException {
        String filepath = "src/main/resources/sleepDreamRooms.json";
        CommandLine cli = new CommandLine(filepath);
    }
}
