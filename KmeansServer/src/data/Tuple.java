package data;

import java.io.Serializable;
import java.util.Set;

public class Tuple implements Serializable {
    Item[] tuple;

    Tuple(int size) {
        this.tuple = new Item[size];
    }

    public int getLength() {
        return this.tuple.length;
    }

    public Item get(int i) {
        return this.tuple[i];
    }

    void add(Item c, int i) {
        this.tuple[i] = c;
    }

    public double getDistance(Tuple obj) {
        double tupleDistance = 0;
        for(int i = 0; i < this.getLength(); i++) {
            tupleDistance += obj.get(i).distance(this.get(i));
        }
        return tupleDistance;
    }

    public double avgDistance(Data data, Set<Integer> clusteredData) {
        double p, d, sumD = 0;
        for (Integer clusteredDatum : clusteredData) {
            d = getDistance(data.getItemSet(clusteredDatum));
            sumD += d;
        }
        /*
        for(int i = 0; i < clusteredData.length; i++) {
            double d = getDistance(src.data.getItemSet(clusteredData[i]));
            sumD += d;
        }
         */
        p = sumD / clusteredData.size();
        return p;
    }
}
