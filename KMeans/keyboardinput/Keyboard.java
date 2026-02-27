package keyboardinput;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Facilita l'input da tastiera astraendo i dettagli circa l'analisi dell'input, conversioni e gestione
 * delle eccezioni.
 * @author Lewis
 * @author Loftus
 */
public class Keyboard {
	// ************* Error Handling Section **************************

	private static boolean printErrors = true;

	private static int errorCount = 0;

	/**
	 * Restituisce il numero degli errori.
	 * @return Numero degli errori.
	 */
	public static int getErrorCount() {
		return errorCount;
	}

	/**
	 * Reimposta il numero degli errori.
	 * @param count
	 */
	public static void resetErrorCount(int count) {
		errorCount = 0;
	}

	/**
	 * Restituisce vero se gli errori sono stati stampati a schermo, falso altrimenti.
	 * @return Vero se gli errori sono stati stampati a schermo, falso altrimenti.
	 */
	public static boolean getPrintErrors() {
		return printErrors;
	}

	/**
	 * Imposta {@link Keyboard#printErrors} a {@code flag}.
	 * @param flag Booleano che indica se gli errori stanno per essere stampati.
	 */
	public static void setPrintErrors(boolean flag) {
		printErrors = flag;
	}

	/**
	 * Incrementa il numero degli errori e li stampa se necessario.
	 * @param str Stringa contenente gli errori.
	 */
	private static void error(String str) {
		errorCount++;
		if (printErrors)
			System.out.println(str);
	}

	// ************* Tokenized Input Stream Section ******************

	/**
	 * Token corrente.
	 */
	private static String current_token = null;
	/**
	 * Tokenizer che suddivide una stringa in token.
	 */
	private static StringTokenizer reader;
	/**
	 * Lettore con buffer.
	 */
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Acquisisce il token successivo dall'input assumendo che possa trovarsi nella prossima riga di input.
	 * @return Token successivo.
	 */
	private static String getNextToken() {
		return getNextToken(true);
	}

	// -----------------------------------------------------------------
	// Gets the next input token, which may already have been read.
	// -----------------------------------------------------------------
	private static String getNextToken(boolean skip) {
		String token;

		if (current_token == null)
			token = getNextInputToken(skip);
		else {
			token = current_token;
			current_token = null;
		}

		return token;
	}

	/**
	 * Acquisisce il prossimo token dall'input che puo' provenire dall'attuale riga di input oppure da una
	 * successiva.
	 * @param skip Determina se la riga successiva e' utilizzata.
	 * @return Token successivo dall'input.
	 */
	private static String getNextInputToken(boolean skip) {
		final String delimiters = " \t\n\r\f";
		String token = null;

		try {
			if (reader == null)
				reader = new StringTokenizer(in.readLine(), delimiters, true);

			while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
				while (!reader.hasMoreTokens())
					reader = new StringTokenizer(in.readLine(), delimiters,
							true);

				token = reader.nextToken();
			}
		} catch (Exception exception) {
			token = null;
		}

		return token;
	}

	/**
	 * Restituisce un booleano sulla base dell'assenza di ulteriori righe da analizzare.
	 * @return Vero se non ci sono altri token da acquisire, falso altrimenti.
	 */
	public static boolean endOfLine() {
		return !reader.hasMoreTokens();
	}

	/**
	 * Restituisce una stringa letta dall'input standard.
	 * @return Stringa acquisita dall'input standard.
	 */
	public static String readString() {
		String str;

		try {
			str = getNextToken(false);
			while (!endOfLine()) {
				str = str + getNextToken(false);
			}
		} catch (Exception exception) {
			error("Error reading String data, null value returned.");
			str = null;
		}
		return str;
	}

	/**
	 * Crea una sottostringa delimitata da spazi (una parola) letta dall'input standard.
	 * @return Sottostringa delimitata da spazi.
	 */
	public static String readWord() {
		String token;
		try {
			token = getNextToken();
		} catch (Exception exception) {
			error("Error reading String data, null value returned.");
			token = null;
		}
		return token;
	}

	/**
	 * @return Booleano letto dall'input standard.
	 */
	public static boolean readBoolean() {
		String token = getNextToken();
		boolean bool;
		try {
			if (token.toLowerCase().equals("true"))
				bool = true;
			else if (token.toLowerCase().equals("false"))
				bool = false;
			else {
				error("Error reading boolean data, false value returned.");
				bool = false;
			}
		} catch (Exception exception) {
			error("Error reading boolean data, false value returned.");
			bool = false;
		}
		return bool;
	}

	/**
	 * Restituisce un carattere letto dall'input standard.
	 * @return Carattere acquisito dall'input standard.
	 */
	public static char readChar() {
		String token = getNextToken(false);
		char value;
		try {
			if (token.length() > 1) {
				current_token = token.substring(1, token.length());
			} else
				current_token = null;
			value = token.charAt(0);
		} catch (Exception exception) {
			error("Error reading char data, MIN_VALUE value returned.");
			value = Character.MIN_VALUE;
		}

		return value;
	}

	/**
	 * Restituisce un intero letto dall'input standard.
	 * @return Intero acquisto dall'input standard.
	 */
	public static int readInt() {
		String token = getNextToken();
		int value;
		try {
			value = Integer.parseInt(token);
		} catch (Exception exception) {
			error("Error reading int data, MIN_VALUE value returned.");
			value = Integer.MIN_VALUE;
		}
		return value;
	}

	/**
	 * Restituisce un intero lungo dall'input standard.
	 * @return Intero lungo acquisito dall'input standard.
	 */
	public static long readLong() {
		String token = getNextToken();
		long value;
		try {
			value = Long.parseLong(token);
		} catch (Exception exception) {
			error("Error reading long data, MIN_VALUE value returned.");
			value = Long.MIN_VALUE;
		}
		return value;
	}

	/**
	 * Restituisce un float dall'input standard.
	 * @return Float acquisito dall'input standard.
	 */
	public static float readFloat() {
		String token = getNextToken();
		float value;
		try {
			value = (new Float(token)).floatValue();
		} catch (Exception exception) {
			error("Error reading float data, NaN value returned.");
			value = Float.NaN;
		}
		return value;
	}

	/**
	 * Restituisce un double letto dall'input standard.
	 * @return Double letto dall'input standard.
	 */
	public static double readDouble() {
		String token = getNextToken();
		double value;
		try {
			value = (new Double(token)).doubleValue();
		} catch (Exception exception) {
			error("Error reading double data, NaN value returned.");
			value = Double.NaN;
		}
		return value;
	}
}
