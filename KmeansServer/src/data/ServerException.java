package data;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ServerException extends Exception implements Serializable {
    public ServerException() {
        System.out.println("Errore del server: ");
    }

    public ServerException(String message) {
        System.out.println("Errore del server: (" + message + "): ");
    }

    public static void sendException(Socket socket, Exception e) {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(e);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return getMessage() + " qualcosa e' andato storto con il server!";
    }
}
