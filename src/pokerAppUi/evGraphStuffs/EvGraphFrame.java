package pokerAppUi.evGraphStuffs;

import javax.swing.JFrame;

import solvers.ev_graph.EvGraphResults;

@SuppressWarnings("serial")
public class EvGraphFrame
        extends JFrame {

    private EvGraphContainerPanel container;

    public EvGraphFrame(String title) {
        container = new EvGraphContainerPanel();
        add(container);
        setTitle(title);
    }

    public void updateGraph(EvGraphResults graphResults) {
        container.updateGraph(graphResults);
    }
}
