/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.webintelligence.models.DocumentMatch;
import no.ntnu.webintelligence.models.NLHChapter;
import no.ntnu.webintelligence.models.PatientCase;
import no.ntnu.webintelligence.parsers.NLHChapterParser;
import no.ntnu.webintelligence.parsers.PatientCaseParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.DefaultSimilarity;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.Version;

/**
 *
 * @author Kristin
 */
public class Search {
    private Index index;
    private IndexSearcher searcher;
    private IndexReader reader;
    private QueryParser parser;
    private final int HITS_PER_PAGE = 3;

    public Search() throws IOException, ParseException {
        
    }

    public void searchICD10() throws IOException{
        index = new Index();
        index.addICD10();
        parser = new QueryParser(Version.LUCENE_35, "label", index.getAnalyzer());
        reader = IndexReader.open(index.getIndex());
        searcher = new IndexSearcher(reader);
    }
    
    public void searchATC() throws IOException{
        index = new Index();
        index.addATC();
        parser = new QueryParser(Version.LUCENE_35, "label", index.getAnalyzer());
        reader = IndexReader.open(index.getIndex());
        searcher = new IndexSearcher(reader);
    }
    
    ScoreDoc[] searchDocument(String queryString, int hitsPerPage) throws ParseException, IOException {
        Query query = parser.parse(queryString);
        searcher.setSimilarity(new DefaultSimilarity() {
            public float idf(int i, int i1) {
                return 1;
            }
        });
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        return hits;
    }

    public IndexSearcher getIndexSearcher() {
        return searcher;
    }
    
    public void searchTherapyAndDrugChapterInNLMH(List<PatientCase> caseList) throws IOException, ParseException{
    	index = new Index();
    	index.addNLMH(true);
    	parser = new QueryParser(Version.LUCENE_35, "text", index.getAnalyzer());
        reader = IndexReader.open(index.getIndex());
        searcher = new IndexSearcher(reader);
        
        
        ArrayList<DocumentMatch> matches = new ArrayList<DocumentMatch>();
    	for(PatientCase c : caseList){
    		for(int j=0; j < c.getSentences().length; j++){
    			DocumentMatch match = new DocumentMatch(Integer.toString(c.getId()), j);
    			String s = c.getSentences()[j];
    			s = s.trim();
    			if(s.length() > 0){
    				System.out.println("S: " + s);
    				
    				ScoreDoc[] hits = searchDocument(s, HITS_PER_PAGE);
    				//System.out.println("Found " + hits.length + " hits");
    				for(int i = 0; i < hits.length; i++){
    					int docId = hits[i].doc;
    					Document d = searcher.doc(docId);
    					//System.out.println((i + 1) + ". " + d.get("id") + "\t" + d.get("title"));
    					match.addHit(d);
    				}
    				if(match.getHits().size() > 0){
    					matches.add(match);
    				}
    			}
    			
    		}
    	}
    }
    
    public ArrayList<DocumentMatch> searchPatientCases() throws ParseException, IOException{
    
        PatientCaseParser cases = new PatientCaseParser();
    	//search.searchTherapyAndDrugChapterInNLMH(cases.getParsedCases());
        ArrayList<DocumentMatch> matches = new ArrayList<DocumentMatch>();
        for (PatientCase pc : cases.getParsedCases()) {
            for (int i = 0; i < pc.getSentences().length; i++) {
                DocumentMatch match = new DocumentMatch(Integer.toString(pc.getId()), i);
                String queryS = pc.getSentences()[i];
                queryS = queryS.trim();
                if (queryS.length() > 0) {
                    //System.out.println("S: " + queryS);       
                    ScoreDoc[] hits = this.searchDocument(queryS, 3);
                    //System.out.println("Found " + hits.length + " hits.");
                    for (int j = 0; j < hits.length; ++j) {
                        int docId = hits[j].doc;
                        Document d = this.getIndexSearcher().doc(docId);
                        //System.out.println((j + 1) + ". " + d.get("id") + "\t" + d.get("label"));
                        match.addHit(d);
                    }
                }
                matches.add(match);
            }
        }
        return matches;
}
    
    public ArrayList<DocumentMatch> searchNLHChapters() throws ParseException, IOException{
    
        NLHChapterParser parser = new NLHChapterParser();
    	//search.searchTherapyAndDrugChapterInNLMH(cases.getParsedCases());
        ArrayList<DocumentMatch> matches = new ArrayList<DocumentMatch>();
        for (NLHChapter chapter : parser.getChapters()) {
            for (int i = 0; i < chapter.getSentences().length; i++) {
                DocumentMatch match = new DocumentMatch(chapter.getId(), i);
                String queryS = chapter.getSentences()[i];
                String escaped = QueryParser.escape(queryS);
                escaped = escaped.trim();
                if (escaped.length() > 0) {
                    //System.out.println("S: " + queryS);       
                    ScoreDoc[] hits = this.searchDocument(escaped, 3);
                    //System.out.println("Found " + hits.length + " hits.");
                    for (int j = 0; j < hits.length; ++j) {
                        int docId = hits[j].doc;
                        Document d = this.getIndexSearcher().doc(docId);
                        //System.out.println((j + 1) + ". " + d.get("id") + "\t" + d.get("label"));
                        match.addHit(d);
                    }
                }
                matches.add(match);
            }
        }
        return matches;
}

    public static void main(String[] args) throws IOException, ParseException {
    	
    }
}
