/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.stemmers;

import org.apache.lucene.analysis.no.NorwegianLightStemmer;

/**
 *
 * @author Kristin
 */
public class NorwegianStemmer {

    private String stemmedString;
    private NorwegianLightStemmer norStemmer;

    public NorwegianStemmer() {
        
    }
    
    public void stem(String inString){
        System.out.println("Hei " + inString);
        String[] strings = inString.split(" ");
        for (int i = 0; i < strings.length; i++) {
            String oldString = strings[i];
            char[] charArray = oldString.toCharArray();
            int stopIndex = norStemmer.stem(charArray, charArray.length);
            inString = "";
            for (int j = 0; j < stopIndex; j++) {
                inString += charArray[j];
            }
            strings[i] = inString;
        }
        stemmedString = "";
        for (int i = 0; i < strings.length; i++) {
            stemmedString += strings[i] + " ";
        }
    }

    public String getStemmedString() {
        return stemmedString;
    }
}
