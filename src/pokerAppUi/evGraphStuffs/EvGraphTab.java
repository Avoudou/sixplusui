package pokerAppUi.evGraphStuffs;

import javafx.embed.swing.SwingNode;
import javafx.scene.control.Tab;
import solvers.ev_graph.EvGraphResults;

public class EvGraphTab
        extends Tab {

    private SwingNode evGraphNode;

    public EvGraphTab(EvGraphResults graphResults) {
        EvGraphContainerPanel panel = new EvGraphContainerPanel();
        panel.updateGraph(graphResults);
        evGraphNode = new SwingNode();
        // panel.setMaximumSize(new Dimension(400, 300));
        evGraphNode.setContent(panel);
        this.setContent(evGraphNode);
    }
}
