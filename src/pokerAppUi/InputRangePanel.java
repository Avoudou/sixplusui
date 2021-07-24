package pokerAppUi;

import hands.HandUtils;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import pokerAppUi.treeViewStuff.PokerHandTreeViewPanel;
import pokerAppUi.uiElements.*;
import ranges.HandOrdering;
import ranges.SimpleLinearRange;
import utils.StringFormatting;
import conversions.RangeConversions;

public class InputRangePanel extends BorderPane {


	private ArrayList<ArrayList<HandElement>> rangeTableData;
	private GridPane rangeTable;
	// list of selected hands / gets updated
	private ArrayList<HandElement> selectedArrayList;
	private Slider slider;
	private HashMap<String, RangeTableStackBlock> handNameToPanels;
	private SetNewRangeControlsPanel controlsBox;

    private AdjustedRangeData nodeData;
    private PokerHandTreeViewPanel parentPanel;

    public InputRangePanel(AdjustedRangeData nodeData, PokerHandTreeViewPanel parentPanel) {
        this.nodeData = nodeData;
        this.parentPanel = parentPanel;

		this.controlsBox = new SetNewRangeControlsPanel(this);
		this.handNameToPanels = new HashMap<String, RangeTableStackBlock>();
		this.selectedArrayList = new ArrayList<HandElement>();
		this.rangeTable = new GridPane();
		this.setPrefSize(500, 600);
		this.setMinSize(500, 600);
		this.setMaxSize(500, 600);
        this.setPadding(new Insets(20));

        initSlider();
		displayRangeTable();
		// rangeTableData = createHandsTable();
		this.setRight(controlsBox);
	}
    
    

    public AdjustedRangeData getNodeData() {
        return nodeData;
    }

    public Slider getSlider() {
		return slider;
	}

	public void displayRangeTable() {
        rangeTableData = nodeData.createAdjustedRangeData();

		for (int i = 0; i < rangeTableData.size(); i++) {
			for (int j = 0; j < rangeTableData.get(0).size(); j++) {
				RangeTableStackBlock rangeStack = null;
				if (i == j) {
					rangeStack = new RangeTableStackBlock(rangeTableData.get(i).get(j),
							javafx.scene.paint.Color.LIGHTGREY, this);
					handNameToPanels.put(rangeTableData.get(i).get(j).getHandName(), rangeStack);

				} else {
					rangeStack = new RangeTableStackBlock(rangeTableData.get(i).get(j), Color.color(1, 1, 1, 1), this);
					handNameToPanels.put(rangeTableData.get(i).get(j).getHandName(), rangeStack);
				}
				rangeTable.add(rangeStack, i, j);
			}
		}
        if (nodeData != null) {
            slider.setValue(100 * nodeData.getCurrentRange().getRangeFraction());
            // ArrayList<HandElement> displayList = takeListPart(slider.getValue());
            ArrayList<HandElement> displayList = RangeConversions.getSelectedHands(nodeData.getCurrentRange());
            setSelectedHands(displayList);
            rangeAltered();
		}
		this.setCenter(rangeTable);
        // updateSlider();
	}

	public ArrayList<HandElement> getSelectedHands() {
		ArrayList<HandElement> selectedList = new ArrayList<HandElement>(selectedArrayList);
		return selectedList;
	}

	public void setSelectedHands(ArrayList<HandElement> elements) {
		diselectAll();
		selectedArrayList = new ArrayList<HandElement>();

		for (int i = 0; i < elements.size(); i++) {
			handNameToPanels.get(elements.get(i).getHandName()).select();
		}
		// slider.setValue(selecArrayList.size() / 81);
	}

	public void diselectAll() {
		for (int i = 0; i < rangeTable.getChildren().size(); i++) {
			if (((RangeTableStackBlock) rangeTable.getChildren().get(i)).isSelected() == true) {
				((RangeTableStackBlock) rangeTable.getChildren().get(i)).deselect();
                rangeAltered();
			}
		}
	}

    public void handSelected(HandElement handElement) {
        selectedArrayList.add(handElement);
        rangeAltered();
        // RangeConversions.convertToRange(selectedArrayList, nodeData);
    }

    public void handDeSelected(HandElement handElement) {
        selectedArrayList.remove(handElement);
        rangeAltered();
    }

	public ArrayList<HandElement> getSelecArrayList() {
		return selectedArrayList;
	}

	public void setSlider(Slider slider) {
		this.slider = slider;
	}

    public void rangeAltered() {
        SimpleLinearRange selectedRange = RangeConversions.convertToRange(selectedArrayList,
                                                                          nodeData);
        updateSliderValue(selectedRange);
    }

    public void cancelRangeEdit() {
        // nodeData.setAdjustedRange(nodeData.getOriginalRange());
        nodeData.resetRange();
        parentPanel.onRangeEdited();
    }

    public void applyRangeEdit() {
        SimpleLinearRange selectedRange = RangeConversions.convertToRange(selectedArrayList,
                                                                          nodeData);
        nodeData.setAdjustedRange(selectedRange);
        parentPanel.onRangeEdited();
    }

    public void updateSliderValue(SimpleLinearRange selectedRange) {
        // System.out.println("Update " + slider.getValue());
        int newSliderValueSimple = (int) (100.0 * selectedArrayList.size() / 81);
        double newSliderValue = (100.0 * selectedRange.getRangeFraction());
        slider.setValue(newSliderValue);
        // System.out.println("RangeFraction " + newSliderValue + " simple: " +
        // newSliderValueSimple);
		controlsBox.getPercentageDisplay().setText(StringFormatting.decimalFormat(slider.getValue(), 1));

	}

    private ArrayList<HandElement> takeListPart(double percentage) {
		ArrayList<HandElement> returnList = new ArrayList<HandElement>();
        // SimpleLinearRange range = nodeData.getOriginalRange();
        SimpleLinearRange range = nodeData.getCurrentRange();
        HandOrdering hardOrdering = range.getHandOrdering();
        int lastHand = hardOrdering.getXpercentageHand(percentage / 100.0);
        // System.out.println(percentage + " " + lastHand);

        for (int i = 0; i <= lastHand; i++) {
            int hand = hardOrdering.getHands()[i];
            String handString = HandUtils.preflop_intToString(hand);
            returnList.add(new HandElement(handString));
        }
		return returnList;
	}

	private void initSlider() {
		this.slider = new Slider();
		slider.setMin(0);
		slider.setMax(100);
		// slider.setValue(selecArrayList.size() * 81 / 100);

		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		// slider.setMajorTickUnit(10);
		// slider.setMinorTickCount(5);
		slider.setBlockIncrement(1);
		// slider.set
		slider.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
                ArrayList<HandElement> displayList = takeListPart(slider.getValue());
				setSelectedHands(displayList);
                rangeAltered();
			}
		});
		slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
                ArrayList<HandElement> displayList = takeListPart(slider.getValue());
				setSelectedHands(displayList);
                rangeAltered();
			}
		});
		this.setTop(slider);
	}

}
