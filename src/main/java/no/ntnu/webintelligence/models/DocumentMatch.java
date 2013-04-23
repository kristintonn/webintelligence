/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;

/**
 *
 * @author Kristin
 */
public class DocumentMatch {
    String id;
    int sentenceId;
    List<DocumentHit> hits;
    
    public DocumentMatch(String id, int sentenceId){
        this.id = id;
        this.sentenceId = sentenceId;
        hits = new ArrayList<DocumentHit>();
    }
    
    public String getID(){
        return id;
    }
    
    public int getSentenceId(){
        return sentenceId;
    }
    
    public List<DocumentHit> getHits(){
        return hits;
    }
    
    public void setHits(List<DocumentHit> hits){
    	this.hits = new ArrayList<DocumentHit>(hits);
    }
    
    public void addHit(Float score, Document doc){
        hits.add(new DocumentHit(score, doc));
    }
    
    @Override
    public String toString(){
        return id + ", " + sentenceId + ": " + hits;
    }
}
