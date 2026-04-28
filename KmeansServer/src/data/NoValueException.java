package data;

public class NoValueException extends Exception {
    public NoValueException() {
        System.out.println("Errore di valore: ");
    }

    @Override
    public String toString() {
        return getMessage() + " valore assente nel resultSet";
    }
}
