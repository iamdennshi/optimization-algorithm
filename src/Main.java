import general.Function;
import fibonacci.Fibonacci;
import general.Pair;
import tangent.Tangent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//class Polylines {
//
//    public static double find(Function f, Function dfx, double a, double b, double eps) {
//
//        double L = Math.abs(dfx.solve(a)); // Константа Липшица
//        double step = 0.1;
//        double lastL;
//
//        while ( (lastL = Math.abs(dfx.solve(a + step))) > L) {
//            L = lastL;
//            step += 0.1;
//        }
//
//        System.out.println(L);
//        return 0.;
//    }
//}


class NelderMead {
    private static double calc(Function f, Pair p) {
        return f.solve(p.first(), p.second());
    }

    private static void sortByFunctionResult(Function f, List<Pair> pairs) {
        pairs.sort((o1, o2) -> {
            if (f.solve(o1.first(), o1.second()) - f.solve(o2.first(), o2.second()) >= 0) return 1;
            return -1;
        });
    }

    public static Pair find(Function f, Pair pointStart, Double eps) {
        // Параметры
        final Double A = 1.0;
        final Double B = 0.5;
        final Double C = 2.0;

        // Количество итераций
        int iter = 0;

        // Список координат точек
        List<Pair> x = new ArrayList<>(3);
        x.add(new Pair(pointStart.first(), pointStart.second()));
        x.add(new Pair(pointStart.first() + 0.1, pointStart.second()));
        x.add(new Pair(pointStart.first() + 0.05, pointStart.second() + 0.025));

        // Количество точек
        Integer n = x.size();

        // Значения функций в тех точках
        List<Double> fx = new ArrayList<>(3);
        fx.add(calc(f, x.get(0)));
        fx.add(calc(f, x.get(1)));
        fx.add(calc(f, x.get(2)));

        // Служат для остановки алгоритма
        Double sigma = 0.0;
        Double fi = 0.0;

        do {
            // Сортировка точек по их значениям функции
            // чтобы узнать где xh - наибольная точка, xl - наименьная, xg - близкая к наибольнему значению
            sortByFunctionResult(f, x);

            // Пересчитваем значения функции, т.к массив точек был отсортирован
            for (int i = 0; i < n; i++) {
                fx.set(i, calc(f, x.get(i)));
            }
            System.out.println(x);
            System.out.println(fx);

            // xc - центр тяжести
            Pair xc = x.get(0).clone();

            // Находим центр тяжести, не считая точку xh
            // начиная с 1-ой точки т.к xc был инициализирован 0-ой точкой
            for (int i = 1; i < n - 1; i++) {
                xc = Pair.add(xc, x.get(i));
            }
            xc = Pair.multiply(xc, 1.0/ (n - 1));

            Double fxc = calc(f, xc);

            // Отражение xh
            Pair xr = Pair.sub(Pair.multiply(xc, 2.0), x.get(n - 1));
            Double fxr = calc(f, xr);

            if (fxr < fx.get(0)) {
                // Направление удачное, увеличиваем шаг - растяжения
                Pair xe = Pair.add(Pair.multiply(xc, 1 - C), Pair.multiply(xr, B));
                Double fxe = calc(f, xe);

                if (fxe < fxr) {
                    fx.set(n - 1, fxe);
                    x.set(n - 1, xe.clone());
                } else {
                    fx.set(n - 1, fxr);
                    x.set(n - 1, xr.clone());
                }
                continue;

            } else if (fxr < fx.get(n - 2)) {
                fx.set(n - 1, fxr);
                x.set(n - 1, xr.clone());
                continue;

            } else if (fxr < fx.get(n - 1)) {
                fx.set(n - 1, fxr);
                x.set(n - 1, xr.clone());
            }

            Pair xs = Pair.add(Pair.multiply(x.get(n - 1), B), Pair.multiply(xc, 1 - B));
            Double fxs = calc(f, xs);

            if (fxs < fx.get(n - 1)) {
                x.set(n - 1, xs.clone());
            } else {
                Pair xl = x.get(0);
                Pair xi;
                for (int i = 1; i < n; i++) {
                    xi = x.get(i);
                    x.set(i, new Pair(
                            xl.first() + (xi.first() - xl.first()) / 2.0,
                            xl.second() + (xi.second() - xl.second()) / 2.0
                    ));
                }
            }
            // Расчет остакновки алгоритма
            fi = 0.0;
            for (int i = 0; i < n; i++) {
                fi += fx.get(i) / n;
            }
            sigma = 0.0;
            for (int i = 0; i < n; i++) {
                sigma +=  Math.pow(fx.get(i) - fi, 2) / n;
            }
            iter++;
        } while(Math.pow(sigma, 0.5) > eps);

        return x.get(0);
//        System.out.println("Iteration " + iter);
//        System.out.printf("f%s = %f", x.get(0).toString(), fx.get(0));


    }
}


public class Main {
    public static void main(String[] args) {
        //fibonacciExample();
        //tangentExample();

        //nelderMeadExample();

    }


    public static void nelderMeadExample() {
        System.out.println("Example of using the Nelder Mead method:");
        Function f = x -> x[0] * x[0] + 3 * x[1] * x[1] + Math.cos(x[0] + x[1]);
        Pair x = NelderMead.find(f, new Pair(0.0, 0.0), 1e-6);
        System.out.printf("f%s = %.2f\n\n", x.toString(), f.solve(x.first(), x.second()));
    }
    public static void fibonacciExample() {
        System.out.println("Example of using the Fibonacci method:");
        Function f = x -> -Math.pow(x[0],3) + 3 * (1 + x[0]) * (Math.log1p(x[0]) - 1);
        double x = Fibonacci.find(f, -0.5, 0.5,10);
        System.out.printf("f(%.2f) = %.2f\n\n", x, f.solve(x));
    }



    public static void tangentExample() {
        System.out.println("Example of using the Tangent method:");
        Function f = x -> -Math.pow(x[0],3) + 3 * (1 + x[0]) * (Math.log1p(x[0]) - 1);
        Function dfx = x -> -3 * x[0] * x[0] + 3 * Math.log1p(x[0]);
        double x = Tangent.find(f, dfx, -0.5, 0.5, 1e-6);
        System.out.printf("f(%.2f) = %.2f\n\n", x, f.solve(x));
    }
}