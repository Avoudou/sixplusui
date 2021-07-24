package pokerAppUi.uiElements;

public class HandElement {

	private String handName;
	private double percentage;
    private boolean played;

	public HandElement(String handName) {
		this.handName = handName;
		percentage = 999;
	}

    public HandElement(String handName, double percentage, boolean played) {
		super();
		this.handName = handName;
		this.percentage = percentage;
        this.played = played;
	}

	public double getPercentage() {
		return percentage;
	}

	public String getHandName() {
		return handName;
	}

    public boolean isPlayed() {
        return played;
    }

	public void setHandName(String handName) {
		this.handName = handName;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

}
