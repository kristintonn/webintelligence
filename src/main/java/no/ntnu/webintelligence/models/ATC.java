/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.models;

/**
 *
 * @author Kristin
 */
public class ATC {
    private String id;
    private String label;
    
    public ATC(String id, String label){
        this.id = id;
        this.label = label;
    }
    
    public String getId(){
        return id;
    }
    
    public String getLabel(){
        return label;
    }
    
    public void setLabel(String label){
        this.label = label;
    }
    
    @Override
    public String toString(){
        return id + ": " + label;
    }
}
