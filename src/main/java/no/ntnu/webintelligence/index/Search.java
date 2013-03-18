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

    public Search() throws IOException, ParseException {
        index = new Index();
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

    public static void main(String[] args) throws IOException, ParseException {
        Search search = new Search();
        PatientCaseParser cases = new PatientCaseParser();
        ArrayList<DocumentMatch> matches = new ArrayList<DocumentMatch>();
        for (PatientCase pc : cases.getParsedCases()) {
            for (int i = 0; i < pc.getSentences().length; i++) {
                DocumentMatch match = new DocumentMatch(pc.getId(), i);
                String queryS = pc.getSentences()[i];
                queryS = queryS.trim();
                if (queryS.length() > 0) {
                    System.out.println("S: " + queryS);
                    
                    ScoreDoc[] hits = search.searchDocument(queryS, 3);
                    System.out.println("Found " + hits.length + " hits.");
                    for (int j = 0; j < hits.length; ++j) {
                        int docId = hits[j].doc;
                        Document d = search.getIndexSearcher().doc(docId);
                        System.out.println((j + 1) + ". " + d.get("id") + "\t" + d.get("label"));
                        match.addHit(d);
                    }
                }
                matches.add(match);
            }
        }
        
        for (DocumentMatch dm : matches){
            System.out.println(dm);
        }
        
    }
}
