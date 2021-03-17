
package gen;

public class Token {

    public Leaf leaf;

    public String text = "";

    public Token(Leaf leaf, String text) {
        this.leaf = leaf;
        this.text = text;
    }

    public Token(Leaf leaf) {
        this.leaf = leaf;
    }

    @Override
    public String toString() {
        return leaf.name() + " : [" + text + "] ";
    }
}



