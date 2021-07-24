package pokerAppUi.evGraphStuffs;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import pokerAppUi.uiElements.HandElement;
import pokerAppUi.uiElements.HandStackBlock;
import ranges.PredefinedRanges;
import conversions.RangeConversions;

public class SelectHandPanel extends HBox {

    private CreateEvGraphPanel parent;
    private GridPane rangeTable;
    private HashMap<String, HandStackBlock> handNameToPanels;

    private HandElement selectedElement;

    public SelectHandPanel(CreateEvGraphPanel createEvGraphPanel) {
        this.parent = createEvGraphPanel;
        this.handNameToPanels = new HashMap<String, HandStackBlock>();
        this.rangeTable = new GridPane();
        displayRangeTable();
	}

    public void displayRangeTable() {
        ArrayList<ArrayList<HandElement>> rangeTableData = RangeConversions.createSampleRangeData(PredefinedRanges.getLinearAnyTwoRange());
        for (int i = 0; i < rangeTableData.size(); i++) {
            for (int j = 0; j < rangeTableData.get(0).size(); j++) {
                HandStackBlock rangeStack = null;
                if (i == j) {
                    rangeStack = new HandStackBlock(rangeTableData.get(i).get(j), Color.LIGHTGREY, this);
                    handNameToPanels.put(rangeTableData.get(i).get(j).getHandName(), rangeStack);
                } else {
                    rangeStack = new HandStackBlock(rangeTableData.get(i).get(j), Color.color(1, 1, 1, 1), this);
                    handNameToPanels.put(rangeTableData.get(i).get(j).getHandName(), rangeStack);
                }
                rangeTable.add(rangeStack, i, j);
            }
        }
        this.getChildren().add(rangeTable);
    }

    public HandElement getSelectedElement() {
        return selectedElement;
    }

    public void handSelected(HandElement handElement) {
        if (selectedElement != null) {
            handNameToPanels.get(selectedElement.getHandName()).deselect();
        }
        this.selectedElement = handElement;
        parent.selectionChanged();
    }

    public void handDeSelected(HandElement handElement) {
        this.selectedElement = null;
        parent.selectionChanged();
    }

}