package repair.regen.utils;

public class Pair<K, V> {
    private K k;
    private V v;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getFirst() {
        return k;
    }

    public V getSecond() {
        return v;
    }

    public String toString() {
        return "Pair [" + k.toString() + ", " + v.toString() + "]";
    }

}
