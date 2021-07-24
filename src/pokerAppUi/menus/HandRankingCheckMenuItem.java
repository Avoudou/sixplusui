package pokerAppUi.menus;

import handeval.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

public class HandRankingCheckMenuItem
        extends CheckMenuItem {

    private Menu handRankingsMenu;

    private HandRanking handRanking;

    public HandRankingCheckMenuItem(Menu handRankingsMenu, HandRanking handRanking, String text) {
        super(text);
        this.handRankingsMenu = handRankingsMenu;
        this.handRanking = handRanking;
        this.setOnAction(new HandRankingCheckMenuItemListener());
        this.setSelected(handRanking == HandRanking.HOLDEM);
    }

    public void onSelectionChanged() {
        if (isSelected()) {
            updateHandRanking();
        } else {
            setSelected(true);
        }
        for (MenuItem item : handRankingsMenu.getItems()) {
            if (item != this && item instanceof HandRankingCheckMenuItem) {
                HandRankingCheckMenuItem checkMenuItem = (HandRankingCheckMenuItem) item;
                if (this.isSelected()) {
                    checkMenuItem.setSelected(false);
                }
            }
        }
    }

    private void updateHandRanking() {
        Preflop_2_PlayerLookupTable.setHandRanking(handRanking);
        Preflop_3_PlayerLookupTable.setHandRanking(handRanking);
    }

    private class HandRankingCheckMenuItemListener
            implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            onSelectionChanged();
        }

    }
}
