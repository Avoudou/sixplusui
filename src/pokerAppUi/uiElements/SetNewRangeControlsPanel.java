package pokerAppUi.uiElements;


import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import pokerAppUi.InputRangePanel;

public class SetNewRangeControlsPanel extends VBox {
	private Button cancelButton;
	private Button runNewSolutionButton;
	private TextField percentageDisplay;
	private InputRangePanel parent;

	public SetNewRangeControlsPanel(InputRangePanel parentPanel) {
		this.parent = parentPanel;
        this.cancelButton = new Button("Reset");
        this.runNewSolutionButton = new Button("Apply");
        // this.runNewSolutionButton.setDisable(true);
        cancelButton.setPrefSize(80, 20);
        runNewSolutionButton.setPrefSize(80, 20);
		initTextDisplay();
		this.getChildren().addAll(runNewSolutionButton, cancelButton);
		this.setSpacing(30);
		this.setAlignment(Pos.TOP_CENTER);
		this.setPadding(new Insets(10, 20, 60, 20));

        cancelButton.setOnMouseClicked(new CancelRangeEditHandler());

		runNewSolutionButton.setOnMouseClicked(new ApplyRangeEditHandler());

	}

	public TextField getPercentageDisplay() {
		return percentageDisplay;
	}

	private void initTextDisplay() {
		Label title = new Label("Range%");
		VBox box = new VBox();
		box.setAlignment(Pos.TOP_CENTER);
		box.setPrefHeight(300);
		this.percentageDisplay = new TextField();
		this.percentageDisplay.setPrefSize(60, 20);
		this.getChildren().add(title);
		box.getChildren().addAll(title, percentageDisplay);
		this.getChildren().add(box);
		percentageDisplay.setEditable(false);
		// percentageDisplay.textProperty().addListener((observable, oldValue, newValue) -> {
		// System.out.println("textfield changed from " + oldValue + " to " + newValue);
		// });
    }

    class ApplyRangeEditHandler
            implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            parent.applyRangeEdit();
        }

	}

    class CancelRangeEditHandler
            implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            // parent.diselectAll();
            parent.cancelRangeEdit();
        }

    }

}
