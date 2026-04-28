package data;

import java.io.Serializable;

abstract class Attribute implements Serializable {
    String name; //nome simbolico dell'attributo
    int index; //identificativo numerico dell'attributo

    Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public Attribute() { }

    String getName() {
        return this.name;
    }

    int getIndex() {
        return this.index;
    }

    @Override
    public String toString() {
        return getName();
    }

}
