/**
 * 单例模式 - 饱汉式
 */
public class Singleton {
    private Singleton() {}

    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final Singleton INSTANCE = new Singleton();
    }
}