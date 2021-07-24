package license;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import utilities.Files;
import utils.FileUtils;


public class LicenseHandlerThread
        implements Runnable {

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }

//    private LicenseStatus licenseStatus;
//    private BasicEncryption encryptor;
//
//    public LicenseHandlerThread(LicenseStatus licenseStatus) {
//        this.licenseStatus = licenseStatus;
//        try {
//            this.encryptor = new BasicEncryption();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void run() {
//        if (!Files.fileExists(ClientFilePaths.licensePath)) {
//            licenseStatus.setInvalidLicense("License file not found");
//            Platform.runLater(() -> displayDialog());
//        } else {
//            verifyLicense();
//        }
//    }
//
//    private void licenseKeyEntered(String licenseKey) {
//        // ConsoleLogger.log(licenseKey);
//        checkServerAddressFile();
//
//        try {
//            byte[][] addressAndPortBytes = (byte[][]) FileUtils.readFromFile(ClientFilePaths.serverAddressPath);
//            String addressString = encryptor.decrypt(addressAndPortBytes[0]);
//            String portString = encryptor.decrypt(addressAndPortBytes[1]);
//            int port = Integer.parseInt(portString);
//            ServerAddress address = new ServerAddress(addressString, port);
//            InetAddress host = InetAddress.getByName(address.getHost());
//            LicenseClient client = LicenseClient.requestNewLicense(host, address.getPort(),
//                                                                   licenseKey);
//            Thread sendLicense = new Thread(client);
//            sendLicense.start();
//            sendLicense.join(20 * 1000);
//
//            ServerResponse response = client.getResponse();
//            if (response == null) {
//                licenseStatus.setInvalidLicense("Connection error");
//            }
//            if (response.isRequestAccepted()) {
//                boolean success = exportLicense(licenseKey);
//                if (success) {
//                    verifyLicense();
//                }
//            } else {
//                licenseStatus.setInvalidLicense(response.getReason());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            licenseStatus.setInvalidLicense("Connection error");
//        }
//        // FileUtils.createFile(ClientFilePaths.licensePath);
//        // FileUtils.writeToFile(ClientFilePaths.licensePath, licenseKey);
//        // licenseStatus.setInvalidLicense("Please restart to update the license");
//    }
//
//    private boolean exportLicense(String licenseKey) {
//        try {
//            FileUtils.createFile(ClientFilePaths.licensePath);
//            PrintWriter out = new PrintWriter(ClientFilePaths.licensePath);
//            out.println(licenseKey);
//            out.close();
//            // licenseStatus.setInvalidLicense("Please restart to update the license");
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//    private void verifyLicense() {
//        checkServerAddressFile();
//        try {
//            byte[][] addressAndPortBytes = (byte[][]) FileUtils.readFromFile(ClientFilePaths.serverAddressPath);
//            String addressString = encryptor.decrypt(addressAndPortBytes[0]);
//            String portString = encryptor.decrypt(addressAndPortBytes[1]);
//            int port = Integer.parseInt(portString);
//            ServerAddress address = new ServerAddress(addressString, port);
//            InetAddress host = InetAddress.getByName(address.getHost());
//            LicenseClient client = LicenseClient.verifyLicense(host, address.getPort());
//            Thread sendLicense = new Thread(client);
//            sendLicense.start();
//            sendLicense.join(20 * 1000);
//
//            ServerResponse response = client.getResponse();
//            if (response == null) {
//                licenseStatus.setInvalidLicense("Connection error");
//            }
//            if (response.isRequestAccepted()) {
//                licenseStatus.confirmValidLicense();
//            } else {
//                licenseStatus.setInvalidLicense(response.getReason());
//            }
//        } catch (SocketException e) {
//            licenseStatus.setInvalidLicense("Connection error");
//            e.printStackTrace();
//        } catch (Exception e) {
//            licenseStatus.setInvalidLicense("An unexpected error occurred");
//            e.printStackTrace();
//        }
//    }
//
//    private void checkServerAddressFile() {
//        if (!Files.fileExists(ClientFilePaths.serverAddressPath)) {
//            byte[][] addressAndPortBytes = new byte[2][];
//            // System.out.println("IP: " + ServerConstants.IP + " port: " + ServerConstants.PORT);
//            addressAndPortBytes[0] = encryptor.encrypt(ServerConstants.IP);
//            addressAndPortBytes[1] = encryptor.encrypt("" + ServerConstants.PORT);
//            try {
//                FileUtils.writeToFile(ClientFilePaths.serverAddressPath, addressAndPortBytes);
//            } catch (IOException e) {
//                e.printStackTrace();
//                licenseStatus.setInvalidLicense("Error, could not write to file");
//
//            }
//        }
//    }
//
//    private void displayDialog() {
//
//        // Custom dialog
//        Dialog<String> dialog = new Dialog<>();
//        String titleTxt = "License";
//        dialog.setTitle(titleTxt);
//        dialog.setHeaderText("No license found, please enter a license key.");
//        dialog.setResizable(true);
//
//        // Widgets
//        Label label1 = new Label("LicenseKey: ");
//        // Label label2 = new Label("Phone: ");
//        TextField text1 = new TextField();
//        // TextField text2 = new TextField();
//
//        // Create layout and add to dialog
//        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(20, 35, 20, 35));
//        grid.add(label1, 1, 1); // col=1, row=1
//        grid.add(text1, 2, 1);
//        // grid.add(label2, 1, 2); // col=1, row=2
//        // grid.add(text2, 2, 2);
//        dialog.getDialogPane().setContent(grid);
//
//        // Add button to dialog
//        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
//        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
//        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
//        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
//
//        // Result converter for dialog
//        dialog.setResultConverter(new Callback<ButtonType, String>() {
//            @Override
//            public String call(ButtonType type) {
//                if (type == buttonTypeOk) {
//                    return text1.getText();
//                }
//                return null;
//            }
//        });
//
//        // Show dialog
//        Optional<String> result = dialog.showAndWait();
//        // Optional<PhoneBook> result = dialog.showAndWait();
//
//        if (result.isPresent()) {
//            String licenseKey = result.get();
//            licenseKeyEntered(licenseKey);
//        } else {
//            licenseStatus.setInvalidLicense("No license key");
//        }
//    }

}