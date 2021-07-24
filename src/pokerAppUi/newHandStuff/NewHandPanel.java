package pokerAppUi.newHandStuff;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import license.LicenseStatus;
import pokerAppUi.InfoPanel;

public class NewHandPanel extends BorderPane {

	private HBox panelTitle;
	private NewHandInputPanel inputPanel;
	private NewHandControlsPanel controlsPanel;

	@SuppressWarnings("static-access")
    public NewHandPanel(InfoPanel centralInfoPanel, LicenseStatus licenseStatus, Label licenseLabel) {
		initialiseTitle();
		this.inputPanel = new NewHandInputPanel();
        this.controlsPanel = new NewHandControlsPanel(centralInfoPanel, inputPanel, licenseStatus,
                                                      licenseLabel);
		this.setTop(panelTitle);
		this.setCenter(inputPanel);
		this.setBottom(controlsPanel);
		this.setPadding(new Insets(10));
		this.setMaxHeight(450);
		this.setAlignment(controlsPanel, Pos.CENTER);
		this.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 1;"
				+ "-fx-border-insets: 5;" + "-fx-border-radius: 1;" + "-fx-border-color: gray;");


	}

	private void initialiseTitle() {
		this.panelTitle = new HBox();
        Label titleLabel = new Label("New Hand");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		panelTitle.getChildren().addAll(titleLabel);
		panelTitle.setAlignment(Pos.CENTER);
		panelTitle.setPadding(new Insets(10));

	}


	}
