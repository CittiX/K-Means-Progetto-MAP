package data;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

class DiscreteAttribute extends Attribute implements Iterable<String> {
    private final TreeSet<String> values; //array di oggetti String, uno per ciascun valore del dominio discreto.
                     //I valori del dominio sono memorizzati in values seguendo un ordine lessiografico.

    DiscreteAttribute(String name, int index, TreeSet<String> values) {
        super(name, index);
        this.values = values;
    }

    int getNumberOfDistinctValues() {
        return this.values.size();
    }

    int frequency(Data data, Set<Integer> idList, String v) {
        int vFreq = 0;
        for(int j = 0; j < data.getNumberOfAttributes(); j++) {
            for(int k = 0; k < data.getNumberOfExamples(); k++) {
                if(v.equals(data.getAttributeValue(k).toString()))
                    vFreq++;
            }
        }
        return vFreq;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < getNumberOfDistinctValues() && values.toArray()[currentIndex] != null;
            }

            @Override
            public String next() {
                return (String) values.toArray()[currentIndex++];
            }
        };
    }
}
