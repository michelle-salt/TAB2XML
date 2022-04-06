package musicxml.parsing;

public class TimeModification {
	private int actualNotes;
	private int normalNotes;
	
	public TimeModification(int actual, int normal) {
		this.actualNotes = actual;
		this.normalNotes = normal;
	}

	//Public getters
	public int getActualNotes() {
		return actualNotes;
	}

	public int getNormalNotes() {
		return normalNotes;
	}
}
