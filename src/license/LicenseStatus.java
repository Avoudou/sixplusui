package license;

import java.util.ArrayList;
import java.util.List;

public class LicenseStatus {

    private boolean checked;

    private boolean valid;

    private String reason;

    private List<LicenseObserver> observers;

    public LicenseStatus() {
        observers = new ArrayList<LicenseObserver>();
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isValid() {
        return valid;
    }

    public String getReason() {
        return reason;
    }

    public void confirmValidLicense() {
        checked = true;
        valid = true;
        notifyObservers();
    }

    public void setInvalidLicense(String reason) {
        checked = true;
        valid = false;
        this.reason = reason;
        notifyObservers();
    }

    public void addLicenseObserver(LicenseObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (LicenseObserver observer : observers) {
            observer.notifyLicenseChanged(this);
        }
    }

}
