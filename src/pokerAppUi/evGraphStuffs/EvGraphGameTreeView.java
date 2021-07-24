package pokerAppUi.evGraphStuffs;

import game_tree.*;

import java.util.*;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;

public class EvGraphGameTreeView
        extends HBox {

    private GameTree gameTree;
    private TreeNode equityNode;
    private CreateEvGraphPanel parent;

    private TreeView<EvGraphTreeViewPanel> graphView;

    private int selectedNodeId = -1;

    public EvGraphGameTreeView(GameTree gameTree, TreeNode equityNode,
                               CreateEvGraphPanel createEvGraphPanel) {
        this.gameTree = gameTree;
        this.equityNode = equityNode;
        this.parent = createEvGraphPanel;

        graphView = createTreeView(gameTree);
        this.getChildren().add(graphView);
    }

    public TreeNode getSelectedNode() {
        if (selectedNodeId < 0) {
            return null;
        }
        return gameTree.getNode(selectedNodeId);
    }

    public void nodeSelected(int nodeId) {
        this.selectedNodeId = nodeId;
        parent.selectionChanged();
    }

    private TreeView<EvGraphTreeViewPanel> createTreeView(GameTree gameTree) {
        TreeItem<EvGraphTreeViewPanel> rootItem = new TreeItem<EvGraphTreeViewPanel>(
                                                                                     new EvGraphTreeViewPanel(
                                                                                                              null,
                                                                                                              null,
                                                                                                              null,
                                                                                                              0));

        populateTreeView(gameTree, rootItem);
        pruneTreeView(rootItem);
        TreeView<EvGraphTreeViewPanel> treeView = new TreeView<EvGraphTreeViewPanel>(rootItem);
        treeView.setShowRoot(false);
        return treeView;
    }

    private void populateTreeView(GameTree gameTree, TreeItem<EvGraphTreeViewPanel> rootItem) {
        for (TreeNode node : gameTree.getNodes()) {
            if (gameTree.getMostRecentBet(node) == gameTree.getRoot() && node.getNumChildren() == 2) {
                TreeItem<EvGraphTreeViewPanel> item = makeItem(node, "Push");
                rootItem.getChildren().add(item);
                List<TreeNode> subTree = findSubTree(node.getShoveChild(),
                                                     new ArrayList<TreeNode>(
                                                                             Arrays.asList(node.getShoveChild())));
                setVpip2(gameTree, item, subTree, node);
            }
        }
    }

    private void setVpip2(GameTree gameTree, TreeItem<EvGraphTreeViewPanel> parent,
                          List<TreeNode> subList, TreeNode parentNode) {
        for (TreeNode node : subList) {
            boolean matchingCondition = gameTree.getMostRecentVpip(node) == parentNode.getShoveChild();
            if (matchingCondition && node.getNumChildren() == 2) {
                TreeItem<EvGraphTreeViewPanel> item = makeItem(node, "Call");
                parent.getChildren().add(item);

                List<TreeNode> subTree = findSubTree(node.getShoveChild(),
                                                     new ArrayList<TreeNode>(
                                                                             Arrays.asList(node.getShoveChild())));
                setVpip3(gameTree, item, subTree, node);
            }
        }
    }

    private void setVpip3(GameTree gameTree, TreeItem<EvGraphTreeViewPanel> parent,
                          List<TreeNode> subList, TreeNode parentNode) {
        for (TreeNode node : subList) {
            boolean matchingCondition = gameTree.getMostRecentVpip(node) == parentNode.getShoveChild();
            if (matchingCondition && node.getNumChildren() == 2) {
                TreeItem<EvGraphTreeViewPanel> item = makeItem(node, "Call");
                parent.getChildren().add(item);
            }
        }

    }

    private TreeItem<EvGraphTreeViewPanel> makeItem(TreeNode node, String action) {
        Player activePlayer = node.getActivePlayer();
        String playerLabel = PositionsNineHanded.getPosition(activePlayer, node.getGameTree()
                                                                               .getNumPlayers())
                             + "";
        EvGraphTreeViewPanel treeViewPanel = new EvGraphTreeViewPanel(this, action, playerLabel,
                                                                      node.getId());
        TreeItem<EvGraphTreeViewPanel> item = new TreeItem<EvGraphTreeViewPanel>(treeViewPanel);
        item.setExpanded(true);
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

    private void pruneTreeView(TreeItem<EvGraphTreeViewPanel> rootItem) {
        List<TreeItem<EvGraphTreeViewPanel>> toRemove = new ArrayList<>();
        for (TreeItem<EvGraphTreeViewPanel> item : rootItem.getChildren()) {
            Set<Integer> nodeIds = new HashSet<Integer>();
            collectChildrenIds(item, nodeIds);
            boolean shouldRemove = !nodeIds.contains(equityNode.getId());
            if (shouldRemove) {
                toRemove.add(item);
            }
        }
        for (TreeItem<EvGraphTreeViewPanel> item : toRemove) {
            rootItem.getChildren().remove(item);
        }
    }

    private void collectChildrenIds(TreeItem<EvGraphTreeViewPanel> item, Set<Integer> nodeIds) {
        nodeIds.add(item.getValue().getNodeId());
        for (TreeItem<EvGraphTreeViewPanel> child : item.getChildren()) {
            collectChildrenIds(child, nodeIds);
        }
    }
}
