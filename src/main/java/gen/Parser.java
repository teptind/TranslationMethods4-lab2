package gen;

import java.util.*;

public class Parser {

    private int position = 0;

    private List<Token> data;

    public Parser(List<Token> data) {
        this.data = data;
    }

        public static class ExprContext {

        private int localIndexRule = -1;

        public String text;

        public int h = 1;
public LeftContext left;
public RightContext right;


        public void process() {
            if (left != null) {
left.h = h + 1;
left.process(); 
}
if (right != null) {
right.h = h + 1;
right.process(); 
}
switch(localIndexRule) {
    
}


        }

    }

    public static class RightContext {

        private int localIndexRule = -1;

        public String text;

        public int h = 0;
public LeftContext left;
public STICKContext STICK;
public XORContext XOR;
public RightContext right;


        public void process() {
            if (left != null) {
left.h = h + 1;
left.process(); 
}
if (right != null) {
right.h = h + 1;
right.process(); 
}
switch(localIndexRule) {
    
}


        }

    }

    public static class LeftContext {

        private int localIndexRule = -1;

        public String text;

        public int h = 0;
public TailContext tail;
public UnaryContext unary;


        public void process() {
            if (tail != null) {
tail.h = h + 1;
tail.process(); 
}
if (unary != null) {
unary.h = h + 1;
unary.process(); 
}
switch(localIndexRule) {
    
}


        }

    }

    public static class UnaryContext {

        private int localIndexRule = -1;

        public String text;

        public int h = 0;
public NOTContext NOT;
public VarContext var;
public UnaryContext unary;


        public void process() {
            if (var != null) {
var.h = h + 1;
var.process(); 
}
if (unary != null) {
unary.h = h + 1;
unary.process(); 
}
switch(localIndexRule) {
    
}


        }

    }

    public static class VarContext {

        private int localIndexRule = -1;

        public String text;

        public int h = 0;
public LPContext LP;
public ExprContext expr;
public RPContext RP;
public NContext N;


        public void process() {
            if (expr != null) {
expr.h = h + 1;
expr.process(); 
}
switch(localIndexRule) {
    
}


        }

    }

    public static class TailContext {

        private int localIndexRule = -1;

        public String text;

        public int h = 0;
public TailContext tail;
public ANDContext AND;
public UnaryContext unary;


        public void process() {
            if (tail != null) {
tail.h = h + 1;
tail.process(); 
}
if (unary != null) {
unary.h = h + 1;
unary.process(); 
}
switch(localIndexRule) {
    
}


        }

    }

    public static class NContext {

        public String text;

    }

    public static class ORContext {

        public String text;

    }

    public static class ANDContext {

        public String text;

    }

    public static class STICKContext {

        public String text;

    }

    public static class LPContext {

        public String text;

    }

    public static class RPContext {

        public String text;

    }

    public static class XORContext {

        public String text;

    }

    public static class NOTContext {

        public String text;

    }

    public ExprContext parseExpr() {
        switch(data.get(position).leaf) {
        case NOT:
        ExprContext result0 = new ExprContext();
        result0.localIndexRule = 0;
result0.left = parseLeft();
result0.right = parseRight();
        return result0;
    case LP:
        ExprContext result1 = new ExprContext();
        result1.localIndexRule = 0;
result1.left = parseLeft();
result1.right = parseRight();
        return result1;
    case N:
        ExprContext result2 = new ExprContext();
        result2.localIndexRule = 0;
result2.left = parseLeft();
result2.right = parseRight();
        return result2;

}
throw new IllegalStateException("Unexpected token " + data.get(position).leaf.name() + " at position: " + position);

    }

    public RightContext parseRight() {
        switch(data.get(position).leaf) {
        case STICK:
        RightContext result0 = new RightContext();
        result0.localIndexRule = 0;
result0.STICK = parseSTICK();
result0.left = parseLeft();
result0.right = parseRight();
        return result0;
    case XOR:
        RightContext result1 = new RightContext();
        result1.localIndexRule = 1;
result1.XOR = parseXOR();
result1.left = parseLeft();
result1.right = parseRight();
        return result1;
default:
    RightContext result2 = new RightContext();
    result2.localIndexRule = 2;
    return result2;

}


    }

    public LeftContext parseLeft() {
        switch(data.get(position).leaf) {
        case NOT:
        LeftContext result0 = new LeftContext();
        result0.localIndexRule = 0;
result0.unary = parseUnary();
result0.tail = parseTail();
        return result0;
    case LP:
        LeftContext result1 = new LeftContext();
        result1.localIndexRule = 0;
result1.unary = parseUnary();
result1.tail = parseTail();
        return result1;
    case N:
        LeftContext result2 = new LeftContext();
        result2.localIndexRule = 0;
result2.unary = parseUnary();
result2.tail = parseTail();
        return result2;

}
throw new IllegalStateException("Unexpected token " + data.get(position).leaf.name() + " at position: " + position);

    }

    public UnaryContext parseUnary() {
        switch(data.get(position).leaf) {
        case NOT:
        UnaryContext result0 = new UnaryContext();
        result0.localIndexRule = 0;
result0.NOT = parseNOT();
result0.unary = parseUnary();
        return result0;
    case LP:
        UnaryContext result1 = new UnaryContext();
        result1.localIndexRule = 1;
result1.var = parseVar();
        return result1;
    case N:
        UnaryContext result2 = new UnaryContext();
        result2.localIndexRule = 1;
result2.var = parseVar();
        return result2;

}
throw new IllegalStateException("Unexpected token " + data.get(position).leaf.name() + " at position: " + position);

    }

    public VarContext parseVar() {
        switch(data.get(position).leaf) {
        case LP:
        VarContext result0 = new VarContext();
        result0.localIndexRule = 1;
result0.LP = parseLP();
result0.expr = parseExpr();
result0.RP = parseRP();
        return result0;
    case N:
        VarContext result1 = new VarContext();
        result1.localIndexRule = 0;
result1.N = parseN();
        return result1;

}
throw new IllegalStateException("Unexpected token " + data.get(position).leaf.name() + " at position: " + position);

    }

    public TailContext parseTail() {
        switch(data.get(position).leaf) {
        case AND:
        TailContext result0 = new TailContext();
        result0.localIndexRule = 0;
result0.AND = parseAND();
result0.unary = parseUnary();
result0.tail = parseTail();
        return result0;
default:
    TailContext result1 = new TailContext();
    result1.localIndexRule = 1;
    return result1;

}


    }


    public NContext parseN() {
        NContext result =  new NContext();
        result.text = data.get(position++).text;
        return result;
    }

    public ORContext parseOR() {
        ORContext result =  new ORContext();
        result.text = data.get(position++).text;
        return result;
    }

    public ANDContext parseAND() {
        ANDContext result =  new ANDContext();
        result.text = data.get(position++).text;
        return result;
    }

    public STICKContext parseSTICK() {
        STICKContext result =  new STICKContext();
        result.text = data.get(position++).text;
        return result;
    }

    public LPContext parseLP() {
        LPContext result =  new LPContext();
        result.text = data.get(position++).text;
        return result;
    }

    public RPContext parseRP() {
        RPContext result =  new RPContext();
        result.text = data.get(position++).text;
        return result;
    }

    public XORContext parseXOR() {
        XORContext result =  new XORContext();
        result.text = data.get(position++).text;
        return result;
    }

    public NOTContext parseNOT() {
        NOTContext result =  new NOTContext();
        result.text = data.get(position++).text;
        return result;
    }



}
