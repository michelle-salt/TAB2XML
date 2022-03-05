package musicxml.parsing;

public class Tied {
	private boolean start, stop, cont, letRing;
	
	public Tied() {
		start = false;
		stop = false;
		cont = false;
		letRing = false;
	}

	//Public getters
	public boolean getStart() {
		return start;
	}

	public boolean getStop() {
		return stop;
	}

	public boolean getCont() {
		return cont;
	}

	public boolean getLetRing() {
		return letRing;
	}

	//Public setters
	public void setStart(boolean start) {
		this.start = start;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public void setCont(boolean cont) {
		this.cont = cont;
	}

	public void setLetRing(boolean letRing) {
		this.letRing = letRing;
	}
	
}
