package student.adventure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import student.server.AdventureState;
import student.server.Command;
import student.server.GameStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents the dream adventure game that holds all room POJOs deserialized from JSON
 *
 * @author Peter Pao-Huang
 * @version 1.0
 */
public class Game {
    @JsonProperty(value = "rooms", required = true)
    private List<Room> rooms;
    @JsonIgnore
    private List<Room> pastRooms;
    @JsonIgnore
    private List<Item> inventory = new ArrayList<>();
    @JsonIgnore
    private int instanceId;
    @JsonIgnore
    private GameStatus status;

    // Getters
    public List<Item> getInventory() {
        List<Item> clonedInventory = new ArrayList<>(inventory);
        return clonedInventory;
    }

    public List<Room> getPastRooms() {
        List<Room> clonePastRooms = new ArrayList<Room>(pastRooms);
        return clonePastRooms;
    }

    public List<Room> getAllRooms() {
        List<Room> cloneAllRooms = new ArrayList<Room>(rooms);
        return cloneAllRooms;
    }

    public GameStatus getGameStatus() {
        return status;
    }

    public Room getCurrentRoom() {
        // most recent room is the last element in pastRooms list
        return pastRooms.get(pastRooms.size() - 1);
    }


    public void initializeGame(int instanceId) {
        if (this.instanceId == 0) {
            pastRooms = new ArrayList<Room>();
            for (Room room : rooms) {
                if (room.getIndex() == 0) {
                    pastRooms.add(room);
                }
            }

            DatasetValidity datasetValidity = new DatasetValidity(this);
            datasetValidity.check();

            this.instanceId = instanceId;
            this.status = new GameStatus(false, instanceId, getCurrentRoom().getInformation(),
                    getCurrentRoom().getImageURL(), null, new AdventureState(), generateCommandOptions());
        } else {
            throw new IllegalArgumentException("Cannot set instance ID more than once");
        }
    }

    public GameStatus input(Command command) {
        switch (command.getCommandName().toLowerCase()) {
            case Command.DROP:
                status = removeFromInventory(command.getCommandValue().toLowerCase());
                return status;
            case Command.TAKE:
                status = addToInventory(command.getCommandValue().toLowerCase());
                return status;
            case Command.HISTORY:
                status = showPastRooms();
                return status;
            case Command.GO:
                try {
                    status = goToDirection(Orientation.valueOf(command.getCommandValue().toUpperCase()));
                } catch(Exception e) {
                    status = showInvalidCommandError(command);
                } finally {
                    return status;
                }
            case Command.EXAMINE:
                status = examineCurrentRoom();
                return status;
            default:
                status = showInvalidCommandError(command);
                return status;
        }
    }

    public GameStatus showInvalidCommandError(Command command) {
        String message = "I can't understand '" + command.getCommandName() + " " + command.getCommandValue() + "'!";
        status = new GameStatus(true, instanceId, message,
                getCurrentRoom().getImageURL(), null, new AdventureState(), generateCommandOptions());

        return status;
    }

    public GameStatus examineCurrentRoom() {
        String message = getCurrentRoom().getInformation();
        status = new GameStatus(false, instanceId, message,
                getCurrentRoom().getImageURL(), null, new AdventureState(), generateCommandOptions());

        return status;
    }

    public GameStatus showPastRooms() {
        String message = "";
        for (int i = 0; i < pastRooms.size(); i++) {
            if (i == 0) {
                message += "Your past rooms: ";
            }

            Room room = pastRooms.get(i);
            message += room.getLocation();

            if (i != pastRooms.size() - 1) {
                message += ", ";
            }
        }
        status = new GameStatus(false, instanceId, message,
                null, null, new AdventureState(), generateCommandOptions());

        return status;
    }

    /**
     * Moves user to the room corresponding to given direction
     *
     * @param orientation what direction to move in
     * @return boolean of whether move in specified direction was successful
     */
    public GameStatus goToDirection(Orientation orientation) {
        String message = "";
        boolean isError = false;

        // checks if given orientation matches any available direction in current room
        for (Direction direction : getCurrentRoom().getDirections()) {
            if (direction.getOrientation() == orientation) {
                for (Room room : rooms) {
                    if (room.getIndex() == direction.getIndex()) {
                        pastRooms.add(room);
                        message = getCurrentRoom().getInformation();
                    }
                }
            }
        }
        if (message.isEmpty()) {
            message = "I can't go '" + orientation.name() + "'!";
            isError = true;
        }

        return new GameStatus(isError, instanceId, message, getCurrentRoom().getImageURL(),
                null, new AdventureState(), generateCommandOptions());
    }

    /**
     * Adds specified item to user's inventory
     *
     * @param itemName name of item to add to inventory
     * @return boolean of whether adding item to inventory was successful
     */
    public GameStatus addToInventory(String itemName) {
        Room currentRoom = getCurrentRoom();

        String message = "";
        boolean isError = false;

        // checks if itemName matches any item in current room
        for (Item item : currentRoom.getItems()) {
            if (item.getName().equals(itemName)) {
                inventory.add(item);
                getCurrentRoom().removeItem(item);
                message = "'" + itemName + "' has been added to your inventory!";
            }
        }
        if (message.isEmpty()) {
            message = "There is no '" + itemName + "' in the room!";
            isError = true;
        }

        return new GameStatus(isError, instanceId, message, getCurrentRoom().getImageURL(),
                null, new AdventureState(), generateCommandOptions());
    }

    /**
     * Removes specified item from user's inventory
     *
     * @param itemName name of item to be removed from inventory
     * @return boolean of whether removing item from inventory was successful
     */
    public GameStatus removeFromInventory(String itemName) {
        String message = "";
        boolean isError = false;

        // checks if itemName matches any item currently in inventory
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            if (item.getName().equals(itemName)) {
                inventory.remove(item);
                getCurrentRoom().addItem(item);
                message = "'" + itemName + "' has been removed to your inventory!";
            }
        }
        if (message.isEmpty()) {
            message = "You don't have '" + itemName + "'!";
            isError = true;
        }

        return new GameStatus(isError, instanceId, message, getCurrentRoom().getImageURL(),
                null, new AdventureState(), generateCommandOptions());
    }

    public Map<String, List<String>> generateCommandOptions() {
        Map<String, List<String>> commandOptions = new HashMap<>();

        if (!getCurrentRoom().isWinner()) {
            // Code below is derived from:
            // https://stackoverflow.com/questions/7309259/get-list-of-attributes-of-an-object-in-an-list
            commandOptions.put(Command.DROP,
                    inventory.stream().map(Item::getName).collect(Collectors.toCollection(ArrayList::new)));
            commandOptions.put(Command.TAKE, getCurrentRoom().getItems().stream()
                    .map(Item::getName).collect(Collectors.toCollection(ArrayList::new)));
            commandOptions.put(Command.HISTORY, null);
            commandOptions.put(Command.GO, getCurrentRoom().getDirections().stream()
                    .map(direction -> direction.getOrientation().name())
                    .collect(Collectors.toCollection(ArrayList::new)));
            commandOptions.put(Command.EXAMINE, null);
        }

        return commandOptions;
    }
}