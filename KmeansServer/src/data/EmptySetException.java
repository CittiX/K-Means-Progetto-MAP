package data;

public class EmptySetException extends Exception {
    public EmptySetException() {
        System.out.println("Errore di resultSet: ");
    }

    @Override
    public String toString() {
        return getMessage() + " resultSet vuoto";
    }
}
