/**
 * (Visitor Pattern)
 * 表示一个作用于某对象结构中的各元素的操作，
 * 它使我们可以在不改变各元素的类的前提下定义作用于这些元素的新操作。
 * 访问者模式是一种对象行为型模式。
 */
public class Client {
    public static void main(String a[]) {
        Product b1 = new Book();
        Product b2 = new Book();
        Product a1 = new Apple();

        BuyBasket basket = new BuyBasket();
        basket.addProduct(b1);
        basket.addProduct(b2);
        basket.addProduct(a1);

        Visitor visitor = new Customer();
        visitor.setName("顾客");
        basket.accept(visitor);

        visitor = new Customer();
        visitor.setName("收银员");
        basket.accept(visitor);
    }
}