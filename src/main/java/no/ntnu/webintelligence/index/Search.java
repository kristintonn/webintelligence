/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.index;

import java.io.IOException;
import no.ntnu.webintelligence.models.PatientCase;
import no.ntnu.webintelligence.parsers.PatientCaseParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
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

    public Search() throws IOException, ParseException {
        index = new Index();
        String queryString = "noe hovne lymfeknuter";
        PatientCaseParser cases = new PatientCaseParser();
        for (PatientCase pc : cases.getParsedCases()){
            for (int i = 0; i<pc.getSentences().length; i++){
                String queryS = pc.getSentences()[i];
                if (queryS.length() > 0){
                    System.out.println("Hei" + queryS);
                }
                
            }
        }
        QueryParser q = new QueryParser(Version.LUCENE_35, "label", index.getAnalyzer());
        Query query = q.parse(queryString);

        int hitsPerPage = 10;
        reader = IndexReader.open(index.getIndex());
        searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("id") + "\t" + d.get("label"));
        }
        searcher.close();
        reader.close();
    }

    //TODO: Search method with above code
    
    public static void main(String[] args) throws IOException, ParseException {
        Search search = new Search();
    }
}
