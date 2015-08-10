package org.mlnlp.nlp.answerextraction.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import org.apache.commons.lang3.StringUtils;
import org.maochen.nlp.datastructure.DNode;
import org.maochen.nlp.datastructure.DTree;
import org.maochen.nlp.datastructure.LangLib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by qingqingcai on 8/10/15.
 */
public class ShortAnswerExtractionUtils {
    private static final Set<String>
        WILDCARD_POS = ImmutableSet
        .of(LangLib.POS_WP, LangLib.POS_WPS, LangLib.POS_WRB, LangLib.POS_WDT);

    public static DNode getNearestVerbAncestor(final DNode node) {
        DNode iterNode = node;
        DNode verbAncestor = null;
        while (iterNode != null) {
            iterNode = iterNode.getHead();
            if (iterNode.getPOS().startsWith(LangLib.POS_VB)) {
                verbAncestor = iterNode;
                break;
            }
        }
        return verbAncestor;
    }

    public static DNode findWildcardNode(final DTree tree) {
        DNode whNode = tree.stream().filter(x -> WILDCARD_POS.contains(x.getPOS())).findFirst().orElse(null);
        return tree.getRoots().contains(getNearestVerbAncestor(whNode)) ? whNode : null;
    }

    public static Comparator<DNode>
        sortDNodeById = (n1, n2) -> Integer.compare(n1.getId(), n2.getId());

    public static String getRawStringFromNode(DNode node) {
        if (node == null) {
            return StringUtils.EMPTY;
        }

        return ShortAnswerExtractionUtils.bfs(node).stream()
            .sorted((n1, n2) -> Integer.compare(n1.getId(), n2.getId()))
            .map(DNode::getForm).reduce((s1, s2) -> s1 + StringUtils.SPACE + s2)
            .orElse(StringUtils.EMPTY);
    }

    /**
     * ************************************************************
     * Breadth-First search for the given node
     *
     * @param node root node that searching starts.
     * @return the returned list contains root node.
     */
    public static List<DNode> bfs(final DNode node) {
        List<DNode> result = new ArrayList<>();

        if (node != null) {
            Queue<DNode> q = new LinkedList<>();
            q.add(node);
            while (!q.isEmpty()) {
                DNode current = q.poll();
                result.add(current);
                current.getChildren().stream().forEachOrdered(q::add);
            }
        }

        return result;
    }

    /**
     * ************************************************************
     * Get the complete pairwise connectivity DTree with a value for
     * each node pair.
     *
     * @param questionDTree
     * @param sentenceDTree
     * @return Table nodeInQuestion, nodeInSentence, nodeSimilarity
     */
    public static Table<DNode, DNode, Double> getNodeSimilarities(DTree questionDTree, DTree sentenceDTree) {
        Table<DNode, DNode, Double> table = HashBasedTable.create();

        return table;
    }

    /** ************************************************************
     * Compute the similarity given two subtrees: questionSubtree and
     * sentenceSubtree, where questionSubtree starts with root, and
     * sentenceSubtree starts at VBs (defined in NodeComparer.VerbSet);
     * @param nodeMatches
     * @return
     */
    public static Table<DNode, DNode, Double> getSubtreeSimilarities(Table<DNode, DNode, Double> nodeMatches) {
        Table<DNode, DNode, Double> result = HashBasedTable.create();
        nodeMatches.cellSet().stream().filter(x -> x.getRowKey().getDepLabel().equals(LangLib.DEP_ROOT)
                                                   && x.getColumnKey().getPOS().startsWith(LangLib.POS_VB))
            .forEach(x -> {
                List<DNode> qnodeSubtree = ShortAnswerExtractionUtils.bfs(x.getRowKey());
                List<DNode> snodeSubtree = ShortAnswerExtractionUtils.bfs(x.getColumnKey());
                double subtreeSimilarity = computeSubtreeSimilarity(nodeMatches, qnodeSubtree, snodeSubtree);
                result.put(x.getRowKey(), x.getColumnKey(), subtreeSimilarity);
            });

        return result;
    }

