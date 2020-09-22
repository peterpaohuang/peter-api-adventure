package student.adventure;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single room in the dream adventure game
 * @author Peter Pao-Huang
 * @version 1.0
 */
public class Room {
    @JsonProperty(value = "index", required = true) private int index;
    @JsonProperty(value = "location", required = true) private String location;
    @JsonProperty(value = "information", required = true) private String information;
    @JsonProperty(value = "directions", required = true) private List<Direction> directions;
    @JsonProperty(value = "items", required = true) private List<Item> items;
    @JsonProperty(value = "imageURL", required = true) private String imageURL;
    @JsonProperty(value = "didWin", required = true) private boolean didWin;

    // Getters
    public int getIndex() {
        return index;
    }
    public String getLocation() {
        return location;
    }
    public String getInformation() {
        return information;
    }
    public String getImageURL() { return imageURL; }
    public boolean isWinner() {
        return didWin;
    }
    public List<Direction> getDirections() {
        // clone to prevent possible mutation of original Directions list
        List<Direction> cloneDirections = new ArrayList<>(directions);
        return cloneDirections;
    }
    public List<Item> getItems() {
        // clone to prevent possible mutation of original Items list
        List<Item> cloneItems = new ArrayList<>(items);
        return cloneItems;
    }

    /**
     * Removes specified item from room
     * @param item to remove from room
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Adds specified item to room
     * @param item to add to room
     */
    public void addItem(Item item) {
        items.add(item);
    }
}

