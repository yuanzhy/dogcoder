import java.util.ArrayList;

public abstract class Visitor {
    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    public abstract void visit(Apple apple);

    public abstract void visit(Book book);
}

class Customer extends Visitor {
    @Override
    public void visit(Apple apple) {
        System.out.println("顾客" + name + "选苹果。");
    }
    @Override
    public void visit(Book book) {
        System.out.println("顾客" + name + "买书。");
    }
}

class Saler extends Visitor {
    @Override
    public void visit(Apple apple) {
        System.out.println("收银员" + name + "给苹果过秤，然后计算其价格。");
    }
    @Override
    public void visit(Book book) {
        System.out.println("收银员" + name + "直接计算书的价格。");
    }
}

class BuyBasket implements Product {
    private ArrayList<Product> list = new ArrayList<>();

    @Override
    public void accept(Visitor visitor) {
        for (Product product : list) {
            product.accept(visitor);
        }
    }

    public void addProduct(Product product) {
        list.add(product);
    }

    public void removeProduct(Product product) {
        list.remove(product);
    }
}

