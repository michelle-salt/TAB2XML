package musicxml.parsing;

public class Repeat {
	private Barline barline;
	private Direction direction;
	
	public Repeat(Barline b, Direction d) {
		this.barline = b;
		this.direction = d;
	}

	//Public getters
	public Barline getBarline() {
		return barline;
	}

	public Direction getDirection() {
		return direction;
	}
}
