import foo.OriginClass;


public class TestMainInJar {
    public static void main(String[] args) {
        System.out.println("TestMainInJar main()");
        new OriginClass().sayHello();
    }
}
