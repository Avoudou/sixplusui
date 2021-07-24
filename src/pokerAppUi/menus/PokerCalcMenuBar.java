package pokerAppUi.menus;

import handeval.HandRanking;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

public class PokerCalcMenuBar extends MenuBar {

    private Menu handRankingsMenu;

	public PokerCalcMenuBar() {
        createMenu();
        // handRankingsMenu = new Menu("File");
        // configure = new MenuItem("Settings");
        // handRankingsMenu.setOnAction(new EventHandler<ActionEvent>() {
        // @Override
        // public void handle(ActionEvent arg0) {
        // }
        // });
        // menuSettings.getItems().add(configure);
	}

    private void createMenu() {
        handRankingsMenu = new Menu("Hand rankings");

        HandRankingCheckMenuItem holdemRanking = new HandRankingCheckMenuItem(handRankingsMenu,
                                                                              HandRanking.HOLDEM,
                                                                              "Holdem Ranking");
        holdemRanking.setSelected(true);
        handRankingsMenu.getItems().add(holdemRanking);

        HandRankingCheckMenuItem flushVsFull = new HandRankingCheckMenuItem(
                                                                            handRankingsMenu,
                                                                            HandRanking.FLUSHgtFull,
                                                                            "Flush > Full House");
        handRankingsMenu.getItems().add(flushVsFull);

        this.getMenus().add(handRankingsMenu);

        // menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
    }

}
