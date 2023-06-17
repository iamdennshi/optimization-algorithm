package tangent;

/* f(x) = ax + b*/
final class Line {
    private double a;
    private double b;

    Line(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double solve(double x) {
        return a * x + b;
    }

    // Вернет x если две прямые пересекаются, иначе null;
    public Double findPointOfIntersection(Line l) {
        if (this.a - l.a == 0) return null;
        return (l.b - this.b) / (this.a - l.a);
    }

    @Override
    public String toString() {
        return String.format("f(x) = %fx + %f", a, b);
    }
}