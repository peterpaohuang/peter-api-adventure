package student.server;

/**
 * An exception thrown if there was an issue starting a new adventure game.
 */
public class AdventureException extends Exception {
    public AdventureException(String message) {
        super(message);
    }

    public AdventureException(String message, Throwable cause) {
        super(message, cause);
    }
}
