import gen.Lexer;
import gen.Parser;
import gen.Token;
import lab4.EnumPrinter;
import lab4.LexerPrinter;
import lab4.ParserPrinter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {
        stuff();

    }

    private static void stuff() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String data;
        try (InputStream is = loader.getResourceAsStream("lab2.g")) {
            data = new String(is.readAllBytes());
        }

        var lexer = new SampleLexer(CharStreams.fromString(data));
        TokenStream tokens = new CommonTokenStream( lexer );
        SampleParser parser = new SampleParser( tokens );
        var gram = new SampleGrammarVisitor().getDescriptor(parser.gram());

        new EnumPrinter().print(gram);
        new LexerPrinter().print(gram);
        new ParserPrinter().print(gram);

        String input = "z ^ a";
        Lexer lexerS = new Lexer(input);

        List<Token> tokenList = lexerS.parseAll();
        var exprContext = new Parser(tokenList).parseExpr();
        exprContext.process();

        Tree result = new LogicVisitor().visitExpr(exprContext);

        result.print();

    }

    private static <T> void print(T e) {
        System.out.println(e);

    }


}
