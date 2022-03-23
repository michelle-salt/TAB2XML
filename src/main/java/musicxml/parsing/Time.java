package musicxml.parsing;

public class Time {
	private int beats, beatType;
	
	public Time(int beats, int beatType) {
		this.beats = beats;
		this.beatType = beatType;
	}

	public int getBeats() {
		return beats;
	}

	public int getBeatType() {
		return beatType;
	}
}
