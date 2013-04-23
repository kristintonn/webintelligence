/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import no.ntnu.webintelligence.models.DocumentHit;
import no.ntnu.webintelligence.models.DocumentMatch;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;

/**
 *
 * @author Ida Katrine
 */
public class SearchMain {
    private Search searchICD10;
    private Search searchATC;
    private Float[][] matrix;
    private List<Document> terms;
    private List<DocumentMatch> chapters;
    private List<DocumentMatch> caseNotes;

    public SearchMain() throws IOException, ParseException {
        this.searchICD10 = new Search();
        this.searchATC = new Search();
        
      
        
        /* ICD search */
        searchICD10.searchICD10();
        
        caseNotes = searchICD10.searchPatientCases();
        
        for(DocumentMatch d : caseNotes){
            System.out.println(d.getID() + ", " + d.getSentenceId() + "\n");
            for(DocumentHit h : d.getHits()){
            	System.out.println("        " + h.getDocument().get("id") + " " + h.getDocument().get("label")+  " - " + h.getScore());
            }
        }
        System.out.println("IDC matches patient case size: " + caseNotes.size());
  
        ArrayList<DocumentMatch> ICDmatches1 = searchICD10.searchNLHChapters();
//        for(DocumentMatch d : ICDmatches1){
//            System.out.println(d.getID() + ", " + d.getSentenceId() + "\n" + d.getHits());
//        }
        System.out.println("ICD matches NLH size: " + ICDmatches1.size());
        
        makeTermByDocumentMatrix(ICDmatches1);
        
//        for(int i=0; i<caseNotes.size(); i++)
        int chapter1 = 0;
        for(DocumentMatch dm : caseNotes){
        	if(dm.getID().equals("1")) chapter1++;
        }
        for(int i=0; i<chapter1; i++){
        	System.out.println("Case: " + caseNotes.get(i).getID() +  "-" + caseNotes.get(i).getSentenceId() + " - "+ caseNotes.get(i).getHits().size());
        	findNoteChapters(i);
        }
        
        
        /* ATC 
        searchATC.searchATC();
        ArrayList<DocumentMatch> ATCmatches = searchATC.searchPatientCases();
        System.out.println("ATC matches size: " + ATCmatches.size());search.se
        
        ArrayList<DocumentMatch> ATCmatches1 = searchATC.searchNLHChapters();
        System.out.println("ATC matches1 size: " + ATCmatches1.size());
*/
    }
    
//    public void searchtest(ArrayList<DocumentMatch> ICD10InCases, ArrayList<DocumentMatch> ICD10InNLH) throws IOException, ParseException{
//    	
//    	Search search = new Search();
//    	    	
//    	search.setResultIndex(ICD10InNLH);
//    	
//    	for(DocumentMatch caseICD10 : ICD10InCases){
//    		/**
//    		 * do search on each case sentence against ICD10InNLH
//    		 * return ArrayList<DocumentMatch> that holds chapters relevant to sentence
//    		 */
//    		ArrayList<DocumentMatch> matches = search.searchInResults(caseICD10); //returns chapters relevant to sentence
//    		
//    		for(DocumentMatch dm2 : matches){
//    			System.out.println(dm2.getID() + ", " + dm2.getSentenceId() + ":");
//    			for(Document d : dm2.getHits()){
//    				System.out.println(d.get("id") + "," + d.get("label"));
//    			}
//    		}
//    		
//    		
//    	}
//    	return NHLChaptersInCases;
//    }
    
