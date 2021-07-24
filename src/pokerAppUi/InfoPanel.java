package pokerAppUi;

import java.util.ArrayList;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import pokerAppUi.evGraphStuffs.EvGraphTab;
import pokerAppUi.uiElements.HandElement;
import ranges.SimpleLinearRange;
import solvers.RecalculateWrapper;
import solvers.Solution;

public class InfoPanel extends BorderPane {

	private HBox infoContainer;
	private HBox buttonMenu;

	private TabPane handTabsPane;
	private RangeTablePanel rangePane;
	private ProgressBar prBar;
	private boolean showPrBar = true;
	private int tabId = 1;

	public InfoPanel() {
		handTabsPane = new TabPane();
        int tabPaneWidth = 650;
        int tabPaneHeight = 500;
        handTabsPane.setMinSize(tabPaneWidth, tabPaneHeight);
        handTabsPane.setPrefSize(tabPaneWidth, tabPaneHeight);
        handTabsPane.setMaxSize(tabPaneWidth, tabPaneHeight);
		rangePane = new RangeTablePanel();
		this.infoContainer = new HBox();
		infoContainer.getChildren().add(handTabsPane);
		infoContainer.getChildren().add(rangePane);
		infoContainer.setAlignment(Pos.TOP_CENTER);
		handTabsPane.setStyle("-fx-padding: 2;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
				+ "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: black;");
		this.setCenter(infoContainer);

		this.buttonMenu = new HBox();
		buttonMenu.setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 0.3;" + "-fx-border-radius: 1;"
				+ "-fx-border-color: grey;");
		buttonMenu.setMaxWidth(540);

		HBox prBox = initPrBox();
		HBox butonBox = new HBox();

        Button runSolutionButton = new Button("Recalculate");
		butonBox.getChildren().add(runSolutionButton);
		butonBox.setAlignment(Pos.CENTER);
		buttonMenu.getChildren().add(butonBox);
		buttonMenu.getChildren().add(prBox);
		buttonMenu.setAlignment(Pos.BOTTOM_LEFT);
		buttonMenu.setPadding(new Insets(10, 10, 5, 15));


        runSolutionButton.setOnMouseClicked(new RecalculateHandler());
		this.setTop(buttonMenu);

	}

    public void displayRange(ArrayList<ArrayList<HandElement>> rangeTableData) {
        rangePane.displayRange(rangeTableData);
    }

	private HBox initPrBox() {
		HBox prBox = new HBox();
		prBar = new ProgressBar();
		prBar.setMaxSize(60, 10);
		prBar.setStyle("-fx-accent: green");
		prBox.getChildren().add(prBar);
		prBox.setAlignment(Pos.CENTER);
		prBox.setPadding(new Insets(10));
		return prBox;
	}

    public void addTab(Solution solution) {
        tabId++;
        PokerHandTab tab = new PokerHandTab(solution, this);
        addToTabsPane(tab);
	}

    public void addTab(EvGraphTab evGraphTab) {
        evGraphTab.setText("EV graph");
        handTabsPane.getTabs().add(evGraphTab);
        handTabsPane.getSelectionModel().select(evGraphTab);
    }

    private void addToTabsPane(PokerHandTab tab) {
        tab.setText("     " + tabId + "    ");
        handTabsPane.getTabs().add(tab);
        handTabsPane.getSelectionModel().select(tab);
    }

    private void recalculatedSolution(PokerHandTab oldTab, 
                                      Solution newSolution,
                                      Map<Integer, SimpleLinearRange> lockedRanges) {
        handTabsPane.getTabs().remove(oldTab);
        PokerHandTab newTab = new PokerHandTab(newSolution, this);
        newTab.setLockedRanges(lockedRanges.keySet());
        newTab.setExpandedNodes(oldTab);
        addToTabsPane(newTab);
    }

    class RecalculateHandler
            implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent arg0) {
            if (showPrBar == true) {
                buttonMenu.getChildren().get(1).setVisible(false);
                showPrBar = false;
            } else {
                buttonMenu.getChildren().get(1).setVisible(true);
                showPrBar = true;
            }
            Tab selectedItem = handTabsPane.getSelectionModel().getSelectedItem();
            if (!(selectedItem instanceof PokerHandTab)) {
                return;
            }
            PokerHandTab selectedTab = (PokerHandTab) selectedItem;
            Solution solution = selectedTab.getSolution();
            Map<Integer, SimpleLinearRange> lockedRanges = selectedTab.getLockedRanges();
            // TODO Unlocking all ranges after a recalculate?
            if (lockedRanges.size() == 0) {
                return;
            }
            Solution newSolution = RecalculateWrapper.recalculateSolution(solution, lockedRanges);
            recalculatedSolution(selectedTab, newSolution, lockedRanges);
        }

    }

}
