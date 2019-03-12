package homework.util;

public class Triplet<T, K, V> {
    private T type;
    private K key;
    private V value;

    public Triplet(T t, K k, V v) {
        this.type = t;
        this.key = k;
        this.value = v;
    }

    public T getFirst() {
        return type;
    }

    public K getSecond() {
        return key;
    }

    public V getThird() {
        return value;
    }
}
