import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 代理模式(Proxy Pattern) ：
 * 给某一个对象提供一个代理，并由代理对象控制对原对象的引用。
 * 代理模式的英文叫做Proxy或Surrogate，它是一种对象结构型模式。
 */
public class Client {
    public static void main(String args[]) {

        InvocationHandler handler = new DynamicProxy(new RealSubjectA());
        Subject subject = (Subject) Proxy.newProxyInstance(Subject.class.getClassLoader(), new Class[]{Subject.class}, handler);
        subject.request();

        System.out.println("------------------------------");

        handler = new DynamicProxy(new RealSubjectB());
        subject = (Subject) Proxy.newProxyInstance(Subject.class.getClassLoader(), new Class[]{Subject.class}, handler);
        subject.request();
    }
}