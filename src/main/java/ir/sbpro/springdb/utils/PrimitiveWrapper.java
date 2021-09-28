package ir.sbpro.springdb.utils;

public class PrimitiveWrapper<T> {
    private T value;

    public PrimitiveWrapper(T value){
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
