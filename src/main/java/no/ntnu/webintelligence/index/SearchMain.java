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
    private Search search;
    private Float[][] matrix;
    private List<Document> terms;
    private List<DocumentMatch> chapters;
    private List<DocumentMatch> caseNotes;
   

    public SearchMain() throws IOException, ParseException {
        this.search = new Search();
        /* uncommet to do other searches */
//         searchTermsInCases(true, false); //Icd10 in cases
//         searchTermsInCases(false, true);  //atc in cases
//         searchTermsInNLMH(true, false); //icd10 in NLMH
//         searchTermsInNLMH(false, true); //atc in NLMH
         
//         searchCaseNotesForNLMH(true, true); // search for NLMH chapters for case notes (using icd10 and atc)
         searchCasesForNLMH(true, true); //search for NLMH chapters for cases (using icd10 and atc)
    }
    
    public void searchTermsInCases(boolean icd10, boolean atc) throws IOException, ParseException{
    	if(icd10 && !atc){ 
    		search.searchICD10();
    	}else if(atc && !icd10){
    		search.searchATC();
    	}else{
    		search.searchICD10AndATC();
    	}
    	caseNotes = search.searchPatientCases();
        for(DocumentMatch d : caseNotes){
        	System.out.println("----------------------");
            System.out.println(d.getID() + ", " + d.getSentenceId());
            for(DocumentHit h : d.getHits()){
            	System.out.println(h.getDocument().get("id") + " " + h.getDocument().get("label")+  " - " + h.getScore());
            }
            System.out.println("\n----------------------");
        }
    	
    }
    public void searchTermsInNLMH(boolean icd10, boolean atc) throws IOException, ParseException{
    	if(icd10 && !atc){ 
    		search.searchICD10();
    	}else if(atc && !icd10){
    		search.searchATC();
    	}else{
    		search.searchICD10AndATC();
    	}
    	chapters = search.searchNLHChapters();
        for(DocumentMatch d : chapters){
        	System.out.println("----------------------");
            System.out.println(d.getID() + ", " + d.getSentenceId());
            for(DocumentHit h : d.getHits()){
            	System.out.println(h.getDocument().get("id")+ " " + h.getDocument().get("label")+  " - " + h.getScore());
            }
            System.out.println( "\n----------------------");
        }
    	
    }
    public void searchCaseNotesForNLMH(boolean icd10, boolean atc) throws ParseException, IOException{
    	if(icd10 && !atc){ 
    		search.searchICD10();
    	}else if(atc && !icd10){
    		search.searchATC();
    	}else{
    		search.searchICD10AndATC();
    	}
		caseNotes = search.searchPatientCases();
		System.out.println("IDC matches patient case size: " + caseNotes.size());
		ArrayList<DocumentMatch> ICDmatches1 = search.searchNLHChapters();
		System.out.println("ICD matches NLH size: " + ICDmatches1.size());
		makeTermByDocumentMatrix(ICDmatches1, false);
		for (int i = 0; i < caseNotes.size(); i++) {
			findNoteChapters(i);
		}
    }
    public void searchCasesForNLMH(boolean icd10, boolean atc) throws IOException, ParseException{
    	if(icd10 && !atc){ 
    		search.searchICD10();
    	}else if(atc && !icd10){
    		search.searchATC();
    	}else{
    		search.searchICD10AndATC();
    	}
		caseNotes = search.searchPatientCases();
		System.out.println("IDC matches patient case size: " + caseNotes.size());
		ArrayList<DocumentMatch> ICDmatches1 = search.searchNLHChapters();
		System.out.println("ICD matches NLH size: " + ICDmatches1.size());
		makeTermByDocumentMatrix(ICDmatches1, true);
		for (int i = 0; i < caseNotes.size(); i++) {
			findNoteChapters(i);
		}
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
    
    public List<DocumentMatch> combindSentences(List<DocumentMatch> list){
       	
    	System.out.println("Combinding sentences...");
    	Map<String, List<DocumentHit>> chapterHits = new HashMap<String, List<DocumentHit>>();
    	Map<String, Float> chapterSentences = new HashMap<String, Float>();
    	String hitId;
    	Float nos; //number of sentences
    	boolean found;
		for (DocumentMatch dm : list) {
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
		
		
//		for (Iterator<Entry<String, List<DocumentHit>>> iterator = chapterHits.entrySet()
//				.iterator(); iterator.hasNext();) {
//			Entry<String, List<DocumentHit>> e = iterator.next();
//			System.out.println("Chapter: " + e.getKey());
//			for(DocumentHit d : e.getValue()) System.out.println("        " + d.getDocument().get("id") + " " + d.getScore());
//		}
    	
    	System.out.println("Making chapter list...");
    	Entry<String, List<DocumentHit>> chapter;
    	Iterator<Entry<String, List<DocumentHit>>> iterator = chapterHits.entrySet().iterator();
    	List<DocumentMatch> newList = new ArrayList<DocumentMatch>();
    	while(iterator.hasNext()){
    		chapter = iterator.next();
    		DocumentMatch dm = new DocumentMatch(chapter.getKey(), 0);
    		dm.setHits(chapter.getValue());
    		newList.add(dm);
    	}
    	return newList;
    }
    
    public void makeTermByDocumentMatrix(List<DocumentMatch> nlmh, boolean atomicCase){
    	
    	/* Combine sentences in NLMH into documents */
    	chapters = combindSentences(nlmh);
    	
    	/* combine case notes to case if atomicCase is true */
    	if(atomicCase) caseNotes = combindSentences(caseNotes);
    	
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
		System.out.println("Case: " + caseNotes.get(caseNotePlacement).getID() /*+  "- Sentence: " + caseNotes.get(caseNotePlacement).getSentenceId() */+ " - Hits: "+ caseNotes.get(caseNotePlacement).getHits().size());
		System.out.println("-------------------------------------\n");
		
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
		for(Entry<String, Float> s : sortedHits.entrySet()){
			System.out.println(i + "." + s.getKey() /*+ " " + s.getValue()*/);
			i++;
			if(i>10) break;
		}
		System.out.println("End of list.\n-------------------------------------");

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
