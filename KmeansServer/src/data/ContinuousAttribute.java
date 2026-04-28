package data;

class ContinuousAttribute extends Attribute {
    private final double max, min; //Rappresentano gli estremi dell'intervallo di valori (dominio)
                     //che l'attributo puo' realmente assumere

    ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    public double getScaledValue(double v) {
        return (v - min) / (max - min);
    }

}
