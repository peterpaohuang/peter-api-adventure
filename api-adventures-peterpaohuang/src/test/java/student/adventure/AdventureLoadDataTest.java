package student.adventure;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.junit.Test;
import student.cli.CommandLine;

import java.io.IOException;

public class AdventureLoadDataTest {
    /*
     * TESTING STRATEGY
     *** Test reading JSON to POJO ***
     *
     * Test invalid json file path
     * Test empty json file
     * Test improperly formatted json file
     * Test missing key attributes
     * Test missing value attributes
     * Test wrong key attributes
     * Test wrong value attribute type (String when expecting int)
     * Test no ending room
     * Test unmatchable String to Enum (instead of say "East", try "foo")
     */

    @Test(expected = IOException.class)
    public void testInvalidJSONFilePath() throws IOException {
        CommandLine cli = new CommandLine("not/valid/path.json");
    }
    @Test(expected = MismatchedInputException.class)
    public void testEmptyJSONFile() throws IOException {
        CommandLine cli = new CommandLine("src/main/resources/test/emptyFileTest.json");
    }
    @Test(expected = IOException.class)
    public void testImproperlyFormattedJSONFile() throws IOException {
        CommandLine cli =
                new CommandLine("src/main/resources/test/invalidFormatFileTest.json");
    }
    @Test(expected = NullPointerException.class)
    public void testMissingKeyAttributesJSONFile() throws IOException {
        CommandLine cli =
                new CommandLine("src/main/resources/test/missingKeyAttributesFileTest.json");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testMissingValueAttributesJSONFile() throws IOException {
        CommandLine cli =
                new CommandLine("src/main/resources/test/missingValueAttributesFileTest.json");
    }
    @Test(expected = IOException.class)
    public void testWrongKeyAttributesJSONFile() throws IOException {
        CommandLine cli =
                new CommandLine("src/main/resources/test/wrongKeyAttributesFileTest.json");
    }
    @Test(expected = IOException.class)
    public void testWrongValueAttributeTypeJSONFile() throws IOException {
        CommandLine cli =
                new CommandLine("src/main/resources/test/wrongValueAttributesFileTest.json");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testMissingEndingRoomJSONFile() throws IOException {
        CommandLine cli =
                new CommandLine("src/main/resources/test/missingEndingRoomFileTest.json");
    }
    @Test(expected = JsonMappingException.class)
    public void testWrongStringToEnumValuesJSONFile() throws IOException {
        CommandLine cli =
                new CommandLine("src/main/resources/test/wrongStringToEnumValuesFileTest.json");
    }
}
