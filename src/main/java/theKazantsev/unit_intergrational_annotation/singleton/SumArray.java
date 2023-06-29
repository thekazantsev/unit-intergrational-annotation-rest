package theKazantsev.unit_intergrational_annotation.singleton;

public class SumArray {

    public static double sum(double[] numbers) {
        double result = 0;
        for (double i : numbers) result += i;
        return result;
    }
}
