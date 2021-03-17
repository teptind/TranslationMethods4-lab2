package lab4;

import java.util.HashSet;
import java.util.List;
import java.util.*;

public final class DataDescriptor {

    public Map<String, Set<String>> first;
    private final List<Leaf> leafs;
    private final List<Node> nodes;
    public List<Rule> allRules;

    public DataDescriptor(List<Leaf> leafs, List<Node> nodes) {
        this.leafs = leafs;
        this.nodes = nodes;
    }

    private static class Named {
        protected final String name;

        public boolean canBeEmpty = false;

        public Named(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    public static final class Node extends Named {

        public Set<String> first = new HashSet<>();

        public Set<String> follow = new HashSet<>();

        public final String initVariables;

        public final List<Rule> rules;

        public final List<String> rulesCode;

        public final String naslCode;

        public final Map<String, Integer> fieldCounter;

        public final Map<String, Map<String, Integer>> rulesFiledCounter;

        public Node(String name, String initVariables, List<Rule> rules, List<String> rulesCode, String naslCode) {
            super(name);
            this.initVariables = initVariables;
            this.rules = rules;
            this.rulesCode = rulesCode;
            this.naslCode = naslCode;
            fieldCounter = new HashMap<>();
            rulesFiledCounter = new HashMap<>();
            for (Rule rule : rules) {
                var tempMap = new HashMap<String, Integer>();
                for (String ruleName : rule.to) {
                    tempMap.merge(ruleName, 1, Integer::sum);
                }
                rulesFiledCounter.put(rule.from, tempMap);
                for (Map.Entry<String, Integer> entry: tempMap.entrySet()) {
                    fieldCounter.merge(entry.getKey(), entry.getValue(), Math::max);
                }
            }
        }

    }

    public static final class Leaf extends Named {

        public final String regularExpression;

        public Leaf(String name, String regularExpression) {
            super(name);
            this.regularExpression = regularExpression;
        }

    }

    public static class Rule {

        public final String from;

        public final List<String> to;

        public Rule(String from, List<String> to) {
            this.from = from;
            this.to = to;
        }
    }

    public List<Leaf> getLeafs() {
        return leafs;
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
