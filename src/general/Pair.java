package general;

public record Pair(Double first, Double second) implements Cloneable {
    public static Pair add(Pair a, Pair b) {
        return new Pair(a.first + b.first, a.second + b.second);
    }
    public static Pair multiply(Pair a, Double scalar) {
        return new Pair(a.first * scalar, a.second * scalar);
    }
    public static Pair sub(Pair a, Pair b) {
        return new Pair(a.first - b.first, a.second - b.second);
    }
    @Override
    public String toString() {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }

    @Override
    public Pair clone() {
        return new Pair(this.first, this.second);
    }
}
