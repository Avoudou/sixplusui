package pokerAppUi;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import pokerAppUi.uiElements.HandElement;
import statics.StaticsMethods;

public class RangeTablePanel extends BorderPane {

	private ArrayList<ArrayList<HandElement>> rangeTableData;
	private GridPane rangeTable;

	public RangeTablePanel() {
		init();
	}

    public RangeTablePanel(ArrayList<ArrayList<HandElement>> rangeTableData) {
        this.rangeTableData = rangeTableData;
        init();
    }

    public ArrayList<ArrayList<HandElement>> getRangeTableData() {
        return rangeTableData;
    }

    private void init() {
        this.setPrefSize(400, 400);
        this.setMaxSize(400, 450);
        this.setPadding(new Insets(20));
		displayRange(rangeTableData);
		this.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
				+ "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: black;");
		HBox titleBox = new HBox();
        Label title = new Label("Range");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 15));
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPadding(new Insets(5));
		titleBox.getChildren().add(title);

		this.setTop(titleBox);
    }

    public void displayRange() {
        displayRange(rangeTableData);
    }

	public void displayRange(ArrayList<ArrayList<HandElement>> inputTable) {
		if (this.getCenter() != null) {
			this.getChildren().remove(this.getCenter());
		}
		rangeTableData = inputTable;

		if (inputTable == null) {
			rangeTableData = createSampleTable();
        }
		rangeTable = new GridPane();
		rangeTable.gridLinesVisibleProperty().set(true);
		for (int i = 0; i < rangeTableData.size(); i++) {
			for (int j = 0; j < rangeTableData.get(0).size(); j++) {
				StackPane rangeStuck = new StackPane();
				rangeStuck.setPrefSize(40, 40);
				Rectangle backRoundFrame = new Rectangle(0, 0, 40, 40);
				Rectangle backRound = new Rectangle(0, 0, 38, 38);
				Rectangle shadeColor = new Rectangle(0, 0, 38, 38);
				backRound.setFill(javafx.scene.paint.Color.WHITE);
				// backRound.setFill(javafx.scene.paint.Color.GREEN);

                rangeStuck.getChildren().add(backRoundFrame);
				rangeStuck.getChildren().add(backRound);
				

				VBox handInfo = new VBox();
				handInfo.setAlignment(Pos.CENTER);
                HandElement handElement = rangeTableData.get(i).get(j);
                Text handName = new Text(handElement.getHandName());
				handName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                Double percent = new Double(handElement.getPercentage());
				Text perc = new Text(StaticsMethods.decimalFormat(percent, 1) + "%");
				perc.setFont(Font.font("Arial", FontWeight.BOLD, 10));

				if (percent < -1) {
					shadeColor.setFill(Color.color(0.8, 0, 0, 0.5));
				} else if (percent < 0) {
					shadeColor.setFill(Color.color(0.8, 0, 0, 0.3));
				} else if (percent > 1) {
					shadeColor.setFill(Color.color(0, 0.8, 0, 0.5));
				} else {
					shadeColor.setFill(Color.color(0, 0.8, 0, 0.3));
				}

                // TODO Display something reasonable
                // Rectangle indicator = new Rectangle(0, 0, 39, 39);
                // if (percent > 0 && !handElement.isPlayed()) {
                // indicator.setFill(Color.YELLOW);
                // shadeColor.setStroke(Color.YELLOW);
                // // rangeStuck.getChildren().add(indicator);
                // } else if (percent < 0 && handElement.isPlayed()) {
                // indicator.setFill(Color.ORANGE);
                // shadeColor.setStroke(Color.ORANGE);
                // // rangeStuck.getChildren().add(indicator);
                // }

				rangeStuck.getChildren().add(shadeColor);
				handInfo.getChildren().add(handName);
				handInfo.getChildren().add(perc);
				rangeStuck.getChildren().add(handInfo);

				rangeTable.add(rangeStuck, i, j);

			}

		}
		this.setCenter(rangeTable);
	}

    public static ArrayList<ArrayList<HandElement>> createSampleTable() {
		ArrayList<String> possibleCards = new ArrayList<String>();
		possibleCards.add("A");
		possibleCards.add("K");
		possibleCards.add("Q");
		possibleCards.add("J");
		possibleCards.add("T");
		possibleCards.add("9");
		possibleCards.add("8");
		possibleCards.add("7");
		possibleCards.add("6");

		ArrayList<ArrayList<HandElement>> sampleTable = new ArrayList<ArrayList<HandElement>>();

		for (int i = 0; i < possibleCards.size(); i++) {
			sampleTable.add(new ArrayList<HandElement>());
		}

		for (int k = 0; k < possibleCards.size(); k++) {
			for (int l = 0; l < possibleCards.size(); l++) {
				double percentage = 100 - 200 * Math.random();
                percentage = 0;
                if (k < l) {
					HandElement element = new HandElement(possibleCards.get(k) + possibleCards.get(l) + "o",
 percentage,
                                                          percentage > 0);
					sampleTable.get(k).add(element);
				} else if (k == l) {
					HandElement element = new HandElement(possibleCards.get(k) + possibleCards.get(l),
 percentage,
                                                          percentage > 0);
					sampleTable.get(k).add(element);

				} else {
					HandElement element = new HandElement(possibleCards.get(l) + possibleCards.get(k) + "s",
 percentage,
                                                          percentage > 0);
					sampleTable.get(k).add(element);
				}

			}
		}

		return sampleTable;

	}
}
