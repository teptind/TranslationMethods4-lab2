import lab4.DataDescriptor;
import org.antlr.v4.runtime.RuleContext;

import static lab4.DataDescriptor.*;

import static lab4.Utils.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SampleGrammarVisitor {

    private List<String> code;

    public DataDescriptor getDescriptor(SampleParser.GramContext context) {

        List<Leaf> leafs = context.token().stream().map(this::getLeaf).collect(Collectors.toList());
        List<Node> nodes = context.expression().stream().map(this::getNode).collect(Collectors.toList());

        return setFirstAndFollow(new DataDescriptor(leafs, nodes));
    }

    private DataDescriptor setFirstAndFollow(DataDescriptor descriptor) {

        Map<String, Set<String>> first = new HashMap<>();
        List<Rule> rules = descriptor.getNodes().stream().flatMap(node -> node.rules.stream()).collect(Collectors.toList());

        setFirst(descriptor, first, rules);

        Map<String, Set<String>> follow = new HashMap<>();
        setFollow(descriptor, first, rules, follow);

        descriptor.allRules = rules;
        descriptor.first = first;

        for (DataDescriptor.Node node: descriptor.getNodes()) {
            String name = node.getName();
            node.first = first.get(name);
            node.follow = follow.get(name);
        }

        return descriptor;
    }

    private void setFollow(DataDescriptor descriptor, Map<String, Set<String>> first, List<Rule> rules, Map<String, Set<String>> follow) {
        Predicate<String> isNode = (data) -> descriptor.getNodes().stream().anyMatch(d -> d.getName().equals(data));
        Predicate<String> isLeaf = (data) -> descriptor.getLeafs().stream().anyMatch(d -> d.getName().equals(data)) || data.equals("");

        descriptor.getNodes().forEach(node -> follow.put(node.getName(), new HashSet<>()));
        follow.get("expr").add("$");


        boolean changed = true;
        while (changed) {
            changed = false;
            for (Rule ruleA : rules) {
                String aName = ruleA.from;
                for (int i = 0; i < ruleA.to.size(); i++) {
                    String bName = ruleA.to.get(i);
                    if (isNode.test(bName)) {
                        Set<String> bSet = follow.get(bName);
                        if (i < ruleA.to.size() - 1) {
                            String gammaName = ruleA.to.get(i + 1);
                            Set<String> gammaFirst = new HashSet<>();
                            if (isLeaf.test(gammaName)) {
                                gammaFirst.add(gammaName);
                            } else {
                                gammaFirst = first.get(gammaName);
                            }
                            boolean result = bSet.addAll(gammaFirst);
                            if (gammaFirst.contains("")) {
                                result |= bSet.addAll(follow.get(aName));
                            }
                            changed |= result;
                        } else {
                            changed |= bSet.addAll(follow.get(aName));
                        }
                    }
                }
            }
        }
    }

    private void setFirst(DataDescriptor descriptor, Map<String, Set<String>> first, List<Rule> rules) {
        Predicate<String> isLeaf = (data) -> descriptor.getLeafs().stream().anyMatch(d -> d.getName().equals(data)) || data.equals("");
        descriptor.getNodes().forEach(node -> first.put(node.getName(), new HashSet<>()));
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Rule currentRule : rules) {
                Set<String> setFrom = first.get(currentRule.from);
                if (!currentRule.to.isEmpty()) {
                    String nextRuleName = currentRule.to.get(0);
                    if (isLeaf.test(nextRuleName)) {
                        if (!setFrom.contains(nextRuleName)) {
                            changed = true;
                            setFrom.add(nextRuleName);
                        }
                    } else {
                        Set<String> setTo = first.get(nextRuleName);
                        boolean result = setFrom.addAll(setTo);
                        if (result) {
                            changed = true;
                        }
                    }
                }
            }
        }
    }


    private Node getNode(SampleParser.ExpressionContext expressionContext) {
        String name = expressionContext.NODE().getText();
        code = new ArrayList<>();
        List<Rule> rules = visitDeclaration(name, expressionContext.declaration());

        SampleParser.Attr_initContext attr_initContext = expressionContext.attr_init();
        String init = "";
        if (attr_initContext != null && attr_initContext.PHRASE() != null) {
            init = formatContent(attr_initContext.PHRASE().getText());
        }
        SampleParser.Attr_parentContext attr_parentContext = expressionContext.attr_parent();
        String nasCode = "";
        if (attr_parentContext != null) {
            nasCode = formatContent(attr_parentContext.PHRASE().getText());
        }
        Node node = new Node(name, init, rules, code, nasCode);
        if (expressionContext.declaration().declaration_empty() != null) {
            node.canBeEmpty = true;
            rules.add(new Rule(name, List.of("")));
        }
        return node;
    }

    private List<Rule> visitDeclaration(String name, SampleParser.DeclarationContext declaration) {
        return declaration.declaration_one().stream().map(x -> parseOneRule(name, x)).collect(Collectors.toList());
    }

    private Rule parseOneRule(String name, SampleParser.Declaration_oneContext context) {
        if (context.syntez_attr() != null) {
            code.add(formatContent(context.syntez_attr().PHRASE().getText()));
        }
        return parseChain(name, context.chain());
    }

    private Rule parseChain(String name, SampleParser.ChainContext chain) {
        return new Rule(name, chain.name().stream()
                .map(RuleContext::getText)
                .collect(Collectors.toList()));
    }


    private Leaf getLeaf(SampleParser.TokenContext context) {
        String name = context.LEAF().getText();
        String regexp = formatContent(context.PHRASE().getText());
        return new Leaf(name, regexp);
    }


}
