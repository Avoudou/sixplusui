package pokerAppUi.uiElements;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import pokerAppUi.evGraphStuffs.SelectHandPanel;

public class HandStackBlock
        extends StackPane {

    protected boolean isSelected;
    protected HandElement handElement;
    protected Rectangle backRoundShade;
    protected Hyperlink selectHandElement;

    private SelectHandPanel selectHandPanel;

    public HandStackBlock(HandElement handElement, Color backroundColor) {
        this.handElement = handElement;
        this.setPrefSize(40, 40);
        Rectangle backRoundFrame = new Rectangle(0, 0, 40, 40);
        backRoundFrame.setFill(javafx.scene.paint.Color.BLACK);
        Rectangle backRound = new Rectangle(0, 0, 38, 38);
        backRoundShade = new Rectangle(0, 0, 38, 38);

        this.getChildren().add(backRoundFrame);
        this.getChildren().add(backRound);

        VBox handInfo = new VBox();
        handInfo.setAlignment(Pos.CENTER);
        Text handName = new Text(handElement.getHandName());
        handName.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        backRound.setFill(backroundColor);

        backRoundShade.setFill(Color.color(0, 1, 0.3, 0.0));
        handInfo.getChildren().add(handName);
        this.getChildren().add(handInfo);
        this.getChildren().add(backRoundShade);

    }

    public HandStackBlock(HandElement handElement2, Color lightgrey, SelectHandPanel selectHandPanel) {
        this(handElement2, lightgrey);
        this.selectHandPanel = selectHandPanel;
        
        this.selectHandElement = new Hyperlink();
        selectHandElement.setPrefSize(38, 38);

        selectHandElement.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if (isSelected == false) {
                    select();
                } else {
                    deselect();
                }
            }
        });

        this.getChildren().add(selectHandElement);
    }
    
    public void select() {
        isSelected = true;
        backRoundShade.setFill(Color.color(0, 1, 0.3, 0.2));
        selectHandPanel.handSelected(handElement);
    }

    public void deselect() {
        isSelected = false;
        backRoundShade.setFill(Color.color(0, 0, 0.0, 0.0));
        selectHandPanel.handDeSelected(handElement);
    }

}