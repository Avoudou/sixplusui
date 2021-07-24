package mainFrameLauncher;

import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import license.LicenseHandlerThread;
import license.LicenseStatus;
import pokerAppUi.MainPanel;

public class Launcher
        extends Application {
    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Poker Assistant");

        LicenseStatus licenseStatus = new LicenseStatus();

        MainPanel root = new MainPanel(licenseStatus);
        Scene scene = new Scene(root, 1366, 600);


        // File cssFile = new File("skins/skin.css");
        // System.out.println(cssFile.);

        scene.getStylesheets().clear();
        // scene.getStylesheets().add("skins/skin.css");
        // scene.getStylesheets().add("file:/skins/skin.css");

        // System.out.println(css);
        // scene.getStylesheets().add(css);

        // StyleManager.getInstance().addUserAgentStylesheet(scene, scene.getStylesheets().get(0));

        URL resource = Launcher.class.getResource("skin.css");
        scene.getStylesheets().add(resource.toExternalForm());

        Image image = new Image(Launcher.class.getResourceAsStream("jupiter.png"));
        primaryStage.getIcons().add(image);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(e -> Platform.exit());

        primaryStage.show();

        // checkLicense(licenseStatus, root);

    }

//    private void checkLicense(LicenseStatus licenseStatus, MainPanel root) {
//        LicenseHandlerThread licenseHandler = new LicenseHandlerThread(licenseStatus);
//        Thread licenseThread = new Thread(licenseHandler);
//        licenseThread.start();
//        try {
//            licenseThread.join();
//            // root.setLicenseState(licenseStatus);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            if (!licenseStatus.isChecked()) {
//                licenseStatus.setInvalidLicense("An error occurred");
//                // root.setLicenseState(licenseStatus);
//            }
//        }
//    }

}
