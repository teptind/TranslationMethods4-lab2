package lab4;

import java.util.List;
import java.util.stream.Collectors;

import static lab4.Maker.makeSource;

public class EnumPrinter {

    public void print(DataDescriptor dataDescriptor) {
        List<DataDescriptor.Leaf> leafs = dataDescriptor.getLeafs();
        var enumCases = leafs.stream().map(l -> l.getName().toUpperCase()).collect(Collectors.joining(","));
        String tempalte = """
                            
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
                               
                                
                            
                """;
        makeSource("Token", tempalte);

        String enumTemplate = """
                 package gen;
                 
                 enum Leaf{
                    %s, END
                }
                """;
        makeSource("Leaf", String.format(enumTemplate, enumCases));
    }


}
