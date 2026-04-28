package data;

public class ContinuousItem extends Item {
    public ContinuousItem(Attribute attribute, Double value) {
        super(attribute, value);
    }

    @Override
    public double distance(Object a) {
        //ContinuousAttribute o = (ContinuousAttribute) this.getAttribute();
        //return Math.abs(o.getScaledValue((Double) this.getValue()) - o.getScaledValue((Double) ((ContinuousItem)a).getValue()));
        ContinuousItem other = (ContinuousItem) a;

        ContinuousAttribute thisAttr = ((ContinuousAttribute) this.getAttribute());
        ContinuousAttribute otherAttr = ((ContinuousAttribute) other.getAttribute());

        double thisScalVal = thisAttr.getScaledValue((double) this.getValue());
        double otherScalVal = otherAttr.getScaledValue((double) other.getValue());

        return Math.abs(thisScalVal - otherScalVal);
    }
}
