package no.ntnu.webintelligence.models;

import java.util.ArrayList;

public class ICD10 {
	private String id;
	private String title;
	private String description;
	private ArrayList<String> synonyms;
	
	public ICD10(String id, String title, String description,
			ArrayList<String> synonyms) {
		this.id = id;
		this.title = title;
		this.description = description;
		synonyms = new ArrayList<String>();
	}

	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ArrayList<String> getSynonyms() {
		return synonyms;
	}
	
	public void addSynonym(String syn){
		synonyms.add(syn);
	}
	
}
