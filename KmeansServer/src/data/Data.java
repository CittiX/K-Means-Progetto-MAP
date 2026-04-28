package data;

import java.sql.SQLException;
import java.util.*;

public class Data {
    private List<Example> data; //una matrice nXm di tipo Object dove ogni riga modella una transazione
    private int numberOfExamples; //cardinalita' dell'insieme di transazioni (numero di righe in src.data)
    private List<Attribute> attributeSet; //un vettore degli attributi in ciascuna tupla (schema della tabella dei dati)

    public Data(String table) {
        DbAccess db = new DbAccess();
        try {
            db.initConnection();
            TableData database = new TableData(db);
            this.data = new ArrayList<>(database.getDistinctTransazioni(table));
            this.attributeSet = new ArrayList<>();
            TableSchema schema = new TableSchema(db, table);
            for (int i = 0; i < schema.getNumberOfAttributes(); i++) {
                if (schema.getColumn(i).isNumber())
                    this.attributeSet.add(new ContinuousAttribute(schema.getColumn(i).getColumnName(),
                            i,
                            (Float)database.getAggregateColumnValue(table, schema.getColumn(i), QUERY_TYPE.MIN),
                            (Float)database.getAggregateColumnValue(table, schema.getColumn(i), QUERY_TYPE.MAX)
                    ));
                else this.attributeSet.add(new DiscreteAttribute(schema.getColumn(i).getColumnName(),
                        i,
                        new TreeSet<>(Collections.singleton(database.getDistinctColumnValues(table, schema.getColumn(i)).toString()))));
            }
            this.numberOfExamples = this.data.size();
        } catch (SQLException | EmptySetException | NoValueException | DatabaseConnectionException e) {
             e.printStackTrace();
        }
    }

    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    public int getNumberOfAttributes() {
        return this.attributeSet.size();
    }

    List<Attribute> getAttributeSet() {
        return this.attributeSet;
    }

    public Example getAttributeValue(int exampleIndex) {
        return data.get(exampleIndex);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        for(Attribute i : getAttributeSet())
            out.append(i).append(',');
        out.append('\n');
        for(int j = 0; j < getNumberOfExamples(); j++) {
            out.append(j + 1).append(':');
            out.append(getAttributeValue(j)).append('\n');
        }
        return out.toString();
    }

    public Tuple getItemSet(int index) {
        Tuple tuple = new Tuple(getNumberOfAttributes());
        for(int i = 0; i < getAttributeSet().size(); i++) {
            if (attributeSet.get(i) instanceof DiscreteAttribute)
                tuple.add(new DiscreteItem((DiscreteAttribute) attributeSet.get(i), data.get(index).toString()), i);
            else tuple.add(new ContinuousItem(attributeSet.get(i), (Double) data.get(index).get(i)), i);
        }
        return tuple;
    }

    public int[] sampling(int k) throws OutOfRageSampleSize {
        int[] centroidIndexes = new int[k];
        //scegli k centroidi diversi in src.data
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for(int i = 0; i < k; i++) {
            boolean found = false;
            int c;
            do {
                found = false;
                c = rand.nextInt(getNumberOfExamples());
                // verifica che centroid[c] sia diverso dal centroide gia' inserito in centroidIndexes
                for(int j = 0; j < i; j++) {
                    if(compare(centroidIndexes[j], c)) {
                        found = true;
                        break;
                    }
                }

            } while(found);
            centroidIndexes[i] = c;
        }
        return centroidIndexes;
    }

    private boolean compare(int i, int j) {
        boolean compared = true;
        for(int k = 0; k < getNumberOfAttributes(); k++) {
            if(!(getAttributeValue(i).equals(getAttributeValue(j))))
                compared = false;
        }
        return compared;
    }

    Object computePrototype(Set<Integer> idList, Attribute attribute) {
        return attribute instanceof ContinuousAttribute ?
                computePrototype(idList, (ContinuousAttribute) attribute) :
                computePrototype(idList, (DiscreteAttribute) attribute);
    }

    String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
        String out = "";
        int maxFreq = 0;

        for(Example i : data) {
            int exampleFreq = attribute.frequency(this, idList, i.toString());
            for (String j : attribute)
                if (j.equals(i.toString()))
                    if (maxFreq < exampleFreq) {
                        maxFreq = exampleFreq;
                        out = i.toString();
                    }
        }
        return out;
    }

    Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
        double avg = 0.0;

        for(Integer i : idList) {
            for(int j = 0; j < getNumberOfAttributes(); j++)
                if(getAttributeValue(i).get(j).equals(attribute)) {
                    avg += (Double) getAttributeValue(i).get(i) / 2;
                }
        }

        return avg;
    }
}
