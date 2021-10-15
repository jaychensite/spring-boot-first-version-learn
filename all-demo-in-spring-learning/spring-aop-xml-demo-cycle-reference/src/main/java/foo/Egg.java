package foo;

public class Egg {
    private Chick chick;

    public Chick getChick() {
        return chick;
    }

    public void setChick(Chick chick) {
        this.chick = chick;
    }

    /**
     * 孵化
     */
    public void incubate() {
        System.out.println("开始孵化");
    }

}
