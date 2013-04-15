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
    Search search;
    Search search1;

    public SearchMain() throws IOException, ParseException {
        this.search = new Search();
        this.search1 = new Search();
        search.searchICD10();
        search1.searchATC();
        ArrayList<DocumentMatch> ICDmatches = search.searchPatientCases();
        for(DocumentMatch d : ICDmatches){
            System.out.println(d.getID() + ", " + d.getSentenceId() + "\n" + d.getHits());
        }
        System.out.println(ICDmatches.size());
        ArrayList<DocumentMatch> ATCmatches = search1.searchPatientCases();
        System.out.println(ATCmatches.size());
        ArrayList<DocumentMatch> ICDmatches1 = search.searchNLHChapters();
        System.out.println(ICDmatches1.size());
        ArrayList<DocumentMatch> ATCmatches1 = search1.searchNLHChapters();
        System.out.println(ATCmatches1.size());
    }
   
    public static void main(String[] args) throws IOException, ParseException {
    	SearchMain main = new SearchMain();
        
    }  
}
