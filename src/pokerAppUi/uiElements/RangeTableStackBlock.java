package pokerAppUi.uiElements;

import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import pokerAppUi.InputRangePanel;

public class RangeTableStackBlock extends HandStackBlock {

	private InputRangePanel inputRangePanel;

	public RangeTableStackBlock(HandElement handElement, Color backroundColor, InputRangePanel inputRangePanel) {
	    super(handElement, backroundColor);
		this.inputRangePanel = inputRangePanel;

        this.selectHandElement = new Hyperlink();
        selectHandElement.setPrefSize(38, 38);

        selectHandElement.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if (isSelected == false) {
                    inputRangePanel.rangeAltered();
                    select();
                } else {
                    deselect();
                    inputRangePanel.rangeAltered();
                }
            }
        });

		this.getChildren().add(selectHandElement);

	}

	public boolean isSelected() {
		return isSelected;
	}

	public HandElement getHandElement() {
		return handElement;
	}

	public void select() {
		isSelected = true;
		backRoundShade.setFill(Color.color(0, 1, 0.3, 0.2));
        inputRangePanel.handSelected(handElement);
	}

	public void deselect() {
		isSelected = false;
		backRoundShade.setFill(Color.color(0, 0, 0.0, 0.0));
		inputRangePanel.getSelecArrayList().remove(RangeTableStackBlock.this.handElement);
        inputRangePanel.handDeSelected(handElement);
	}

}
