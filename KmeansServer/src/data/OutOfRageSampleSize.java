package data;

public class OutOfRageSampleSize extends Throwable {
    public OutOfRageSampleSize() {
        super("Si e' verificato un problema con il numero di cluster scelto");
    }

    @Override
    public String toString() {
        return getMessage() + ": il numero scelto e' maggiore o nullo rispetto al numero" +
                " dei centroidi generabili dall'insieme di transazioni";
    }
}
