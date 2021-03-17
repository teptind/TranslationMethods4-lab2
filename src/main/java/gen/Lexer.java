package gen;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private String data;

    private final Map<Pattern, Leaf> map;

    public Lexer(String data) {
        this.data = data.replaceAll("[ \n\t]", "");
        map = new HashMap<>();
        map.put(Pattern.compile("([a-z]).*"), Leaf.N);
map.put(Pattern.compile("(\\|\\|).*"), Leaf.OR);
map.put(Pattern.compile("(&).*"), Leaf.AND);
map.put(Pattern.compile("(\\|).*"), Leaf.STICK);
map.put(Pattern.compile("(\\().*"), Leaf.LP);
map.put(Pattern.compile("(\\)).*"), Leaf.RP);
map.put(Pattern.compile("(\\^).*"), Leaf.XOR);
map.put(Pattern.compile("(!).*"), Leaf.NOT);

    }

    public List<Token> parseAll() {
        List<Token> tokens = new ArrayList<>();
        while (true) {
            Token current = getToken();
            tokens.add(current);
            if (current.leaf == Leaf.END) {
                return tokens;
            }
        }
    }

    private Token getToken() {
        if (data.isEmpty()) {
            return new Token(Leaf.END);
        }

        for (Map.Entry<Pattern, Leaf> entry : map.entrySet()) {
            Pattern pat = entry.getKey();
            Leaf leaf = entry.getValue();
            Matcher matcher = pat.matcher(data);
            if (matcher.matches() && !matcher.group().isEmpty()) {
                String result = matcher.group(1);
                int len = result.length();
                if (len < data.length()) {
                    data = data.substring(len);
                } else {
                    data = "";
                }
                return new Token(leaf, result);
            }

        }

        return new Token(Leaf.END);
    }

}

