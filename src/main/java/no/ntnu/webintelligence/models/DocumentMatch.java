/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.models;

import java.util.ArrayList;
import org.apache.lucene.document.Document;

/**
 *
 * @author Kristin
 */
public class DocumentMatch {
    String id;
    int sentenceId;
    ArrayList<Document> hits;
    
    public DocumentMatch(String id, int sentenceId){
        this.id = id;
        this.sentenceId = sentenceId;
        hits = new ArrayList<Document>();
    }
    
    public String getID(){
        return id;
    }
    
    public int getSentenceId(){
        return sentenceId;
    }
    
    public ArrayList<Document> getHits(){
        return hits;
    }
    
    public void addHit(Document doc){
        hits.add(doc);
    }
    
    @Override
    public String toString(){
        return id + ", " + sentenceId + ": " + hits;
    }
}
