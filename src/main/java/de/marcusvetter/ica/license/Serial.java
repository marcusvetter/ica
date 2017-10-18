package de.marcusvetter.ica.license;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;
import java.util.prefs.Preferences;

public class Serial {

    private static String PREFS_KEY = "ICA_SERIAL_NUMBER";

    public static void checkSerialNumber() {
        if (!Serial.isSerialValid()) {

            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Image Color Analyzer");
            dialog.setHeaderText("Please enter your serial number");
            Optional<String> result = dialog.showAndWait();

            try {
                if (result.isPresent()) {
                    String enteredSerial = result.get();
                    Serial.storeSerial(Double.parseDouble(enteredSerial));
                    if (!Serial.isSerialValid()) {
                        throw new Exception();
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                // Serial is not valid
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Image Color Analyzer");
                String s = "Serial number is not valid. Image Color Analyzer will be closed.";
                alert.setContentText(s);
                alert.showAndWait();

                System.exit(0);
            }
        }
    }

    private static boolean isSerialValid() {
        // Sometimes it is so easy guess a valid serial number ;-)
        return Serial.getStoredSerial() / 42 > 10 && Serial.getStoredSerial() % 42 == 0;
    }

    private static void storeSerial(double serialNumber) {
        Serial.getPrefs().putDouble(PREFS_KEY, serialNumber);
    }

    private static double getStoredSerial() {
        return Serial.getPrefs().getDouble(PREFS_KEY, 0);
    }

    private static Preferences getPrefs() {
        return Preferences.userRoot().node(Serial.class.getName());
    }

    public static void clearSerialNumber() {
        Serial.getPrefs().remove(PREFS_KEY);
    }

}
