package no.ntnu.webintelligence.models;

public class PatientCase {
	private int id;
	private String text;
	private String[] sentences;
	
	public PatientCase(int id, String text) {
		this.id = id;
		this.text = text;
		setSentences(text);
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
	
	public void setSentences(String text) {
		this.sentences = text.split(".");
	}
        
        public String toString(){
            return id + ":" + text;
        }
	
}
