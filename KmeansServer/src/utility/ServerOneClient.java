package utility;

import data.Data;
import data.OutOfRageSampleSize;
import data.ServerException;
import mining.KMeansMiner;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Modella una connessione al server
 */
public class ServerOneClient extends Thread {
    /**
     * Client al quale connettersi
     */
    Socket socket;

    /**
     * Stream con le richieste del client
     */
    ObjectInputStream in;

    /**
     * Stream con le risposte del server
     */
    ObjectOutputStream out;

    /**
     * Riferimento ad un oggetto {@link KMeansMiner}
     */
    KMeansMiner kmeans;

    /**
     * Inizializza gli attributi {@link ServerOneClient#socket}, {@link ServerOneClient#in} e {@link ServerOneClient#out} e
     * avvia il thread.
     * @param s Client al quale connettersi
     * @throws IOException Sollevata se ci sono errori negli stream di oggetti
     */
    public ServerOneClient(Socket s) throws IOException {
        try {
            this.socket = s;
            this.out = new ObjectOutputStream(s.getOutputStream());
            this.in = new ObjectInputStream(s.getInputStream());
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     * Riscrive il metodo della {@link Thread superclasse} al fine di gestire le richieste del client
     */
    @Override
    public void run() {
        try {
            Data data = null;
            int k = 0;
            String tabName = null;
            while (true) {
                int clientMsg = (int) in.readObject();
                System.out.println("Client Message: " + clientMsg);
                switch (clientMsg) {
                    case 3 -> {
                        //Il client vuole caricare da file un cluster
                        tabName = (String) in.readObject();
                        k = (int) in.readObject(); //Receive iteration number
                        String file = tabName + "_" + k + ".dat"; //Build file name
                        this.kmeans = new KMeansMiner(file); //From file build Cluster
                        data = new Data(tabName);
                        out.writeObject("OK"); //Send OK

                        out.writeObject(this.kmeans.getC().toString(data)); //Send kmeans Object
                    }
                    case 0 -> {
                        //Il client vuole caricare i dati
                        tabName = (String) in.readObject();
                        data = new Data(tabName);
                        out.writeObject("OK");
                    }
                    case 1 -> {
                        k = (int) in.readObject();
                        this.kmeans = new KMeansMiner(k);
                        this.kmeans.kMeans(data);
                        out.writeObject("OK");
                        out.writeObject(this.kmeans.getC().toString(data));
                        out.writeObject(this.kmeans.getC().toString(data));
                    }
                    case 2 -> {
                        this.kmeans.save(tabName + "_" + k + ".dat");
                        out.writeObject("OK");
                    }
                    default -> ServerException.sendException(this.socket, new ServerException());
                }
            }
        } catch (IOException | ClassNotFoundException | OutOfRageSampleSize e) {
            e.printStackTrace();
        } finally {
            System.out.println("[SERVER]: Closing...");
            try {
                in.close();
                out.close();
                socket.close();
                if (socket.isClosed())
                    System.out.println("[SERVER]: Socket has just been closed");
            } catch (IOException e) {
                System.err.println("Socket not closed");
            }
        }
    }

}
