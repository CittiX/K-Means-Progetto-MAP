package utility;

import data.ServerException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
    int port = 8080;

    public MultiServer(int port) {
        this.port = port;
        run();
    }

    void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server in ascolto sulla porta " + port);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    new ServerOneClient(clientSocket);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MultiServer(8080);
    }
}
