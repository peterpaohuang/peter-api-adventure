package student.adventure;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an individual item available in a room in dream adventure game
 * @author Peter Pao-Huang
 * @version 1.0
 */
public class Item {
    @JsonProperty(value = "name", required = true) private String name;

    // Getter
    public String getName() {
        return name;
    }
}
