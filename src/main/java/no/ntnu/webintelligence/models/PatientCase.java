package no.ntnu.webintelligence.models;

public class PatientCase {
	private int id;
	private String text;
	private String[] sentences;
	
	public PatientCase(int id, String text) {
		this.id = id;
		this.text = text;
		this.sentences = text.split("\\.");
	}
	
	public int getId() {
		return id;
	}
	
	public String getText() {
		return text;
	}
	
	public String[] getSentences() {
		return sentences;
	}
	

        public String getSentence(int i){
            return sentences[i];
        }
        
        @Override
        public String toString(){
            return id + ": " + sentences.length + " sentences";
        }

}
