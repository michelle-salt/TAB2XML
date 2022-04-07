package GUI;

//Won't work if a measure extends one staff
public class MeasureLocation {
	private double startX, endX, startY;
	private String instrument;
	private boolean startRepeat, endRepeat;

	//Used at beginning of a measure
	public MeasureLocation(double startX, String instrument) {
		this.startX = startX;
		this.instrument = instrument;
	}

	//Public getters
	public double getStartX() {
		return startX;
	}

	public double getEndX() {
		return endX;
	}

	public double getStartY() {
		return startY;
	}

	public String getInstrument() {
		return instrument;
	}

	public boolean isStartRepeat() {
		return startRepeat;
	}

	public boolean isEndRepeat() {
		return endRepeat;
	}

	//Public setters
	public void setStartRepeat() {
		this.startRepeat = true;
	}

	public void setEndRepeat() {
		this.endRepeat = true;
	}
	
	public void setStartX(double startX) {
		this.startX = startX;
	}
	
	public void setEndX(double endX) {
		this.endX = endX;
	}

	public void setStartY(double startY) {
		this.startY = startY;
	}
}
