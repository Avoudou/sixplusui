package pokerAppUi.evGraphStuffs;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class EvGraphTreeViewPanel
        extends HBox {

    private EvGraphGameTreeView parent;
    private int nodeId;

    private Label playerLabel;
    private Label actionLabel;

    public EvGraphTreeViewPanel(EvGraphGameTreeView evGraphGameTreeView, 
                                String action,
                                String player, 
                                int nodeId) {
        this.parent = evGraphGameTreeView;
        this.nodeId = nodeId;

        this.playerLabel = new Label(action);
        this.actionLabel = new Label(player);
        
        this.setSpacing(10);

        this.getChildren().addAll(actionLabel, playerLabel);

        this.setOnMousePressed(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                parent.nodeSelected(nodeId);
            }
        });
    }

    public int getNodeId() {
        return nodeId;
    }

}
