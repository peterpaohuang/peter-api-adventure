package student.adventure;

import java.util.ArrayList;

public class DatasetValidity {
    Game game;

    public DatasetValidity(Game game) {
        this.game = game;
    }

    public void check() {
        checkNullAttributes();
        checkStartingRoom();
        checkEndingRoom();
    }

    public void checkNullAttributes() {
        if (
                game.getAllRooms() == null
                || game.getAllRooms().size() == 0
                || game.getCurrentRoom().getDirections().size() == 0) {
            throw new IllegalArgumentException("Your adventure dataset is incomplete");
        }
    }

    public void checkStartingRoom() {
        if (game.getPastRooms().size() < 1) {
            throw new IllegalArgumentException("Adventure dataset does not include room with index 0");
        }
    }
    public void checkEndingRoom() {
        boolean doesEndExist = false;
        for (Room room : game.getAllRooms()) {
            if (room.isWinner() == true) {
                doesEndExist = true;
            }
        }
        if (!doesEndExist) {
            throw new IllegalArgumentException("Your adventure dataset does not include an ending room");
        }
    }

}
