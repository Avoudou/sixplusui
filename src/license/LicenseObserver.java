package license;

public interface LicenseObserver {

    public void notifyLicenseChanged(LicenseStatus status);
}
