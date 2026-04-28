package data;

class DiscreteItem extends Item {
    public DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute, value);
    }

    @Override
    public double distance(Object a) {
        return getValue().equals(a) ? 0 : 1;
    }
}
