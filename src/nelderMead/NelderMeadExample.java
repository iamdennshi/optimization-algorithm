package nelderMead;

import general.Function;

import java.util.ArrayList;

public class NelderMeadExample {
    public static void main(String[] args) {
        System.out.println("Example of using the Nelder Mead method:");
        Function f = x -> x[0] * x[0] + 2 * x[1] * x[1] + Math.cos(x[0] + x[1]);
        ArrayList<Double> arr = new ArrayList<>(2);
        arr.add(0.0);
        arr.add(0.0);
        NelderMead.find(f, arr, 1e-6);





//        Pair x = NelderMead.find(f, new Pair(0.0, 0.0), 1e-6);
//
//        if (!x.equals(new Pair(0.0,0.0))) {
//            System.err.printf("f%s = %.2f MIN POINT IN (0.0, 0.0)\n\n", x.toString(), f.solve(x.first(), x.second()));
//        } else {
//            System.out.println("1. TEST COMPLETE!");
//        }
    }
}
