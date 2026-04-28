package mining;

import data.Data;
import data.Tuple;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Cluster implements Serializable {
	private final Tuple centroid;

	private final Set<Integer> clusteredData = new HashSet<>();
	
	/*src.mining.Cluster(){
		
	}*/

	Cluster(Tuple centroid){
		this.centroid = centroid;
	}
		
	Tuple getCentroid(){
		return centroid;
	}
	
	void computeCentroid(Data data){
		for(int i = 0; i < centroid.getLength(); i++){
			centroid.get(i).update(data,clusteredData);
		}
		
	}
	//return true if the tuple is changing cluster
	boolean addData(int id){
		return clusteredData.add(id);
		
	}
	
	//verifica se una transazione � clusterizzata nell'array corrente
	boolean contain(int id){
		return clusteredData.contains(id);
	}
	

	//remove the tuple that has changed the cluster
	void removeTuple(int id){
		clusteredData.remove(id);
		
	}
	
	public String toString(){
		StringBuilder str= new StringBuilder("Centroid=(");
		for(int i=0;i<centroid.getLength();i++)
			str.append(centroid.get(i));
		str.append(")");
		return str.toString();
		
	}
	

	
	public String toString(Data data){
		StringBuilder str= new StringBuilder("Centroid=(");
		for(int i=0;i<centroid.getLength();i++)
			str.append(centroid.get(i)).append(" ");
		str.append(")\nExamples:\n");
		Set<Integer> array = new HashSet<>(clusteredData);
        for (int k : array) {
            str.append("[");
            for (int j = 0; j < data.getNumberOfAttributes(); j++)
                str.append(data.getAttributeValue(k)).append(" ");
            str.append("] dist=").append(getCentroid().getDistance(data.getItemSet(k))).append("\n");

        }
		str.append("\nAvgDistance=").append(getCentroid().avgDistance(data, array));
		return str.toString();
		
	}

}
