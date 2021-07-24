package pokerAppUi.newHandStuff;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class NewHandInputPanel extends HBox {

	private GridPane newHandStacksMatrix;
	private ArrayList<TextField> stackInputFields;
	private GridPane newHandBlindsMatrix;
	private ArrayList<TextField> blindInputFields;

	public NewHandInputPanel() {
		this.newHandBlindsMatrix = new GridPane();
		this.newHandStacksMatrix = new GridPane();
		stackInputFields = new ArrayList<TextField>();
		blindInputFields = new ArrayList<TextField>();
		setStacksMatrix();
		setBlindsFields();
		// newHandBlindsMatrix.add(new Button("clearAll"), 0, 3);
		this.getChildren().addAll(newHandStacksMatrix, newHandBlindsMatrix);

        blindInputFields.get(0).setText("1");
        blindInputFields.get(1).setText("2");
        stackInputFields.get(0).setText("20");
        stackInputFields.get(1).setText("20");
        stackInputFields.get(2).setText("20");
	}

    public int[] getStacks() {
        ArrayList<Integer> stacks = new ArrayList<Integer>();
        for (TextField stackField : stackInputFields) {
            String text = stackField.getText();
            if (text == null || text.length() == 0) {
                // TODO should we check other fields?
                continue;
            }
            try {
                int stack = Integer.parseInt(text);
                stacks.add(stack);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        if (stacks.size() < 2) {
            // TODO No point in solving
        }

        int[] intStacks = stacks.stream().mapToInt(i -> i).toArray();
        return intStacks;
    }
    
    public int[] getBlinds() {
        ArrayList<Integer> blinds = new ArrayList<Integer>();
        for (TextField blindField : blindInputFields) {
            String text = blindField.getText();
            if (text == null || text.length() == 0) {
                blinds.add(0);
                continue;
            }
            try {
                int blind = Integer.parseInt(text);
                blinds.add(blind);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (blinds.size() < 2) {
            // TODO That's an error?
        }
        int[] intBlinds = blinds.stream().mapToInt(i -> i).toArray();
        return intBlinds;
    }
    

	private void setStacksMatrix() {
		newHandStacksMatrix.setPadding(new Insets(10));
		newHandStacksMatrix.setHgap(3);
		newHandStacksMatrix.setVgap(10);
		newHandStacksMatrix.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 1;"
				+ "-fx-border-insets: 5;" + "-fx-border-radius: 1;" + "-fx-border-color: gray;");
		newHandStacksMatrix.setMaxHeight(300);
		for (int i = 0; i < 9; i++) {

		StackPane clickableFieldPanel = new StackPane();

			Label player = new Label("Player" + (i + 1));

		TextField inputField = new TextField();
		inputField.setPrefSize(60, 20);
			inputField.setEditable(true);
		Hyperlink inputRequest = new Hyperlink("");
		inputRequest.setPrefSize(60, 20);

		clickableFieldPanel.getChildren().addAll(inputField, inputRequest);

			newHandStacksMatrix.add(player, 0, i);
			newHandStacksMatrix.add(clickableFieldPanel, 1, i);

		inputRequest.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				inputField.requestFocus();

				}
			});
			stackInputFields.add(inputField);
		}
	}

	private void setBlindsFields() {
		newHandBlindsMatrix.setPadding(new Insets(10));
		newHandBlindsMatrix.setHgap(3);
		newHandBlindsMatrix.setVgap(10);
		newHandBlindsMatrix.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 1;"
				+ "-fx-border-insets: 5;" + "-fx-border-radius: 1;" + "-fx-border-color: gray;");
		newHandBlindsMatrix.setMaxHeight(100);
		for (int i = 0; i < 3; i++) {

			StackPane clickableFieldPanel = new StackPane();
			HBox textBox = new HBox();
			textBox.setAlignment(Pos.CENTER);
			Label blindName = new Label("blind" + (i + 1));
			if (i == 0) {
                blindName = new Label("SB");
			}
			if (i == 1) {
                blindName = new Label("BB");
			}
			if (i == 2) {
				blindName = new Label("ANTE");
			}
			textBox.getChildren().add(blindName);


			TextField inputField = new TextField();
			inputField.setPrefSize(60, 20);

			Hyperlink inputRequest = new Hyperlink("");
			inputRequest.setPrefSize(60, 20);

			clickableFieldPanel.getChildren().addAll(inputField, inputRequest);

			newHandBlindsMatrix.add(textBox, 0, i);
			// newHandBlindsMatrix.setAlignment(Pos.CENTER);
			newHandBlindsMatrix.add(clickableFieldPanel, 1, i);
			// newHandBlindsMatrix.setGridLinesVisible(true);

			inputRequest.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent mouseEvent) {
					inputField.requestFocus();

				}
			});

			blindInputFields.add(inputField);
		}
	}
}
