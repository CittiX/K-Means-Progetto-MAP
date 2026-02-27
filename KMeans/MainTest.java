import keyboardinput.Keyboard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Classe principale del client.
 */
public class MainTest {
	/**
	 * Stream con risposte dal server.
	 */
    private ObjectOutputStream out;
	/**
	 * Stream con richieste del client.
	 */
    private ObjectInputStream in;

	/**
	 * Stabilisce la connessione al server e, una volta ottenuta, invia e riceve messaggi in base alla scelta
	 * che l'utente effettua.
	 * @implNote Attraverso un {@link MainTest#menu() menu} l'utente sceglie se:
	 * <ol>
	 *     <li>Caricare dati da un file contenente cluster già generati:
	 *         <br>Si invia al server il nome del file in cui sono serializzati i cluster da recuperare.</li>
	 *     <li>Caricare dati nel server con nuovi cluster da generare:
	 *         <br>Si invia al server il nome della tabella del database, il numero di cluster da serializzare e il nome
	 *         del file in cui serializzare i cluster scoperti.</li>
	 * </ol>
	 * In ambo i casi il client acquisisce i risultati e li visualizza a video.
	 * Gli input da tastiera sono registrati con l'ausilio della classe {@link Keyboard}.
	 * @param ip Indirizzo ip (eventualmente parametrizzabile con {@code "localhost" (/127.0.0.1)}.
	 * @param port Porta del server al quale conettersi.
	 * @throws IOException Sollevata in caso di errori con gli stream.
	 * @see MainTest#learningFromFile()
	 * @see MainTest#learningFromDbTable()
	 * @see MainTest#storeTableFromDb()
	 * @see MainTest#storeClusterInFile()
	 */
    public MainTest(String ip, int port) throws IOException {
        InetAddress addr = InetAddress.getByName(ip); //ip
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, port); //Port
        System.out.println(socket);

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream()); //stream con richieste del client
    }

	/**
	 * Si connette al server e raccoglie informazioni da esso per poi visualizzarle a video.
	 * Controllare la documentazione di {@code MainTest} per ulteriori informazioni (riferirsi al tag {@code see}).
	 * @param args Indirizzo ip completo per collegarsi al server (ip + porta).
	 * @see MainTest#MainTest(String, int)  MainTest
	 */
    public static void main(String[] args) {
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        MainTest main = null;
        try {
            main = new MainTest(ip, port);
        } catch (IOException e) {
            System.out.println(e);
            return;
        }

        do {
            int menuAnswer = main.menu();
			switch (menuAnswer) {
				case 1 -> {
					try {
						String kmeans = main.learningFromFile();
						System.out.println(kmeans);
					} catch (SocketException e) {
						System.err.println("Sollevata SocketException: " + e.getMessage());
						return;
					} catch (FileNotFoundException e) {
						System.err.println("Sollevata FileNotFoundException: " + e.getMessage());
						return;
					} catch (IOException e) {
						System.err.println("Sollevata IOException: " + e.getMessage());
						return;
					} catch (ClassNotFoundException e) {
						System.err.println("Sollevata ClassNotFoundException: " + e.getMessage());
						return;
					} catch (ServerException e) {
						System.err.println("Sollevata ServerException: " + e.getMessage());
						return;
					}
				}
				case 2 -> { // learning from db

					while (true) {
						try {
							main.storeTableFromDb();
							break; //esce fuori dal while
						} catch (SocketException e) {
							System.err.println("Sollevata SocketException: " + e.getMessage());
							return;
						} catch (FileNotFoundException e) {
							System.err.println("Sollevata FileNotFoundException: " + e.getMessage());
							return;
						} catch (IOException e) {
							System.err.println("Sollevata IOException: " + e.getMessage());
							return;
						} catch (ClassNotFoundException e) {
							System.err.println("Sollevata ClassNotFoundException: " + e.getMessage());
							return;
						} catch (ServerException e) {
							System.err.println("Sollevata ServerException: " + e.getMessage());
							return;
						}
					} //end while [viene fuori dal while con un db (in alternativa il programma termina)
					char answer;//itera per learning al variare di k
					do {
						try {
							String clusterSet = main.learningFromDbTable();
							System.out.println(clusterSet);

							main.storeClusterInFile();

						} catch (SocketException e) {
							System.err.println("Sollevata SocketException: " + e.getMessage());
							return;
						} catch (FileNotFoundException e) {
							System.err.println("Sollevata FileNotFoundException: " + e.getMessage());
							return;
						} catch (IOException e) {
							System.err.println("Sollevata IOException: " + e.getMessage());
							return;
						} catch (ClassNotFoundException e) {
							System.err.println("Sollevata ClassNotFoundException: " + e.getMessage());
							return;
						} catch (ServerException e) {
							System.err.println("Sollevata ServerException: " + e.getMessage());
							return;
						}

						do {
							System.out.print("\nVuoi ripetere l'esecuzione?(y/n):> ");
							answer = Keyboard.readChar();
							if (answer != 'y' && answer != 'n')
								System.err.println("\nInput non valido!");
						} while (answer != 'y' && answer != 'n');
					}
					while (answer == 'y');
				} //fine case 2
				default -> System.out.println("Opzione non valida!");
			}

			char choice;
			do {
				System.out.print("\nVuoi scegliere una nuova operazione da menu?(y/n)");
				choice = Keyboard.readChar();
				if (choice != 'y' && choice != 'n')
					System.err.println("\nInput non valido!");
				else if (Keyboard.readChar() == 'n')
					break;
			} while (choice != 'y' && choice != 'n');
        }
        while (true);
    }

	/**
	 * Menu per selezionare l'attivita' da svolgere tra:
	 * <ol>
	 *     <li>Caricamento cluster da file</li>
	 *     <li>Caricamento dati sul server (con salvataggio su file)</li>
	 * </ol>
	 * @return Scelta effettuata dall'utente.
	 */
    private int menu() {
        int answer;
        System.out.println("Scegli un'opzione");
        do {
            System.out.println("(1) Carica Cluster da File");
            System.out.println("(2) Carica Dati");
            System.out.print("Risposta:> ");
            answer = Keyboard.readInt();
        }
        while (answer <= 0 || answer > 2);
        return answer;

    }

	/**
	 * Carica i dati di addestramento da un file salvato nel server.
	 * @return Stringa che contenente i cluster.
	 * @throws SocketException Sollevata in caso di errori nell'accesso al server.
	 * @throws ServerException Sollevata dal server in caso vi sono errori all'interno di esso.
	 * @throws IOException Sollevata se vi sono anomalie a livello di stream.
	 * @throws ClassNotFoundException Sollevata se non e' stato possibile trovare il nome della classe.
	 * @implSpec Il file deve avere come denominazione: <em>nomeTabella_numeroIterate.dat</em>.
	 */
    private String learningFromFile() throws SocketException, ServerException, IOException, ClassNotFoundException {
        out.writeObject(3);

        System.out.print("Nome tabella:> ");
        String tabName = Keyboard.readString();
        out.writeObject(tabName);
        System.out.print("Numero iterate:> ");
        int k = Keyboard.readInt();
        out.writeObject(k);
        String result = (String) in.readObject();
        if (result.equals("OK"))
            return (String) in.readObject();
        else throw new ServerException(result);

    }

	/**
	 * Salva la tabella (caricata da un database) modellandole come transazioni all'interno di una matrice.
	 * @throws SocketException Sollevata in caso di errori nell'accesso al server.
	 * @throws ServerException Sollevata dal server in caso vi sono errori all'interno di esso.
	 * @throws IOException Sollevata se vi sono anomalie a livello di stream.
	 * @throws ClassNotFoundException Sollevata se non e' stato possibile trovare il nome della classe.
	 */
    private void storeTableFromDb() throws SocketException, ServerException, IOException, ClassNotFoundException {
        out.writeObject(0);
        System.out.print("Nome tabella:> ");
        String tabName = Keyboard.readString();
        out.writeObject(tabName);
        String result = (String) in.readObject();
        if (!result.equals("OK"))
            throw new ServerException(result);

    }

	/**
	 * Invia al server il numero di cluster da scoprire.
	 * @return Stringa contenente i cluster.
	 * @throws SocketException Sollevata in caso di errori nell'accesso al server.
	 * @throws ServerException Sollevata dal server in caso vi sono errori all'interno di esso.
	 * @throws IOException Sollevata se vi sono anomalie a livello di stream.
	 * @throws ClassNotFoundException Sollevata se non e' stato possibile trovare il nome della classe.
	 */
    private String learningFromDbTable() throws SocketException, ServerException, IOException, ClassNotFoundException {
        out.writeObject(1);
        System.out.print("Numero di cluster:> ");
        int k = Keyboard.readInt();
        out.writeObject(k);
        String result = (String) in.readObject();
        if (result.equals("OK")) {
            System.out.println("Clustering output" + in.readObject());
            return (String) in.readObject();
        } else throw new ServerException(result);


    }

	/**
	 * Invia al server la richiesta di salvataggio del clustering effettuato all'interno di un file.
	 * @throws SocketException Sollevata in caso di errori nell'accesso al server.
	 * @throws ServerException Sollevata dal server in caso vi sono errori all'interno di esso.
	 * @throws IOException Sollevata se vi sono anomalie a livello di stream.
	 * @throws ClassNotFoundException Sollevata se non e' stato possibile trovare il nome della classe.
	 * @implSpec Il file deve avere come denominazione: <em>nomeTabella_numeroIterate.dat</em>.
	 */
    private void storeClusterInFile() throws SocketException, ServerException, IOException, ClassNotFoundException {
        out.writeObject(2);

        String result = (String) in.readObject();
        if (!result.equals("OK"))
            throw new ServerException(result);
    }
}



