import java.util.ArrayList;
import java.util.List;

/**
 * 组合模式(Composite Pattern)：
 * 组合多个对象形成树形结构以表示“整体-部分”的结构层次。组合模式对单个对象（即叶子对象）和组合对象（即容器对象）的使用具有一致性。
 * 组合模式又可以称为“整体-部分”(Part-Whole)模式，属于对象的结构模式，它将对象组织到树结构中，可以用来描述整体与部分的关系。
 */
public abstract class Component {
    public abstract void add(Component c);
    public abstract void remove(Component c);
    public abstract void operation();

    public static void main(String[] args) {
        Component l1 = new Leaf();
        Component l2 = new Leaf();
        Component c1 = new Composite();
        c1.add(l1);
        c1.add(l2);
        Component c2 = new Composite();
        c2.add(l1);
        c2.add(c1);
        c2.operation();
    }
}

class Leaf extends Component {
    public void add(Component c) {}
    public void remove(Component c) {}
    public void operation() {
        System.out.println("做操作");
    }
}

class Composite extends Component {
    private List<Component> list =new ArrayList<>();

    public void add(Component c) {
        list.add(c);
    }

    public void remove(Component c) {
        list.remove(c);
    }

    public void operation() {
        for (Component c : list) {
            c.operation();
        }
    }
}