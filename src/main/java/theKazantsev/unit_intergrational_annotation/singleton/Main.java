package theKazantsev.unit_intergrational_annotation.singleton;

public class Main {
    public static void main(String[] args) {

        Singleton instance1 = Singleton.instance;
        Singleton instance2 = Singleton.instance;
        Singleton instance3 = Singleton.instance;

        System.out.println(instance1.toString());
        System.out.println(instance2.toString());
        System.out.println(instance3.toString());


        System.out.println(SumArray.sum(new double[] {1, 2, 3}));

    }
}
