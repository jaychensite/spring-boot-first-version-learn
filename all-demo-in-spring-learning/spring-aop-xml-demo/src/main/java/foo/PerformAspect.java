package foo;

public class PerformAspect {
    public void beforePerform() {
        System.out.println("表演之前要整理衣服");
    }

    public void afterPerform() {
        System.out.println("表演之后要行礼");
    }
}
