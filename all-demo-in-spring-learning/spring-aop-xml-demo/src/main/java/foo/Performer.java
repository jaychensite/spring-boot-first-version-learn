package foo;


public class Performer implements Perform {
    @Override
    public void sing() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("男孩在唱歌");

    }

    public void eat() {
        System.out.println("男孩在吃饭");
    }



}
