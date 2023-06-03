
public class Main {
    public static void main(String[] args) {
        System.out.println("Example of using the Fibonacci method:");
        Function f = x -> 4.2 * x[0] * x[0] + 23./x[0];
        double x = Fibonacci.find(f, 0, 8,6);
        System.out.printf("f(%.2f) = %.2f\n\n", x, f.solve(x));
    }

}