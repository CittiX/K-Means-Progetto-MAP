package data;

public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException() {
        System.out.println("Errore database: ");
    }

    @Override
    public String toString() {
        return getMessage() + " impossibile stabilire una connessione";
    }
}
