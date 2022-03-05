package GUI;

import java.util.ArrayList;

import org.jfugue.player.Player;

import converter.Converter;
import musicxml.parsing.Measure;
import musicxml.parsing.Note;
import musicxml.parsing.Parser;
import musicxml.parsing.Pitch;

public class MusicPlayer extends MainViewController{

	public void play(Converter converter) {
		
		Parser parse = new Parser(converter.getMusicXML());
		String instrument = parse.getInstrument();
		String string = "T50 V0 I[" + instrument + "] ";
		ArrayList<Measure> measures = parse.getMeasures();

		for (int i = 0; i < parse.getNumMeasures(); i++) { // go through every measure

			ArrayList<Note> notesInMeasure = measures.get(i).getNotes();

			for (int j = 0; j < measures.get(i).getNumNotes(); j++) { // go through all note sin specific measure

				Pitch pitch = measures.get(i).getNotes().get(j).getPitch();
				String altervalue = "";
				char step = pitch.getStep();
				int alter = pitch.getAlter();
				int octave = pitch.getOctave();
				char type = measures.get(i).getNotes().get(j).getType();

				if(alter != 0) {
					
					if(alter == 1) {
						
						altervalue = "#"; // insert sharp accidental
						
					}
					
					if(alter == -1) {
						
						altervalue = "b"; // insert flat accidental
						
					}
					
				}else {
					
					altervalue = "";
					
				}
				
				if(measures.get(i).getNotes().get(j).getDuration() == 0) { // if note is a grace note
					
					string += step + altervalue + octave + "o-";
					
					string += " ";
					
					string += step + altervalue + octave + "-o";
					
				}else {
						
						string += step + altervalue + octave + type;				
					
				}

				if (measures.get(i).getNumNotes() - j != 1) {

					if (measures.get(i).getNotes().get(j + 1).isChord()) { //  if next note is also part of the chord
						
						string += "+";

					}else{
						
						string += " "; // add a space to split up notes
						
					}
					
				}else { // add the tie thing around here i think
					
					string += " ";
					
				}
				
				
				
				
				
			}
			
			if(parse.getNumMeasures() - i != 1) {
				
				string += " | "; // add space between notes to indicate measures
				
			}
		}
		
		System.out.println(string);
		Player player = new Player();
		player.play(string);
		
	}
	
}
