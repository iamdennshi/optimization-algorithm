package tangent;

import general.Function;

final public class Tangent {
    public static double find(Function f, Function dfx, double a, double b, double eps) {
        int i = 0; // iteration
        double startX;
        boolean isEnd = false;
        Function g = x -> f.solve(x[1]) + dfx.solve(x[1]) * (x[0] - x[1]);

        // Касательная от точек a и b
        Line la = new Line(dfx.solve(a), f.solve(a) + dfx.solve(a) * -a);
        Line lb = new Line(dfx.solve(b), f.solve(b) + dfx.solve(b) * -b);
        Line lc;

        do {
            // Находим точку пересечения двух кастельных
            startX = la.findPointOfIntersection(lb);

            // Строим новую касательную от точки пересечения
            lc = new Line(dfx.solve(startX), f.solve(startX) + dfx.solve(startX) * -startX);

            // Находим наибольшее пересечение старых кастельных к новой
            // и заменяем старую касательную новой
            double xla = la.findPointOfIntersection(lc);
            double xlb = lb.findPointOfIntersection(lc);

            isEnd = Math.abs(f.solve(startX) - la.solve(startX)) < eps;
            if (la.solve(xla) > lb.solve(xlb)) {
                la = lc;
            } else {
                lb = lc;
            }
        } while (!isEnd);
        return startX;
    }
}