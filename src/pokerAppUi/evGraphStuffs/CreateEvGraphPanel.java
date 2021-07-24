package pokerAppUi.evGraphStuffs;

import game_tree.TreeNode;
import hands.HandUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pokerAppUi.InfoPanel;
import pokerAppUi.uiElements.HandElement;
import solvers.Solution;
import solvers.ev_graph.EvGraphCalculator;
import solvers.ev_graph.EvGraphResults;

public class CreateEvGraphPanel extends BorderPane {

    private Solution solution;
    private TreeNode equityNode;
    private InfoPanel infoPanel;
    private Stage containingStage;

	private HBox mainContainer;
    private EvGraphGameTreeView treeView;
	private SelectHandPanel inputPanel;
	private Button okButton;

    public CreateEvGraphPanel(Solution solution, TreeNode equityNode, InfoPanel infoPanel,
                              Stage containingStage) {
        this.solution = solution;
        this.equityNode = equityNode;
        this.infoPanel = infoPanel;
        this.containingStage = containingStage;

		this.mainContainer = new HBox();
        this.treeView = new EvGraphGameTreeView(solution.getGameTree(), equityNode, this);
        this.inputPanel = new SelectHandPanel(this);

		mainContainer.getChildren().add(treeView);
		mainContainer.getChildren().add(inputPanel);
		mainContainer.setAlignment(Pos.TOP_LEFT);
        // mainContainer.setPrefSize(500, 500);
        // mainContainer.setMaxSize(500, 500);
        mainContainer.setPadding(new Insets(10));

        this.okButton = new Button("OK");
        okButton.setOnAction(new CreateGraphHandler());
        okButton.setDisable(true);
		HBox butnBox = new HBox();
		butnBox.getChildren().add(okButton);

		this.setCenter(mainContainer);
        this.setTop(butnBox);

	}

    public void selectionChanged() {
        HandElement selectedElement = inputPanel.getSelectedElement();
        TreeNode selectedNode = treeView.getSelectedNode();
        boolean isValidSelection = selectedElement != null && selectedNode != null
                                   && selectedNode != equityNode;
        okButton.setDisable(!isValidSelection);
    }

    class CreateGraphHandler
            implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent arg0) {
            HandElement selectedElement = inputPanel.getSelectedElement();
            TreeNode selectedNode = treeView.getSelectedNode();
            if (selectedElement == null || selectedNode == null) {
                return;
            }
            String selectedHand = selectedElement.getHandName();
            int selectedHandId = HandUtils.preflop_stringToInt(selectedHand);

            EvGraphCalculator calculator = new EvGraphCalculator();
            EvGraphResults graphResults = calculator.createGraphData(solution, equityNode,
                                                                     selectedNode, selectedHandId);

            EvGraphTab evGraphTab = new EvGraphTab(graphResults);
            infoPanel.addTab(evGraphTab);
            containingStage.close();
        }

    }

}
