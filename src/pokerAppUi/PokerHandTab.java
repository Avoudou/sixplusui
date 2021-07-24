package pokerAppUi;

import game_tree.*;

import java.util.*;

import javafx.scene.control.*;
import pokerAppUi.treeViewStuff.PokerHandTreeViewPanel;
import pokerAppUi.uiElements.HandElement;
import ranges.SimpleLinearRange;
import solve_results.ISolveResults;
import solve_results.ShoveFoldNodeResults;
import solvers.Solution;
import solvers.strategy.LinearStrategies;
import statics.StaticsMethods;

public class PokerHandTab extends Tab {

	private TreeView<PokerHandTreeViewPanel> handView;
	private InfoPanel infoPanel;

    private Solution solution;
    private Map<Integer, AdjustedRangeData> nodeIdsToData;

    private Map<Integer, TreeItem<PokerHandTreeViewPanel>> nodeIdsToTreePanels;

    public PokerHandTab(Solution solution, InfoPanel infoPanel) {
        this.solution = solution;
        this.infoPanel = infoPanel;
        this.nodeIdsToData = new HashMap<>();
        this.nodeIdsToTreePanels = new HashMap<>();

        this.handView = createTreeView(solution);
        this.setContent(handView);
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    public Solution getSolution() {
        return solution;
    }

    public Map<Integer, AdjustedRangeData> getNodeIdsToData() {
        return nodeIdsToData;
    }

    public void displayRange(ArrayList<ArrayList<HandElement>> rangeTableData) {
        infoPanel.displayRange(rangeTableData);
    }

    /**
     * Returns a map from node ids to the range. Only has keys corresponding to the ranges that are
     * actually locked
     */
    public Map<Integer, SimpleLinearRange> getLockedRanges() {
        Map<Integer, SimpleLinearRange> lockedRanges = new HashMap<>();
        for (int nodeId : nodeIdsToData.keySet()) {
            AdjustedRangeData nodeData = nodeIdsToData.get(nodeId);
            // TODO Modified and unlocked?
            if (nodeData.isModified()) {
                lockedRanges.put(nodeId, nodeData.getCurrentRange());
            }
        }
        return lockedRanges;
    }

    public void setLockedRanges(Set<Integer> lockedNodeIds) {
        for (int nodeId : lockedNodeIds) {
            nodeIdsToTreePanels.get(nodeId).getValue().lockRange();
        }
    }

    /**
     * Sets the same expanded nodes as the provided tab (It is assumed that the tree structure is
     * the same)
     */
    public void setExpandedNodes(PokerHandTab other) {
        for (int nodeId : other.nodeIdsToTreePanels.keySet()) {
            TreeItem<PokerHandTreeViewPanel> otherItem = other.nodeIdsToTreePanels.get(nodeId);
            TreeItem<PokerHandTreeViewPanel> item = this.nodeIdsToTreePanels.get(nodeId);
            item.setExpanded(otherItem.isExpanded());
        }
    }

    private TreeView<PokerHandTreeViewPanel> createTreeView(Solution solution) {
        TreeItem<PokerHandTreeViewPanel> rootItem = new TreeItem<PokerHandTreeViewPanel>(
                                                                                         new PokerHandTreeViewPanel(
                                                                                                                    null,
                                                                                                                    null,
                                                                                                                    null,
                                                                                                                    null,
                                                                                                                    null,
                                                                                                                    null));

        populateTreeView(solution, rootItem);
        TreeView<PokerHandTreeViewPanel> treeView = new TreeView<PokerHandTreeViewPanel>(rootItem);
        treeView.setShowRoot(false);
        return treeView;
    }

    private void populateTreeView(Solution solution, TreeItem<PokerHandTreeViewPanel> rootItem) {
        ISolveResults solveResults = solution.getSolveResults();
        GameTree gameTree = solveResults.getGameTree();
        LinearStrategies strategies = solution.getStrategies();
        for (TreeNode node : gameTree.getNodes()) {
            if (gameTree.getMostRecentBet(node) == gameTree.getRoot() && node.getNumChildren() == 2) {
                TreeItem<PokerHandTreeViewPanel> item = makeItem(node,
                                                                 solveResults.getResults(node),
                                                                 strategies,
                                                                 "Push");
                rootItem.getChildren().add(item);
                List<TreeNode> subTree = findSubTree(node.getShoveChild(),
                                                     new ArrayList<TreeNode>(Arrays.asList(node.getShoveChild())));
                setVpip2(solution, item, subTree, node);
            }
        }
    }

    private void setVpip2(Solution solution,
                          TreeItem<PokerHandTreeViewPanel> parent,
                          List<TreeNode> subList, TreeNode parentNode) {
        ISolveResults solveResults = solution.getSolveResults();
        GameTree gameTree = solveResults.getGameTree();
        LinearStrategies strategies = solution.getStrategies();
        for (TreeNode node : subList) {
            boolean matchingCondition = gameTree.getMostRecentVpip(node) == parentNode.getShoveChild();
            if (matchingCondition && node.getNumChildren() == 2) {
                TreeItem<PokerHandTreeViewPanel> item = makeItem(node,
                                                                 solveResults.getResults(node),
                                                                 strategies, "Call");
                parent.getChildren().add(item);

                List<TreeNode> subTree = findSubTree(node.getShoveChild(),
                                                     new ArrayList<TreeNode>(
                                                                             Arrays.asList(node.getShoveChild())));
                setVpip3(solution, item, subTree, node);
            }
        }
    }

    private void setVpip3(Solution solution, TreeItem<PokerHandTreeViewPanel> parent,
                          List<TreeNode> subList,
                          TreeNode parentNode) {
        ISolveResults solveResults = solution.getSolveResults();
        GameTree gameTree = solveResults.getGameTree();
        LinearStrategies strategies = solution.getStrategies();
        for (TreeNode node : subList) {
            boolean matchingCondition = gameTree.getMostRecentVpip(node) == parentNode.getShoveChild();
            if (matchingCondition && node.getNumChildren() == 2) {
                TreeItem<PokerHandTreeViewPanel> item = makeItem(node,
                                                                 solveResults.getResults(node),
                                                                 strategies, "Call");
                parent.getChildren().add(item);
            }
        }

    }

    public TreeItem<PokerHandTreeViewPanel> makeItem(TreeNode node,
                                                     ShoveFoldNodeResults results,
                                                     LinearStrategies strategies, 
                                                     String action) {
        Player activePlayer = node.getActivePlayer();
        String playerLabel = PositionsNineHanded.getPosition(activePlayer, node.getGameTree().getNumPlayers()) + "";
        SimpleLinearRange range = strategies.getStrategy(activePlayer)
                                            .getRange(node.getShoveChild());
        double rangeFraction = 100.0 * range.getRangeFraction();
        String rangeLabel = "range : " + StaticsMethods.decimalFormat(rangeFraction, 1) + "%";
        AdjustedRangeData uiNodeData = new AdjustedRangeData(results, range);
        PokerHandTreeViewPanel treeViewPanel = new PokerHandTreeViewPanel(uiNodeData,
                                                                          node, 
                                                                          playerLabel,
                                                                          action,
                                                                          rangeLabel,
                                                                          this);
        TreeItem<PokerHandTreeViewPanel> item = new TreeItem<PokerHandTreeViewPanel>(treeViewPanel);
        nodeIdsToData.put(node.getId(), uiNodeData);
        nodeIdsToTreePanels.put(node.getId(), item);
        return item;
    }

    private List<TreeNode> findSubTree(TreeNode node, List<TreeNode> subTree) {
        if (node.isLeaf()) {
            return subTree;
        }
        // subTree.add(node);
        for (TreeNode child : node.getChildrenIterator()) {
            if (!child.isLeaf()) {
                subTree.add(child);
                findSubTree(child, subTree);
            }
        }
        return subTree;
    }

}
