package lab4;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static lab4.Maker.makeSource;
import static lab4.Utils.toName;

public class ParserPrinter {

    private int index = 0;

    public void print(DataDescriptor descriptor) {
        String nested = makeNestedContent(descriptor);
        String functions = makeFunctions(descriptor);
        String result = nested + functions;
        String content = String.format(template, result);
        makeSource("Parser", content);
    }

    private String makeNestedContent(DataDescriptor descriptor) {
        NestedCreator nestedCreator = new NestedCreator();
        String nestedNodes = descriptor.getNodes().stream().map(node -> nestedCreator.getNodesPrint(descriptor, node)).collect(Collectors.joining("\n"));
        String nestedLeaves = descriptor.getLeafs().stream().map(node -> nestedCreator.getLeavesPrint(descriptor, node)).collect(Collectors.joining("\n"));
        return nestedNodes + "\n" + nestedLeaves + "\n";
    }

    private String makeFunctions(DataDescriptor descriptor) {
        String nodeFunctions = descriptor.getNodes().stream().map(n -> makeFunctionNode(descriptor, n)).collect(Collectors.joining("\n")) + "\n";
        String leafFunctions = descriptor.getLeafs().stream().map(leaf -> makeFunctionLeaf(descriptor, leaf)).collect(Collectors.joining("\n")) + "\n";
        return nodeFunctions + "\n" + leafFunctions;
    }

    private String makeFunctionLeaf(DataDescriptor descriptor, DataDescriptor.Leaf leaf) {
        String funTemplate = """
                    public %sContext parse%s() {
                        %sContext result =  new %sContext();
                        result.text = data.get(position++).text;
                        return result;
                    }
                """;
        String name = toName(leaf.name);
        return String.format(funTemplate, name, name, name, name);
    }

    private String makeFunctionNode(DataDescriptor descriptor, DataDescriptor.Node node) {
        String funTemplate = """
                    public %sContext parse%s() {
                        %s
                    }
                """;
        String name = toName(node.getName());

        String content = makeFunctionContent(descriptor, node);

        return String.format(funTemplate, name, name, content);
    }


    private String makeFunctionContent(DataDescriptor descriptor, DataDescriptor.Node node) {
        index = 0;
        String resultType = toName(node.name);
        String switchTemplate = """
                switch(data.get(position).leaf) {
                    %s
                }
                %s
                """;
        String caseTemplate = """
                    case %s:
                        %sContext result%s = new %sContext();
                        %s
                        return result%s;
                """;
        StringBuilder builder = new StringBuilder();
        for (String termName : node.first) {
            if (!termName.equals("")) {
                builder.append(String.format(caseTemplate, termName, resultType, index, resultType, makeCaseForRule(descriptor, node, termName), index));
                index++;
            }
        }
        boolean needsException = false;
        if (node.first.contains("")) {
            processDefaultBlock(node, builder);
        } else {
            needsException = true;
        }
        String exception = needsException ? "throw new IllegalStateException(\"Unexpected token \" + data.get(position).leaf.name() + \" at position: \" + position);" : "";
        return String.format(switchTemplate, builder.toString(), exception);
    }

    private void processDefaultBlock(DataDescriptor.Node node, StringBuilder builder) {
        String defaultTemplate = """
                default:
                    %sContext result%s = new %sContext();
                    result%s.localIndexRule = %s;
                    return result%s;                    
                """;
        int number = 0;
        for (int i = 0; i < node.rules.size(); i++) {
            List<String> toList = node.rules.get(i).to;
            if (!toList.isEmpty() && toList.get(0).equals("")) {
                number = i;
                break;
            }
        }
        String name = toName(node.name);
        builder.append(String.format(defaultTemplate, name, index, name, index, number, index));
    }

    private String makeCaseForRule(DataDescriptor descriptor, DataDescriptor.Node node, String termName) {
        DataDescriptor.Rule nextRule = null;
        int number = 0;
        for (DataDescriptor.Rule rule : node.rules) {
            String nextStateName = rule.to.get(0);
            Set<String> firstByNextState = descriptor.first.get(nextStateName);
            if (firstByNextState == null && nextStateName.equals(termName)) {
                nextRule = rule;
                break;
            }
            if (firstByNextState != null && firstByNextState.contains(termName)) {
                nextRule = rule;
                break;
            }
            number++;
        }
        if (nextRule == null) {
            throw new IllegalStateException("null");
        }
        String settingNumber = "result" + index + ".localIndexRule = " + number + ";\n";
        return settingNumber + nextRule.to.stream().map(toRuleName -> {
            Integer count = node.fieldCounter.get(toRuleName);
            if (count == null || count == 1) {
                return "result" + index + "." + toRuleName + " = parse" + toName(toRuleName) + "();";
            } else {
                return "result" + index + "." + toRuleName + ".add(parse" + toName(toRuleName) + "());";
            }
        }).collect(Collectors.joining("\n"));
    }

    private static final String template = """
            package gen;
                        
            import java.util.*;
                        
            public class Parser {
                        
                private int position = 0;
                
                private List<Token> data;
                
                public Parser(List<Token> data) {
                    this.data = data;
                }
                
                %s
                        
            }
            """;

}
