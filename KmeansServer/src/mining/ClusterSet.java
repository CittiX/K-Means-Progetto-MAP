package mining;

import data.Data;
import data.OutOfRageSampleSize;
import data.Tuple;

import java.io.Serializable;

public class ClusterSet implements Serializable {
    Cluster[] C;
    int i = 0; //posizione valida per la memorizzazione di un nuovo cluster C

    ClusterSet(int k) {
        this.C = new Cluster[k];
    }

    void add(Cluster c) {
        this.C[i] = c;
        i++;
    }

    Cluster get(int i) {
        return this.C[i];
    }

    void initializeCentroids(Data data) throws OutOfRageSampleSize {
        int[] centroidIndexes = data.sampling(C.length);
        for (int i = 0; i < centroidIndexes.length; i++) {
            Tuple centroidI = data.getItemSet(centroidIndexes[i]);
            add(new Cluster(centroidI));
        }
    }

    Cluster nearestCluster(Tuple tuple) {
        Cluster out = null;
        for (int i = 0; i < this.C.length - 1; i++) {
            double temp = tuple.getDistance(get(i).getCentroid());
            for (int j = i + 1; j < this.C.length; j++) {
                double currentCentDist = tuple.getDistance(get(j).getCentroid());
                if (temp < currentCentDist) {
                    out = get(i);
                } else {
                    out = get(j);
                }
            }
        }
        return out;
    }

    Cluster currentCluster(int id) {
        Cluster out = null;
        boolean found = false;
        for(int i = 0; i < C.length && !found; i++) {
            if(get(i).contain(id)) {
                found = true;
                out = get(i);
            }
        }
        return out;
    }

    void updateCentroids(Data data) {
        for(var i : C) {
            i.computeCentroid(data);
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for(var i : C) {
            out.append(i.getCentroid());
        }

        return out.toString();
    }

    public String toString(Data data) {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < C.length; i++) {
            if(get(i) != null) {
                str.append(i).append(":").append(get(i).toString(data)).append("\n");
            }
        }
        return str.toString();
    }

}
