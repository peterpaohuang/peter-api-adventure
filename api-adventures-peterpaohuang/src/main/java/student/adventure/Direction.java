package student.adventure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents a direction object in JSON
 * Includes orientation of direction and the room index the direction is pointing to
 * @author Peter Pao-Huang
 * @version 1.0
 */
public class Direction {
    @JsonProperty(value = "index", required = true) private int index;
    @JsonIgnore private Orientation orientation;

    /**
     * Setter annotation of direction property to map JSON direction orientation to Orientation enumeration
     * @param orientation the orientation string deserialized from JSON
     */
    @JsonSetter("direction")
    public void setDegree(String orientation) {
        try {
            this.orientation = Orientation.valueOf(orientation.toUpperCase());
        } catch(Exception e) {
            throw new IllegalArgumentException("Unable to convert Direction string to Enum");
        }
    }

    // Getter methods
    public int getIndex() {
        return index;
    }
    public Orientation getOrientation() {
        return orientation;
    }
}
