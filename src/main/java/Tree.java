import java.util.ArrayList;
import java.util.List;

public class Tree {

    public String name;

    public int h = 0;

    public List<Tree> childes = new ArrayList<>();

    public Tree(String name, int h) {
        this.name = name;
        this.h = h;
    }

    public Tree(String name) {
        this.name = name;
    }

    public void print() {
        System.out.println(toString());
        childes.forEach(Tree::print);
    }

    @Override
    public String toString() {
        return " ".repeat(h * 5) + name;
    }
}
