package no.ntnu.webintelligence.models;

import java.util.ArrayList;

public class ICD10 {
	private String id;
	private String label;
	private ArrayList<String> synonyms;
	
	public ICD10(String id, String label, ArrayList<String> synonyms) {
		this.id = id;
		this.label = label;
		this.synonyms = new ArrayList<String>();
	}

	public String getId() {
		return id;
	}
	
	public String getLabel() {
		return label;
	}
	
        public void setLabel(String label){
            this.label = label;
        }
        
	public ArrayList<String> getSynonyms() {
		return synonyms;
	}
	
	public void addSynonym(String syn){
		synonyms.add(syn);
	}
             
        @Override
        public String toString(){
            return id + ": " + label + ", " + synonyms.toString();
        }
	
}
