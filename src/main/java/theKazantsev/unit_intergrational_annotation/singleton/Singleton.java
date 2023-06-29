package theKazantsev.unit_intergrational_annotation.singleton;

public class Singleton {
    public static final Singleton instance = new Singleton();

    static {
        System.out.println("Singleton.class initiation");
    }

    private Singleton() {

    }
}
