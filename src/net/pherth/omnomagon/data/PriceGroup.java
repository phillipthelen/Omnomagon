package net.pherth.omnomagon.data;

public enum PriceGroup {
    students(0), employees(1), guests(2);

    private final int _typeInteger;

    PriceGroup(int typeInteger) {
        _typeInteger = typeInteger;
    }

    public int getTypeInteger() {
        return _typeInteger;
    }
}
