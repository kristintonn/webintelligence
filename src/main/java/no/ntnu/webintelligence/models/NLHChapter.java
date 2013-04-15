package no.ntnu.webintelligence.models;

public class NLHChapter {
	private String id;
	private String title;
	private String text;
	private String[] sentences;
	
	public NLHChapter(String id, String title, String text) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.sentences = text.split("\\.");
	}

	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getText() {
		return text;
	}
	
	public String[] getSentences() {
		return sentences;
	}
	
	public void setSentences(String text) {
		this.sentences = text.split("\\.");
	}
	
        @Override
	public String toString(){
            return id + " " + title + "\n" + text;
        }
	
}
