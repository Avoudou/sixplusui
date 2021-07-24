package pokerAppUi.treeViewStuff;

import game_tree.TreeNode;

import java.util.ArrayList;

import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mainFrameLauncher.Launcher;
import pokerAppUi.*;
import pokerAppUi.evGraphStuffs.CreateEvGraphPanel;
import pokerAppUi.uiElements.HandElement;
import statics.StaticsMethods;

import com.sun.javafx.css.StyleManager;

public class PokerHandTreeViewPanel
        extends HBox {

    private TreeNode node;
    private AdjustedRangeData nodeData;

    private PokerHandTab handTab;

    private Stage launchedStage;

    private RangeLockHandler lockHandler;

    private PokerHandTreeViewPanelData buttonsAndLabels = new PokerHandTreeViewPanelData(25, 17);

    public PokerHandTreeViewPanel(AdjustedRangeData nodeData, TreeNode node, String actionLabel,
                                  String playerLabel, String rangeLabel, PokerHandTab handTab) {
        this.node = node;
        this.nodeData = nodeData;
        this.handTab = handTab;
        this.setSpacing(10);
        this.buttonsAndLabels.playerLabel = new Label(playerLabel);
        this.buttonsAndLabels.actionLabel = new Label(actionLabel);
        this.buttonsAndLabels.rangeLabel = new Label(rangeLabel);
        this.buttonsAndLabels.setRangeButton = new Button("Edit");
        this.buttonsAndLabels.showGraphButton = new Button("EV graph");

        this.buttonsAndLabels.toggleButton = new Button("");
        Image image = new Image(PokerHandTreeViewPanel.class.getResourceAsStream("unlocked3.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(buttonsAndLabels.lockWidth);
        imageView.setFitHeight(buttonsAndLabels.lockHeight);
        buttonsAndLabels.toggleButton.setGraphic(imageView);

        this.lockHandler = new RangeLockHandler(false);
        // TODO Manual locking
        // toggleButton.setOnMouseClicked(lockHandler);

        this.getChildren().addAll(this.buttonsAndLabels.actionLabel,
                                  this.buttonsAndLabels.playerLabel,
                                  this.buttonsAndLabels.rangeLabel,
                                  this.buttonsAndLabels.setRangeButton,
                                  buttonsAndLabels.toggleButton, buttonsAndLabels.showGraphButton);

        buttonsAndLabels.setRangeButton.setOnAction(new EditRangeHandler());
        buttonsAndLabels.showGraphButton.setOnAction(new ShowGraphHandler());

        this.setOnMousePressed(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                ArrayList<ArrayList<HandElement>> rangeTableData = nodeData == null ? null
                                                                                   : nodeData.createAdjustedRangeData();
                handTab.displayRange(rangeTableData);
            }
        });

    }

    public void onRangeEdited() {
        launchedStage.close();
        ArrayList<ArrayList<HandElement>> rangeTableData = nodeData.createAdjustedRangeData();
        handTab.displayRange(rangeTableData);

        double rangeFraction = 100.0 * nodeData.getCurrentRange().getRangeFraction();
        String rangeLabel = "range : " + StaticsMethods.decimalFormat(rangeFraction, 1) + "%";
        this.buttonsAndLabels.rangeLabel.setText(rangeLabel);

        if (nodeData.isModified()) {
            lockRange();
        } else {
            unlockRange();
        }
    }

    public void lockRange() {
        lockHandler.isLocked = true;
        lockHandler.setGraphic();
        nodeData.setModified(true);
    }

    public void unlockRange() {
        lockHandler.isLocked = false;
        lockHandler.setGraphic();
    }

    class EditRangeHandler
            implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Stage primaryStage = new Stage();
            launchedStage = primaryStage;
            InputRangePanel root = new InputRangePanel(nodeData, PokerHandTreeViewPanel.this);

            String css = Launcher.class.getResource("skin.css").toExternalForm();
            Scene scene = new Scene(root, 500, 500);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(css);

            StyleManager.getInstance().addUserAgentStylesheet(css);

            primaryStage.setTitle("Edit Range");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            Image image = new Image(Launcher.class.getResourceAsStream("jupiter.png"));
            primaryStage.getIcons().add(image);
            primaryStage.show();
        }
    }

    class RangeLockHandler
            implements EventHandler<MouseEvent> {

        private boolean isLocked;

        public RangeLockHandler(boolean isLocked) {
            this.isLocked = isLocked;
        }

        @Override
        public void handle(MouseEvent arg0) {
            isLocked = !isLocked;
            setGraphic();
        }

        public void setGraphic() {
            String imagePath;
            if (isLocked) {
                imagePath = "locked3.png";
            } else {
                imagePath = "unlocked3.png";
            }
            Image image = new Image(PokerHandTreeViewPanel.class.getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(buttonsAndLabels.lockWidth);
            imageView.setFitHeight(buttonsAndLabels.lockHeight);
            buttonsAndLabels.toggleButton.setGraphic(imageView);
        }

    }

    class ShowGraphHandler
            implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Stage stage = new Stage();
            CreateEvGraphPanel root = new CreateEvGraphPanel(handTab.getSolution(),
                                                             node,
                                                             handTab.getInfoPanel(),
                                                             stage);

            String css = Launcher.class.getResource("skin.css").toExternalForm();
            Scene scene = new Scene(root, 500, 500);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(css);
            StyleManager.getInstance().addUserAgentStylesheet(css);

            stage.setTitle("Select range to adjust");
            stage.setScene(scene);
            stage.setResizable(false);

            Image image = new Image(Launcher.class.getResourceAsStream("jupiter.png"));
            stage.getIcons().add(image);
            stage.show();
        }

    }

    static class PokerHandTreeViewPanelData {
        private Label playerLabel;
        private Label actionLabel;
        private Label rangeLabel;
        private Button setRangeButton;
        private Button toggleButton;
        private Button showGraphButton;
        private int lockWidth;
        private int lockHeight;

        PokerHandTreeViewPanelData(int lockWidth, int lockHeight) {
            this.lockWidth = lockWidth;
            this.lockHeight = lockHeight;
        }
    }

}
