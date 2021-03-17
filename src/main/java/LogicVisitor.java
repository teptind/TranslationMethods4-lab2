import gen.Parser;

public class LogicVisitor {

    public Tree visitExpr(Parser.ExprContext context) {
        Tree tree = new Tree("expr");
        tree.h = context.h;
        tree.childes.add(visitLeft(context.left));
        if (context.right.left != null) {
            tree.childes.add(visitRight(context.right));
        }
        return tree;
    }

    private Tree visitRight(Parser.RightContext right) {
        Tree tree = new Tree("right");
        tree.h = right.h;
        if (right.STICK != null) {
            tree.childes.add(new Tree("OR", right.h + 1));
            tree.childes.add(visitLeft(right.left));
            if (right.right.left != null) {
                tree.childes.add(visitRight(right.right));
            }
        } else if (right.XOR != null) {
            tree.childes.add(new Tree("XOR", right.h + 1));
            tree.childes.add(visitLeft(right.left));
            if (right.right.left != null) {
                tree.childes.add(visitRight(right.right));
            }
        }
        return tree;
    }

    private Tree visitLeft(Parser.LeftContext left) {
        Tree tree = new Tree("left");
        tree.h = left.h;
        tree.childes.add(visitUnary(left.unary));
        if (left.tail.unary != null) {
            tree.childes.add(visitTail(left.tail));
        }
        return tree;
    }

    private Tree visitTail(Parser.TailContext tail) {
        Tree tree = new Tree("tail");
        tree.h = tail.h;
        tree.childes.add(new Tree("AND", tail.h + 1));
        tree.childes.add(visitUnary(tail.unary));
        if (tail.tail.unary != null) {
            tree.childes.add(visitTail(tail.tail));
        }
        return tree;
    }

    private Tree visitUnary(Parser.UnaryContext unary) {
        Tree tree = new Tree("unary");
        tree.h = unary.h;
        if (unary.NOT != null) {
            tree.childes.add(new Tree("NOT", unary.h + 1));
            tree.childes.add(visitUnary(unary.unary));
        } else {
            tree.childes.add(visitVar(unary.var));
        }
        return tree;
    }

    private Tree visitVar(Parser.VarContext var) {
        Tree tree = new Tree("var");
        tree.h = var.h;
        if (var.N != null) {
            tree.childes.add(new Tree("N: " + var.N.text, var.h + 1));
        } else {
            tree.childes.add(new Tree("(", var.h + 1));
            tree.childes.add(visitExpr(var.expr));
            tree.childes.add(new Tree(")", var.h + 1));
        }
        return tree;
    }


}
