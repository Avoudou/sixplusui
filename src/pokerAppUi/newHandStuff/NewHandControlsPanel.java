package pokerAppUi.newHandStuff;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import license.LicenseStatus;
import pokerAppUi.InfoPanel;
import solvers.LinearSolverWrapper;
import solvers.Solution;
import utilities.Files;
import utils.FileUtils;
//import constants.ClientFilePaths;

public class NewHandControlsPanel extends VBox {
	private InfoPanel infoPanel;
	private Button newHandButton;
    private NewHandInputPanel inputPanel;

    private LicenseStatus licenseStatus;
    private Label licenseLabel;

    public NewHandControlsPanel(InfoPanel infoPanel, 
                                NewHandInputPanel inputPanel,
                                LicenseStatus licenseStatus,
                                Label licenseLabel) {
        this.infoPanel = infoPanel;
        this.inputPanel = inputPanel;
        this.licenseStatus = licenseStatus;
        this.licenseLabel = licenseLabel;

		this.setAlignment(Pos.TOP_CENTER);
		this.setMaxSize(150, 20);
		// setSpacing(10);
		setPadding(new Insets(30));
		this.infoPanel = infoPanel;
        initializeNewHandButton();
		this.setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 0.5;" + "-fx-border-radius: 1;"
				+ "-fx-border-color: grey;");

	}

    private void initializeNewHandButton() {
        newHandButton = new Button("Solve");
		newHandButton.setPrefSize(100, 40);
        newHandButton.setOnAction(new NewHandButtonEventHandler());
		this.getChildren().add(newHandButton);
	}

    private class NewHandButtonEventHandler
            implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
//            if (!confirmLicense()) {
//                return;
//            }
            int[] stacks = inputPanel.getStacks();
            int[] blinds = inputPanel.getBlinds();
            // long start = System.currentTimeMillis();
            Solution solution = LinearSolverWrapper.solve(stacks, blinds);
            // double elapsed = (System.currentTimeMillis() - start) / 1000.0;
            // System.out.println("Elapsed: " + elapsed);
            infoPanel.addTab(solution);
        }

        private boolean confirmLicense() {
            if (licenseStatus.isValid()) {
                licenseLabel.setText("");
                return true;
            }
            if (!licenseStatus.isChecked()) {
                licenseLabel.setText("Please wait, verifying license...");
            } else {
                String reason = licenseStatus.getReason();
                licenseLabel.setText(reason);

                // if (!reason.contains("onnection")) {
                // deleteLicenseFile();
                // }
            }
            return false;
        }

//        private void deleteLicenseFile() {
//            // ConsoleLogger.log("Deleting");
//            if (Files.fileExists(ClientFilePaths.licensePath)) {
//                try {
//                    FileUtils.deleteIfExists(ClientFilePaths.licensePath);
//                    // ConsoleLogger.log("deleted");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

    }

}
