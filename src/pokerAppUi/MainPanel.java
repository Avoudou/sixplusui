package pokerAppUi;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import license.LicenseObserver;
import license.LicenseStatus;
import pokerAppUi.menus.PokerCalcMenuBar;
import pokerAppUi.newHandStuff.NewHandPanel;

public class MainPanel
        extends BorderPane
        implements LicenseObserver {

	private PokerCalcMenuBar menuNorth;
	private InfoPanel centralInfoPanel;
	private NewHandPanel leftNewHandPanel;

    private Label licenseLabel;

    public MainPanel(LicenseStatus licenseStatus) {
		menuNorth = new PokerCalcMenuBar();
        this.setTop(menuNorth);
        centralInfoPanel = new InfoPanel();
        this.setCenter(centralInfoPanel);

        String licenseString = licenseStatus.isValid() ? "" : "";
        this.licenseLabel = new Label(licenseString);
        leftNewHandPanel = new NewHandPanel(centralInfoPanel, licenseStatus, licenseLabel);
        VBox vBox = new VBox();
        vBox.getChildren().add(leftNewHandPanel);
        vBox.getChildren().add(licenseLabel);
        this.setLeft(vBox);
        licenseStatus.addLicenseObserver(this);
	}

    public void notifyLicenseChanged(LicenseStatus licenseStatus) {
        System.out.println("Updating licence state");
        Platform.runLater(() -> updateLicenseLabel(licenseStatus));
    }

    private void updateLicenseLabel(LicenseStatus licenseStatus) {
        if (licenseStatus.isValid()) {
            licenseLabel.setText("");
        } else {
            if (licenseStatus.isChecked()) {
                licenseLabel.setText(licenseStatus.getReason());
            } else {
                licenseLabel.setText("Error checking the license");
            }
        }
    }

}
