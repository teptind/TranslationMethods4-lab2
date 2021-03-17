package lab4;

import java.util.stream.Collectors;
import java.util.*;

import static lab4.Utils.toName;

public class NestedCreator {

    public String getNodesPrint(DataDescriptor descriptor, DataDescriptor.Node node) {
        String template = """
                    public static class %sContext {
                        
                        private int localIndexRule = -1;
                        
                        public String text;
                    
                        %s
                    
                        public void process() {
                            %s
                        }
                    
                    }
                """;
        String name = toName(node.getName());
        String declaration = node.initVariables;
        String fields = makeFields(node);
        return String.format(template, name, declaration + "\n" + fields, makeProcess(descriptor, node));
    }

    private String makeProcess(DataDescriptor descriptor, DataDescriptor.Node node) {
        StringBuilder calling = new StringBuilder();
        for (Map.Entry<String, Integer> e : node.fieldCounter.entrySet()) {
            String name = e.getKey();
            if (descriptor.getLeafs().stream().anyMatch(l -> l.getName().equals(name))) {
                continue;
            }
            if (e.getValue() == 1) {
                calling.append("if (").append(name).append(" != null) {\n");
                calling.append(makeExtendedCode(descriptor, name)).append("\n");
                calling.append(name).append(".process(); \n}\n");
            } else {
                calling.append(name).append(".stream()\n.filter(Object::notNull)\n.forEach(x -> x.process());\n");
            }
        }

        String switchTemplate = """
                switch(localIndexRule) {
                    %s
                }
                """;
        String caseTemplate = """
                case %s:
                    %s
                    break;
                """;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < node.rulesCode.size(); i++) {
            builder.append(String.format(caseTemplate, i, node.rulesCode.get(i)));
        }
        return calling.toString() + String.format(switchTemplate, builder.toString()) + "\n";
    }

    private String makeExtendedCode(DataDescriptor descriptor, String name) {
        DataDescriptor.Node toNode = null;
        Optional<DataDescriptor.Node> child = descriptor.getNodes().stream().filter(node -> node.getName().equals(name)).findFirst();
        if (child.isPresent()) {
            DataDescriptor.Node childNode = child.get();
            return childNode.naslCode.replace("this", name);
        } else {
            return "";
        }
    }

    private String makeFields(DataDescriptor.Node node) {
        return node.fieldCounter.entrySet().stream().map(e -> {
            String name = e.getKey();
            Integer count = e.getValue();
            return count == 1 ? "public " + toName(name) + "Context " + name + ";\n" : "private List<" + toName(name) + "Context> = new ArrayList<>();" + name + ";\n";
        }).collect(Collectors.joining());
    }

    public String getLeavesPrint(DataDescriptor descriptor, DataDescriptor.Leaf leaf) {
        String template = """
                    public static class %sContext {
                    
                        public String text;
                    
                    }
                """;
        String name = toName(leaf.getName());
        return String.format(template, name);
    }

}