    public static double computeSubtreeSimilarity(Table<DNode, DNode, Double> nodeMatches, List<DNode> questionNodes, List<DNode> sentenceNodes) {
        double[] sum = {0D};
        questionNodes.stream().filter(q -> q.getId() != 0).forEach(qNode -> {
                                                                       Map.Entry entry = nodeMatches.row(qNode).entrySet().stream()
                                                                           .filter(e -> sentenceNodes.contains(e.getKey()))
                                                                           .max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).orElse(null);
                                                                       Double sim = (Double) entry.getValue();
                                                                       sum[0] += sim;
                                                                   }
        );
        return sum[0] / questionNodes.size()*1.0;
    }

    /**
     * ************************************************************
     * For <R, C, V>, Get the most similar C given R by selecting
     * the highest C;
     *
     * @param table
     * @param row
     * @return
     */
    public static DNode getMostSimilarColumn(Table<DNode, DNode, Double> table, DNode row) {
        if (null == row)
            return null;

        Map.Entry<DNode, Double> entry = table.row(row).entrySet().stream().max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).orElse(null);
        if (null == entry)
            return null;
        return entry.getKey();
    }

    /**
     * ************************************************************
     * For <R, C, V>, Get the most similar R given C by selecting
     * the highest C;
     *
     * @param table
     * @param col
     * @return
     */
    public static DNode getMostSimilarRow(Table<DNode, DNode, Double> table, DNode col) {
        if (null == col)
            return null;

        Map.Entry<DNode, Double> entry = table.column(col).entrySet().stream().max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).orElse(null);
        if (null == entry)
            return null;
        return entry.getKey();
    }


    /**
     * Filter from the map of scored nodes all those nodes whose forms are found in the given DTree.
     * Intended use case: we have a map of scored DNodes (the scoredNodes parameter), and we want to remove from the map all
     * the words which are in the question (the tree parameter).
     * @param scoredNodes
     * @param tree
     * @return
     */
    public static Map<DNode, Double> filterFormsFromDNodeMap(Map<DNode, Double> scoredNodes, DTree tree) {
        List<String> tokensToRemove = tree.stream().map(x -> x.getForm().toLowerCase()).collect(
            Collectors.toList());

        final List<String> allowedWords = Lists.newArrayList("a", "an", "the",
                                                             "is", "was",
                                                             "at", "from", "in", "on", "to", "with",
                                                             "for",
                                                             "and", "but", "or");

        tokensToRemove.removeAll(allowedWords);

        return scoredNodes.entrySet().stream()
            // Filter out the tokens we want to remove.
            .filter(entry -> !tokensToRemove.contains(entry.getKey().getForm().toLowerCase()))

                // Collect the result into a map.
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Get the head of a list of DNodes--the one which subsumes the others. If a single node does not subsume the others,
     * we find the smallest subset of subsuming nodes and return the first of the result.
     * @param list
     * @return
     */
    public static DNode getSubtreeHead(List<DNode> list) {
        List<DNode> children = Lists.newArrayList();

        // Get a list of all the children nodes of the given list.
        list.stream().forEach(node -> {
            children.addAll(node.getChildren());
        });

        // Create a new list of nodes which are not children.
        List<DNode> nonChildren = Lists.newArrayList(list);
        nonChildren.removeAll(children);

        return nonChildren.get(0);
    }

    /**
     * Determines whether a short answer is "too long" to qualify as a true short answer.
     * @return
     */
    public static boolean isShortAnswerTooLong(final String expected, final String actual) {
        // Get ratio of expected length to actual length.
        // Expected length is tricky if a regular expression, as in (hello|hi|what's up|how's it going|how have you been).
        // We'll take the length of the longest possibility--not perfect, but maybe good enough.
        String[] orSplit = expected.trim().split("\\|");
        String longestStr = Arrays.asList(orSplit).stream()
            .max((s1, s2) -> s1.trim().split(StringUtils.SPACE).length - s2.trim().split
                (StringUtils.SPACE).length)
            .orElse(StringUtils.EMPTY);
        double expectedLength = longestStr.trim().split(StringUtils.SPACE).length;

        double actualLength = actual.trim().split(StringUtils.SPACE).length;
        double lengthRatio = actualLength / expectedLength;

        // Allowable length ratio depends on how long the expected length is. If expected is one word in length, we want to allow a two-word answer.
        double allowableLengthRatio = 2.0;
        if (expectedLength >= 3) {
            allowableLengthRatio = 1.4;
        }

        return lengthRatio > allowableLengthRatio;
    }
}
