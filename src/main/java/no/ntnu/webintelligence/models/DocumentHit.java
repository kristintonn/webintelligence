package no.ntnu.webintelligence.models;

import org.apache.lucene.document.Document;

public class DocumentHit {
	
	private Float score;
	private Document document;
	private Float numberOfLines;

	public DocumentHit(Float score, Document document){
		this.score = score;
		this.document = document;
		this.numberOfLines = new Float(1f);
	}

	public Float getScore() {
		return score/numberOfLines;
	}

	
	public void addScore(Float score){
		this.score += score;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
	
	public void setNumberOfLines(Float nol){
		this.numberOfLines = nol;
	}
	

}
