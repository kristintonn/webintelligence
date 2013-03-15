package no.ntnu.webintelligence.models;

import java.util.ArrayList;

public class ICD10 {
	private String id;
	private String title;
	private ArrayList<String> synonyms;
	
	public ICD10(String id, String title, ArrayList<String> synonyms) {
		this.id = id;
		this.title = title;
		this.synonyms = new ArrayList<String>();
	}

	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
        public void setTitle(String title){
            this.title = title;
        }
        
	public ArrayList<String> getSynonyms() {
		return synonyms;
	}
	
	public void addSynonym(String syn){
		synonyms.add(syn);
	}
             
        @Override
        public String toString(){
            return id + ": " + title + ", " + synonyms.toString();
        }
	
}
