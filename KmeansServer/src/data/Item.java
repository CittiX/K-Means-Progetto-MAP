package data;

import java.io.Serializable;
import java.util.Set;

public abstract class Item implements Serializable {
    private final Attribute attribute; //attributo coinvolto nell'item
    private final Object value; //valore assegnato all'attributo

    Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    Attribute getAttribute() {
        return this.attribute;
    }

    Object getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value instanceof Double ? Double.toString((Double) this.value) : (String) this.getValue();
    }

    public abstract double distance(Object a);

    public void update(Data data, Set<Integer> clusteredData) {
        data.computePrototype(clusteredData, getAttribute());
    }


}
