package nelderMead;

import general.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Метод Нелдера-Мида (метод диформируемого многогранника)
 * Вычисляет минимум функции от n переменных, на каждом шаге находятся значения вершин многогранника и
 * присходит смещение в область меньших значений
 */
public final class NelderMead {
    // Промяжуточная информация
    private static final boolean DEBUG = false;

    // Вычисление функции в точке p
    private static double calc(Function f, ArrayList<Double> p) {
        return f.solve(p.toArray(new Double[]{}));
    }

    // Сортировка по значению функции f для точек points
    private static void sortByFunctionResult(Function f, ArrayList<ArrayList<Double>> points) {
        points.sort((o1, o2) -> {
            if (calc(f, o1) - calc(f, o2) > 0) {
                return 1;
            }
            return -1;
        });
    }

    /**
     * @param f          Функция
     * @param pointStart Точка начала поиска из n компонент
     * @param eps        Погрешность результата, например 1e-6
     */
    public static ArrayList<Double> find(Function f, List<Double> pointStart, double eps) {
        // Параметры
        final double A = 1.0;
        final double B = 0.5;
        final double C = 2.0;

        // Количество итераций
        int iter = 0;

        // Количество точек
        final int n = pointStart.size() + 1;

        // Индексы точек xh - наибольная точка, xl - наименьная, xg - близкая к наибольнему значению
        final int xhIndex = n - 1;
        final int xlIndex = 0;
        final int xgIndex = xhIndex - 1;

        // Массив координат точек
        ArrayList<ArrayList<Double>> x = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            x.add(new ArrayList<Double>(n - 1));
        }

        // Инициализация массива кординатами точек
        for (int i = 0; i < n; i++) {
            int usedRand = i;
            for (int j = 0; j < n - 1; j++) {
                if (usedRand > 0) {
                    x.get(i).add(pointStart.get(j) + Math.random());
                    usedRand--;
                } else {
                    x.get(i).add(pointStart.get(j));
                }
            }
        }

        // Служат для остановки алгоритма
        double sigma = 0.0;
        double fi = 0.0;

        // Для хранения значений функции
        ArrayList<Double> fx;

        do {
            fx = new ArrayList<>(n);

            // Сортировка точек по их значениям функции
            // чтобы узнать где xh - наибольная точка, xl - наименьная, xg - близкая к наибольнему значению
            sortByFunctionResult(f, x);

            // Расчет значений функций
            for (int i = 0; i < n; i++) {
                fx.add(calc(f, x.get(i)));
            }

            if (DEBUG) {
                System.out.println("x:");
                for (int i = 0; i < n; i++) {
                    System.out.println(" " + (i + 1) + ". " + x.get(i));
                }
                System.out.println("fx: " + fx);
            }

            // xc - центр тяжести
            ArrayList<Double> xc = new ArrayList<>(n - 1);

            // Исключается точка xh и находится цент тяжести среди других точек
            for (int i = 0; i < n - 1; i++) {
                double tmp = 0.0;
                for (int j = 0; j < x.size() - 1; j++) {
                    tmp += x.get(j).get(i);
                }
                xc.add(tmp / (n - 1));
            }

            double fxc = calc(f, xc);
            if (DEBUG) {
                System.out.println("xc: " + xc);
                System.out.println("fxc: " + fxc);
            }

            // Отрожение точки xh относительно xc, получаем точку xr
            ArrayList<Double> xr = new ArrayList<>(n - 1);

            final double Ap1 = A + 1.0;
            for (int i = 0; i < n - 1; i++) {
                xr.add(Ap1 * xc.get(i) - x.get(xhIndex).get(i) * A);
            }

            double fxr = calc(f, xr);
            if (DEBUG) {
                System.out.println("xr: " + xr);
                System.out.println("fxr: " + fxr);
            }

            if (fxr < fx.get(xlIndex)) {
                // Направление удачное, увеличиваем шаг - растяжения
                ArrayList<Double> xe = new ArrayList<>(n - 1);
                for (int i = 0; i < n - 1; i++) {
                    xe.add(xc.get(i) * (1 - C) + C * xr.get(i));
                }
                double fxe = calc(f, xe);

                if (fxe < fxr) {
                    x.set(xhIndex, xe);
                } else {
                    x.set(xhIndex, xr);
                }
                continue;

            } else if (fxr < fx.get(xgIndex)) {
                fx.set(xhIndex, fxr);
                x.set(xhIndex, xr);
                continue;

            } else if (fxr < fx.get(xhIndex)) {
                fx.set(xhIndex, fxr);
                x.set(xhIndex, xr);
            }

            ArrayList<Double> xk = new ArrayList<>(n - 1);
            for (int i = 0; i < n - 1; i++) {
                xk.add((B * x.get(xhIndex).get(i) + B * xc.get(i)));
            }
            double fxk = calc(f, xk);

            if (fxk < fx.get(xhIndex)) {
                // сжатие
                x.set(xhIndex, xk);
            } else {
                // редукция
                ArrayList<Double> xl = new ArrayList<>(x.get(xlIndex));
                ArrayList<Double> xi = new ArrayList<>(Collections.nCopies(n - 1, 0.0));
                ArrayList<Double> xm;

                for (int i = 0; i < n; i++) {
                    xm = new ArrayList<>(x.get(i));

                    for (int j = 0; j < n - 1; j++) {
                        xi.set(j, xl.get(j) + (xm.get(j) - xl.get(j)) / 2.0);
                    }
                    x.set(i, xi);
                }
            }
            // Расчет остановки алгоритма
            fi = 0.0;
            for (int i = 0; i < n; i++) {
                fi += fx.get(i) / n;
            }
            sigma = 0.0;
            for (int i = 0; i < n; i++) {
                sigma +=  Math.pow(fx.get(i) - fi, 2) / n;
            }
            iter++;
        } while (Math.sqrt(sigma) > eps);

        if (DEBUG) {
            System.out.println("Iteration " + iter);
            System.out.printf("f%s = %f\n", x.get(0).toString(), fx.get(0));
        }
        return x.get(0);
    }
}