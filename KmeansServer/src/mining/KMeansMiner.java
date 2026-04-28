package mining;

import data.Data;
import data.OutOfRageSampleSize;

import java.io.*;

public class KMeansMiner implements Serializable {
    ClusterSet C;

    public KMeansMiner(int k) {
        this.C = new ClusterSet(k);
    }

    public KMeansMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
        this.C = (ClusterSet) in.readObject();
        in.close();
    }

    public ClusterSet getC() {
        return this.C;
    }

    public int kMeans(Data data) throws OutOfRageSampleSize {
        int numberOfIterations = 0;
        //STEP 1
        C.initializeCentroids(data);
        boolean changedCluster = false;
        do {
            numberOfIterations++;
            //STEP 2
            changedCluster = false;
             for(int i = 0; i < data.getNumberOfExamples(); i++) {
                Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
                Cluster oldCluster = C.currentCluster(i);
                boolean currentChange = nearestCluster.addData(i);
                if(currentChange)
                    changedCluster = true;
                //rimuovo la tupla dal vecchio cluster
                if(currentChange && oldCluster != null)
                    //il nodo va rimosso dal suo vecchio cluster
                    oldCluster.removeTuple(i);
            }
            //STEP 3
            C.updateCentroids(data);
        } while(changedCluster);
        return numberOfIterations;
    }

    public void save(String fileName) throws FileNotFoundException, IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(C);
        out.close();
    }

}