    public void makeTermByDocumentMatrix(List<DocumentMatch> nlmh){
    	
    	/* Combine sentences in NLMH into documents */
    	System.out.println("Combinding sentences...");
    	Map<String, List<DocumentHit>> chapterHits = new HashMap<String, List<DocumentHit>>();
    	Map<String, Float> chapterSentences = new HashMap<String, Float>();
    	String hitId;
    	Float nos; //number of sentences
    	boolean found;
		for (DocumentMatch dm : nlmh) {
			List<DocumentHit> hitList;
			/* keep count of number of sentences in a case */
			if(!chapterSentences.containsKey(dm.getID())){
				nos = new Float(1f);
				chapterSentences.put(dm.getID(), nos);
			}else{
				nos = chapterSentences.get(dm.getID());
				nos = nos+1f;
				chapterSentences.put(dm.getID(), nos);
			}
			
			/* Create hitList for chapter if the chapter does not already exist */
			if (!chapterHits.containsKey(dm.getID())) {
				hitList = new ArrayList<DocumentHit>();
				for (DocumentHit hit : dm.getHits()) {
					hitList.add(hit);
				}
			} else {
				hitList = chapterHits.get(dm.getID()); // get hits for chapter from map
				for (DocumentHit hit : dm.getHits()) {
					found = false;
					hitId = hit.getDocument().get("id");
					for (DocumentHit chapterHit : hitList) {
						
						if (hitId.equals(chapterHit.getDocument().get("id"))) {
							/*
							 * The document already exists in chapter, add to document
							 * score (since it is in multiple sentences)
							 */
							chapterHit.addScore(hit.getScore());
							found = true;
							break;
						}
					}
					if (!found) {
						/* The document does not exist in hit list, add it! */
						hitList.add(hit);
					}
				}
			}
			for(DocumentHit h : hitList){
				h.setNumberOfLines(nos);
			}
			chapterHits.put(dm.getID(), hitList); // reset chapter hit list
		}
		
		
		for (Iterator<Entry<String, List<DocumentHit>>> iterator = chapterHits.entrySet()
				.iterator(); iterator.hasNext();) {
			Entry<String, List<DocumentHit>> e = iterator.next();
			System.out.println("Chapter: " + e.getKey());
			for(DocumentHit d : e.getValue()) System.out.println("        " + d.getDocument().get("id") + " " + d.getScore());
		}
    	
    	System.out.println("Making chapter list...");
    	Entry<String, List<DocumentHit>> chapter;
    	Iterator<Entry<String, List<DocumentHit>>> iterator = chapterHits.entrySet().iterator();
    	chapters = new ArrayList<DocumentMatch>();
    	while(iterator.hasNext()){
    		chapter = iterator.next();
    		DocumentMatch dm = new DocumentMatch(chapter.getKey(), 0);
    		dm.setHits(chapter.getValue());
    		chapters.add(dm);
    	}
    	
    	/* Get all the ICD10Terms found in searches */
    	System.out.println("Gathering terms from Patient Cases...");
    	List<Document> termsFromCases = getTermsFromResults(caseNotes, null);
    	System.out.println("Gathering terms from NLMH...");
    	terms = getTermsFromResults(chapters, termsFromCases);
    	

	
    	/* Make term-by-document matrix where values will be document scores for term */
    	matrix = new Float[caseNotes.size()+chapters.size()][terms.size()];
    	String id;
    	Document doc;
    	System.out.println("Making term-by-document matrix from patient cases(" + caseNotes.size() + ") and terms("+terms.size()+")...");
    	/* Put patient cases into matrix */
    	for(int i=0; i<caseNotes.size(); i++){
    		for(DocumentHit hit : caseNotes.get(i).getHits()){
    			doc = hit.getDocument();
    			id = doc.get("id");
    			for(int j=0; j<terms.size(); j++){
    				if(id.equals(terms.get(j).get("id"))){
    					matrix[i][j] = hit.getScore();
    					break;
    				}
    			}
    		}
    	}
    	
    	System.out.println("Making term-by-document matrix from chapters(" + chapters.size() + ") and terms("+terms.size()+")...");
    	
    	/* Put NLMH hits into matrix */
    	for(int i=caseNotes.size(); i<chapters.size(); i++){
    		for(DocumentHit hit : chapters.get(i).getHits()){
    			doc = hit.getDocument();
    			id = doc.get("id");
    			for(int j=0; j<terms.size(); j++){
    				if(id.equals(terms.get(j).get("id"))){
    					matrix[i][j] = hit.getScore();
    					break;
    				}
    			}
    		}
    	}
    }
    
    
	public void findNoteChapters(int caseNotePlacement) {
		Map<String, Float> hits = new HashMap<String, Float>();
		/* For a case sentence terms, find the terms entries of NLMH chapters */
		System.out.println("-------------------------------------\nFinding chapters for note nr :" + caseNotePlacement);
		
		for (int j = 0; j < matrix[caseNotePlacement].length; j++) {
			if (matrix[caseNotePlacement][j] != null
					&& matrix[caseNotePlacement][j] != 0f) {
				/* found term in patient case */
				for (int k = caseNotes.size(); k < matrix.length; k++) {
					if (matrix[k][j] != null && matrix[k][j] != 0f) {

						/* found chapter that contains term */
						/* add chapter with corresponding score to hits */
						if(hits.containsKey(chapters.get(k).getID())){
							/* Chapter has been found for another term, this should give added score */
							Float value = hits.get(chapters.get(k).getID());
							value = new Float(value+matrix[k][j]);
						}else{
							hits.put(chapters.get(k).getID(), matrix[k][j]); 
						}
						
					}
				}
			}
		}

		/* Rank NLMH chapters for each case sentence */
		Map<String, Float> sortedHits = sortByValue(hits);
		int i = 1;
		System.out.println("RANKED LISTS FOR PATIENT CASE: " + caseNotes.get(caseNotePlacement).getID() + ", " + caseNotes.get(caseNotePlacement).getSentenceId());
		for(Entry<String, Float> s : sortedHits.entrySet()){
			System.out.println(i + "." + s.getKey() + " " + s.getValue());
			i++;
			if(i>10) break;
		}
		System.out.println("End of list.\n-------------------------------------");
		/**
		 * TODO tar nå hensyn til occurences av ICD10 term, men ikke antall
		 * linjer i NLMH. Vi ønsker kanskje at en artikkel som inneholder mange
		 * linjer med en ICD10 skal skåre høyere...
		 */
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
    	

    /**
     * 
     * @param searchResults search results to get terms from
     * @param terms pre-existing terms (terms that has been found in advance)
     * @return terms found in results
     */
    private List<Document> getTermsFromResults(List<DocumentMatch> searchResults, List<Document> terms){
    	if(terms == null){
    		terms = new ArrayList<Document>();
    	}
    	boolean found;
    	String id;
    	for(DocumentMatch dm : searchResults){
    		for(DocumentHit dh : dm.getHits()){
    			found = false;
    			id = dh.getDocument().get("id");
    			/* Check if term already exists in terms list */
    			for(Document t : terms){
    				if(id.equals(t)){
    					//TODO does not consider synonyms at the moment
    					found = true;
    					break;
    				}
    			}
    			if(!found){ //term has not been found earlier, we need to add it!
    				terms.add(dh.getDocument());
    			}
    		}
    	}
    	return terms;
    }
   
    public static void main(String[] args) throws IOException, ParseException {
    	SearchMain main = new SearchMain();
        
    }  
}
