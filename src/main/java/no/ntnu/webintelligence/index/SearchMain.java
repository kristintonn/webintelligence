/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.index;

import java.io.IOException;
import java.util.ArrayList;
import no.ntnu.webintelligence.models.DocumentMatch;
import no.ntnu.webintelligence.models.PatientCase;
import no.ntnu.webintelligence.parsers.PatientCaseParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;

/**
 *
 * @author Ida Katrine
 */
public class SearchMain {
    Search searchICD10;
    Search searchATC;

    public SearchMain() throws IOException, ParseException {
        this.searchICD10 = new Search();
        this.searchATC = new Search();
        
      
        
        /* ICD search */
        searchICD10.searchICD10();
        
        ArrayList<DocumentMatch> ICDmatches = searchICD10.searchPatientCases();
        
//        for(DocumentMatch d : ICDmatches){
//            System.out.println(d.getID() + ", " + d.getSentenceId() + "\n" + d.getHits());
//        }
        System.out.println("IDC matches patient case size: " + ICDmatches.size());
  
        
        ArrayList<DocumentMatch> ICDmatches1 = searchICD10.searchNLHChapters();
//        for(DocumentMatch d : ICDmatches1){
//            System.out.println(d.getID() + ", " + d.getSentenceId() + "\n" + d.getHits());
//        }
        System.out.println("ICD matches NLH size: " + ICDmatches1.size());
        
        searchtest(ICDmatches, ICDmatches1);
        
        
//        searchICD10.searchPatientCasesAgainsNLHChapters(ICDmatches1);
        
        /* ATC 
        searchATC.searchATC();
        ArrayList<DocumentMatch> ATCmatches = searchATC.searchPatientCases();
        System.out.println("ATC matches size: " + ATCmatches.size());search.se
        
        ArrayList<DocumentMatch> ATCmatches1 = searchATC.searchNLHChapters();
        System.out.println("ATC matches1 size: " + ATCmatches1.size());
*/
    }
    
    public void searchtest(ArrayList<DocumentMatch> ICD10InCases, ArrayList<DocumentMatch> ICD10InNLH) throws IOException, ParseException{
    	
    	Search search = new Search();
    	    	
    	search.setResultIndex(ICD10InNLH);
    	
    	for(DocumentMatch caseICD10 : ICD10InCases){
    		/**
    		 * do search on each case sentence against ICD10InNLH
    		 * return ArrayList<DocumentMatch> that holds chapters relevant to sentence
    		 */
    		ArrayList<DocumentMatch> matches = search.searchInResults(caseICD10); //returns chapters relevant to sentence
    		
    		for(DocumentMatch dm2 : matches){
    			System.out.println(dm2.getID() + ", " + dm2.getSentenceId() + ":");
    			for(Document d : dm2.getHits()){
    				System.out.println(d.get("id") + "," + d.get("label"));
    			}
    				
    		}
    		
    		
    	}
//    	return NHLChaptersInCases;
    }
   
    public static void main(String[] args) throws IOException, ParseException {
    	SearchMain main = new SearchMain();
        
    }  
}
